package com.wmall.logger.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.wmall.exception.ParamValidationException;
import com.wmall.util.JsonUtil;
import com.wmall.vo.ReturnDO;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;

/**
 * dubbo 服务 调用前过滤器，记录接口调用日志和流水号
 *
 * @author ivenhf
 */
@Activate(group = Constants.CONSUMER, value = "transNo")
public class TransNoFilter implements Filter {

    private Logger logger = Logger.getLogger(this.getClass());

    public Result invoke(Invoker<?> invoker, Invocation inv)
            throws RpcException {
        String transNo = getTransNo();
        // 在上下文中加入生成的 transNo 标示
        RpcContext.getContext().setAttachment("transNo", transNo);
        Class<?> serviceType = invoker.getInterface();
        Object[] args = inv.getArguments();
        StringBuffer param = new StringBuffer();
        String paramStr;
        String methodName = inv.getMethodName();

        try {
            for (Object obj : args) {
                // 不记录含 xml 内容的日志信息 twf 20140710
                if (null != obj) {
                    if (obj.toString().indexOf("<?xml version=\"1.0\" encoding=\"utf-8\"?>") == -1) {
                        param.append(JsonUtil.toJson(obj) + ",");
                    }
                } else {
                    param.append("null,");
                }
            }
        } catch (Exception e) {
            logger.error("保存服务调用日志失败", e);
        }

        logger.info("transNo filter [ " + transNo + " ]invoke remote service "
                + serviceType + " method " + methodName
                + "() from consumer " + RpcContext.getContext().getLocalHost()
                + " to provider " + RpcContext.getContext().getRemoteHost()
                + "\nparams:" + param.toString());

        Result result = null;
        try {
            result = invoker.invoke(inv);
        } catch (RpcException e) {
            Throwable throwable = e.getCause();
            if (throwable instanceof ParamValidationException) {
                // 只针对参数异常进行处理
                final String errormag = e.getMessage();
                result = new Result() {

                    @Override
                    public Object getValue() {
                        ReturnDO<Object> returnDO = new ReturnDO<>();
                        returnDO.setErrorMsg("100001", errormag);
                        return returnDO;
                    }

                    @Override
                    public Throwable getException() {
                        return null;
                    }

                    @Override
                    public boolean hasException() {
                        return false;
                    }

                    @Override
                    public Object recreate() throws Throwable {
                        ReturnDO<Object> returnDO = new ReturnDO<>();
                        returnDO.setErrorMsg("100001", errormag);
                        return returnDO;
                    }

                    @Override
                    public Object getResult() {
                        ReturnDO<Object> returnDO = new ReturnDO<>();
                        returnDO.setErrorMsg("100001", errormag);
                        return returnDO;
                    }

                    @Override
                    public Map<String, String> getAttachments() {
                        return null;
                    }

                    @Override
                    public String getAttachment(String key) {
                        return null;
                    }

                    @Override
                    public String getAttachment(String key, String defaultValue) {
                        return null;
                    }
                };
            } else {
                throw e;
            }
        }
        return result;
    }

    private String getTransNo() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("TN");
        stringBuffer.append(new Date().getTime());
        Double random = Math.random() * 10000;
        stringBuffer.append(random.longValue());
        return stringBuffer.toString();
    }
}
