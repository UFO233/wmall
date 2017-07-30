package com.wmall.logger.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.wmall.util.JsonUtil;
import com.wmall.util.StringUtil;
import org.apache.log4j.Logger;

/**
 * Created by ivenhf on 14-5-19.
 */
@Activate(group = Constants.CONSUMER, value = "transRe")
public class TransReFilter implements Filter {

    private Logger logger = Logger.getLogger(TransRpFilter.class);

    public Result invoke(Invoker<?> invoker, Invocation invocation)
            throws RpcException {

        String transNo = RpcContext.getContext().getAttachment("transNo");
        if (StringUtil.isEmpty(transNo)) {
            transNo = "NULL";
        }
        Class<?> serviceType = invoker.getInterface();
        Result result = invoker.invoke(invocation);

        logger.info("transRp filter [ " + transNo + " ]invoke remote service "
                + serviceType + " method " + invocation.getMethodName()
                + "() from consumer " + RpcContext.getContext().getRemoteHost()
                + " to provider " + RpcContext.getContext().getLocalHost()
                + "\nresult:" + JsonUtil.toJson(result.getValue()));
        return result;
    }
}
