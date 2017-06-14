package com.wmall.vo;

import com.alibaba.fastjson.JSONObject;
import com.wmall.constant.CoderConstant;

import java.io.Serializable;

/**
 * 封装返回的结果
 *
 * @author iven
 * @version 2.0
 *          重新定义框架的服务返回对象，修改isOk　boolean 类型为　字符串状态编码
 *          000000 为正常；　　　　100000 类为业务状态异常　200000 为逻辑异常　300000　为数据库异常
 *          其他错误见对应的错误状态编码表；或异常定义;
 */
public class ReturnDO<T> implements Serializable {

    private static final long serialVersionUID = -4872654646406273633L;
    /**
     * 正确的　代码　*
     */
    private String coder = CoderConstant.SUCCESS;
    private T obj;
    private String errorMsg;

    private String transNo;
    private String sign;

    public T getObj() {
        return obj;
    }

    public ReturnDO<T> setObj(T obj) {
        this.obj = obj;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getCoder() {
        return coder;
    }

    public ReturnDO<T> setErrorMsg(String coder, String errorMsg) {
        this.coder = coder;
        this.errorMsg = errorMsg;
        return this;

    }

    /**
     * 根据状态好判断 服务调用是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return CoderConstant.SUCCESS.equals(coder);
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * 已Json 格式化字符串
     *
     * @return
     */
    @Override
    public String toString() {
        JSONObject toJsonString = new JSONObject();
        toJsonString.put("coder", coder);
        toJsonString.put("message", errorMsg);
        toJsonString.put("data", obj);
        return toJsonString.toString();
    }

}
