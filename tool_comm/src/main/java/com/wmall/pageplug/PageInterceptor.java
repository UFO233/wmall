package com.wmall.pageplug;

import com.wmall.vo.Pager;
import org.apache.ibatis.builder.xml.dynamic.ForEachSqlNode;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import javax.xml.bind.PropertyException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by asus-pc on 2017/6/6.
 * 分页拦截器，用于拦截�?要分页的查询操作，然后对其进行分页处理�??
 * 利用拦截器实现mybatis分页的原理�??
 * 要利用JDBC对数据库进行操作就必须要有个对应的Statement对象，Mybatis在执行sql语句的Statement对象,而且对应的sql语句是在Statement之前产生的，
 * �?以我们就可以在他生成Statement之前用来生成Statement的sql语句下手。在mybatis中Statement语句是�?�过RoutingStatementHandler对象的prepare方法生成的�??
 * �?以利用拦截器实现mybatis分页的一个�?�路就是拦截StatementHandler接口的prepare方法，然后在拦截器方法中把sql改成对应的分页查询sql语句，之后在调用StatementHandler
 * 对象的prepare方法，即调用invocation.proceed()�?
 * 对于分页而言，在拦截器里面我们还�?要做的一个操作就是统计满足条件记录的�?共有多少，这是�?�过获取到了原始的sql语句后，把它改成对应的统计语句再利用mybatis
 * 封装好的参数和设置参数的功能把sql语句中的参数进行替换，之后在执行记录数的sql语句进行总记录数的统计�??
 */
@Intercepts({@Signature(type = StatementHandler.class,method="prepare",args = {Connection.class})})
public class PageInterceptor implements Interceptor {

    private static String dialect = ""; // 数据库方�?
    private static String pageSqlId = ""; // mapper.xml中需要拦截的ID(正则匹配)
    private static String DIALECT_STR = "dialect";

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object intercept(Invocation ivk) throws Throwable {
        if (ivk.getTarget() instanceof RoutingStatementHandler) {
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
            BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
            MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");

            if (mappedStatement.getId().matches(pageSqlId)) { // 拦截�?要分页的SQL
                BoundSql boundSql = delegate.getBoundSql();
                Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属�?�对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为�?
                if (parameterObject == null) {
                    throw new NullPointerException("parameterObject尚未实例化！");
                } else {
                    Connection connection = (Connection) ivk.getArgs()[0];
                    String sql = boundSql.getSql();
                    String countSql = "select count(0) as tmp_count from (" + sql + ") t"; // 记录统计
                    PreparedStatement countStmt = connection.prepareStatement(countSql);

                    BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), sql, boundSql.getParameterMappings(), parameterObject);
                    Field metaParamsField = ReflectHelper.getFieldByFieldName(boundSql, "metaParameters");
                    if (metaParamsField != null) {
                        MetaObject mo = (MetaObject) ReflectHelper.getValueByFieldName(boundSql, "metaParameters");
                        ReflectHelper.setValueByFieldName(countBS, "metaParameters", mo);
                    }
                    setParameters(countStmt, mappedStatement, countBS, parameterObject);
                    ResultSet rs = countStmt.executeQuery();
                    int count = 0;
                    if (rs.next()) {
                        count = rs.getInt(1);
                    }
                    rs.close();
                    countStmt.close();

                    Pager page = null;
                    if (parameterObject instanceof Pager) { // 参数就是Page实体
                        page = (Pager) parameterObject;
                        page.setTotal(count);
                    } else if ((parameterObject instanceof Map) && !(parameterObject instanceof List)) {// 参数为map，Page为map中的�?个对�?
                        Map<Object, Object> map = (Map<Object, Object>) parameterObject;
                        if (map.containsKey("list")) {
                            List<Object> list = (List<Object>) map.get("list");
                            Object obj = list.get(0);
                            Field pageField = ReflectHelper.getFieldByFieldName(obj, "page");
                            if (pageField != null) {
                                page = (Pager) ReflectHelper.getValueByFieldName(obj, "page");
                                if (page == null) {
                                    page = new Pager();
                                }
                                page.setTotal(count);
                                ReflectHelper.setValueByFileNameSup(obj, "page", page); // 通过反射，对实体对象设置分页对象
                            } else {
                                throw new NoSuchFieldException(obj.getClass().getName() + "不存�? page 属�?�！");
                            }
                            page.setTotal(count);
                            list.remove(0);
                            list.add(0, obj);
                        } else {
                            if(map.containsKey("page")){
                                page = (Pager) map.get("page");
                                page.setTotal(count);
                                map.remove("page");
                                map.put("page", page);
                            }else{
                                for(Object entry:map.values()){
                                    if(entry instanceof Pager){
                                        page = (Pager)entry;
                                        page.setTotal(count);
                                        break;
                                    }
                                }

                                if(null==page){
                                    throw new NoSuchFieldException(parameterObject.getClass().getName() + "不存�? page 属�?�！");
                                }
                            }
                        }
                    } else { // 参数为某个实体，该实体拥有Page属�??
                        Field pageField = ReflectHelper.getFieldByFieldName(parameterObject, "page");
                        if (pageField != null) {
                            page = (Pager) ReflectHelper.getValueByFieldName(parameterObject, "page");
                            if (page == null) {
                                page = new Pager();
                            }
                            page.setTotal(count);
                            ReflectHelper.setValueByFieldName(parameterObject, "page", page); // 通过反射，对实体对象设置分页对象
                        } else {

                            throw new NoSuchFieldException(parameterObject.getClass().getName() + "不存�? page 属�?�！");
                        }
                    }
                    String pageSql = generatePageSql(sql, page);
                    ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); // 将分页sql语句反射回BoundSql.
                }
            }
        }
        return ivk.proceed();
    }

    /**
     * 对SQL参数(?)设�??,参�?�org.apache.ibatis.executor.parameter.DefaultParameterHandler
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    @SuppressWarnings("unchecked")
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                        }
                    } else {

                        value = metaObject == null ? null : metaObject.getValue(propertyName);

                    }
                    @SuppressWarnings("rawtypes")
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }

    /**
     * 根据数据库方�?，生成特定的分页sql
     *
     * @param sql
     * @param page
     * @return
     */
    @SuppressWarnings("rawtypes")
    private String generatePageSql(String sql, Pager page) {
        if (page != null && notEmpty(dialect)) {
            StringBuffer pageSql = new StringBuffer();
            if ("Mysql".equals(dialect)) {
                pageSql.append(sql);
                pageSql.append(" limit " + page.getCurrentResult() + "," + page.getSize());
            } else if ("oracle".equals(dialect)) {
                pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
                pageSql.append(sql);
                pageSql.append(")tmp_tb where ROWNUM<=");
                pageSql.append(page.getCurrentResult() + page.getSize());
                pageSql.append(") where row_id>");
                pageSql.append(page.getCurrentResult());
            }
            return pageSql.toString();
        } else {
            return sql;
        }
    }

    public void setProperties(Properties p) {
        dialect = p.getProperty(DIALECT_STR);
        if (isEmpty(dialect)) {
            try {
                throw new PropertyException("dialect property is not found!");
            } catch (PropertyException e) {
                e.printStackTrace();
            }
        }
        pageSqlId = p.getProperty("pageSqlId");
        if (isEmpty(pageSqlId)) {
            try {
                throw new PropertyException("pageSqlId property is not found!");
            } catch (PropertyException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean notEmpty(String s) {
        return s != null && !"".equals(s) && !"null".equals(s);
    }

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s) || "null".equals(s);
    }

    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }
}
