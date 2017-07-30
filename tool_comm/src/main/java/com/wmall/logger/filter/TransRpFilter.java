package com.wmall.logger.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.wmall.util.JsonUtil;
import com.wmall.util.StringUtil;
import org.apache.log4j.Logger;

/**
 * 服务接口 返回日志系统默认记录
 *
 * @author ivenhf
 * @version 1.0.0
 */
@Activate(group = Constants.PROVIDER, value = "transRp")
public class TransRpFilter implements Filter {

    private Logger logger = Logger.getLogger(TransRpFilter.class);

    public Result invoke(Invoker<?> invoker, Invocation invocation)
            throws RpcException {
        String transNo = invocation.getAttachment("transNo");
        if (StringUtil.isEmpty(transNo)) {
            transNo = "NULL";
        }
        Class<?> serviceType = invoker.getInterface();
        Result result = invoker.invoke(invocation);
        Object[] param = invocation.getArguments();
        logger.info("transRp filter [ " + transNo + " ]invoke remote service "
                + serviceType + " method " + invocation.getMethodName()
                + "() from consumer " + RpcContext.getContext().getRemoteHost()
                + " to provider " + RpcContext.getContext().getLocalHost()
                + "\nresult:" + JsonUtil.toJson(result.getValue()));
        return result;
    }
}
