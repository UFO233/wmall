package com.wmall.logger.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.wmall.exception.ParamValidationException;
import com.wmall.util.JsonUtil;
import com.wmall.util.StringUtil;
import com.wmall.vo.ReturnDO;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by ivenhf on 14-4-22.
 */
@Activate(group = Constants.PROVIDER, value = "transRq")
public class TransRqFilter implements Filter {

    private Logger logger = Logger.getLogger(TransRpFilter.class);

    public Result invoke(Invoker<?> invoker, Invocation invocation)
            throws RpcException {
        String transNo = invocation.getAttachment("transNo");
        if (StringUtil.isEmpty(transNo)) {
            transNo = "NULL";
        }
        Class<?> serviceType = invoker.getInterface();
        Object[] args = invocation.getArguments();
        StringBuffer param = new StringBuffer();
        String paramStr;
        String methodName = invocation.getMethodName();
        try {
            for (Object obj : args) {
                param.append(JsonUtil.toJson(obj) + ",");
            }
        } catch (Exception e) {
            logger.error("保存服务调用日志失败", e);
        }

        logger.info("transRq filter [ " + transNo + " ]invoke remote service "
                + serviceType + " method " + invocation.getMethodName()
                + "() from consumer " + RpcContext.getContext().getRemoteHost()
                + " to provider " + RpcContext.getContext().getLocalHost()
                + "\nparams:" + param.toString());

        Result result = null;
        try {
            result = invoker.invoke(invocation);
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
}
