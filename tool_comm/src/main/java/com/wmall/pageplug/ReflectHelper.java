package com.wmall.pageplug;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus-pc on 2017/6/8.
 * 反射工具�?
 */
public class ReflectHelper {
    /**
     * 获取obj对象的filedName的field
     */
    public static Field getFieldByFieldName(Object obj,String fieldName){

        for(Class<?> superClass = obj.getClass();superClass != Object.class; superClass = superClass.getSuperclass()){
            try {
                //返回�?�? Field 对象，该对象反映�? Class 对象�?表示的类或接口的指定已声明字段（包括私有成员）�??
                return superClass.getDeclaredField(fieldName);
            }catch (NoSuchFieldException e){

            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性�??
     * @param obj
     * @param fieldName
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Object getValueByFieldName(Object obj,String fieldName)throws SecurityException,NoSuchFieldException,
            IllegalAccessException,IllegalArgumentException{
        Field field = getFieldByFieldName(obj,fieldName);
        Object value = null;
        if(field != null){
            //由于JDK的安全检查�?�时较多.�?以�?�过setAccessible(true)的方式关闭安全检查就可以达到提升反射速度的目�?
            if(field.isAccessible()){
                value = field.get(obj);
            }else{
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性�??
     *
     * @param obj
     * @param fieldName
     * @param value
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setValueByFieldName(Object obj,String fieldName,Object value)throws SecurityException,NoSuchFieldException,
            IllegalAccessException,IllegalArgumentException{
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (field.isAccessible()){
            field.set(obj,value);
        }else{
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }
    /**
     * 设置obj父类对象fieldName的属性�??
     *
     * @param obj
     * @param fieldName
     * @param value
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setValueByFileNameSup(Object obj,String fieldName,Object value)throws SecurityException,NoSuchFieldException,
            IllegalAccessException,IllegalArgumentException{
        Field field = obj.getClass().getDeclaredField(fieldName);
        if(field.isAccessible()){
            field.set(obj,value);
        }else{
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }
    public static Object invoke(Class runClass,String method,Object[] params)throws InstantiationException,IllegalAccessException,
            NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Object reObj = null;
        Object obj = runClass.newInstance();
        Method ms = runClass.getMethod(method, transformObjectParameter(params));
        ms.setAccessible(true);
        reObj = ms.invoke(obj,params);
        ms.setAccessible(false);
        return reObj;
    }
    public static Object getPropertyByProprtyName(Object object, String fieldName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        Method method = object.getClass().getMethod(getter);
        // 关闭安全�?查提高效�?
        method.setAccessible(true);
        Object value = method.invoke(object);
        method.setAccessible(false);
        return value;
    }

    public static Object setPropertyByProprtyName(Object object, String fieldName, Object[] params) {
        Object reObj = null;
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "set" + firstLetter + fieldName.substring(1);
            Method method = object.getClass().getMethod(getter, transformObjectParameter(params));
            // 关闭安全�?查提高效�?
            method.setAccessible(true);
            reObj = method.invoke(object, params);
            method.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reObj;
    }
    private static Class[] transformObjectParameter(Object[] m_gwParameter) {
        Class[] res = null;
        if((m_gwParameter == null) || (m_gwParameter.length < 1)){
            return res;
        }
        res = new Class[m_gwParameter.length];
        for(int i = 0; i < m_gwParameter.length; i++){
            if(Integer.class.isInstance(m_gwParameter[i])){
                res[i] = int.class;
            }else if (Byte.class.isInstance(m_gwParameter[i])) {
                res[i] = byte.class;
            } else if (Short.class.isInstance(m_gwParameter[i])) {
                res[i] = short.class;
            } else if (HashMap.class.isInstance(m_gwParameter[i])) {
                res[i] = Map.class;
            } else if (ArrayList.class.isInstance(m_gwParameter[i])) {
                res[i] = List.class;
            } else if (m_gwParameter[i] == null) {
                res[i] = null;
            } else {
                res[i] = m_gwParameter[i].getClass();
            }
        }
        return res;
    }
}
