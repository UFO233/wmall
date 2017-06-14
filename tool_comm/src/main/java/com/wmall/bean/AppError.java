package com.wmall.bean;

import com.wmall.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivenhf on 14-9-1.
 */
public class AppError {
    // 错误码
    private String code;
    /**
     * 错误信息 *
     */
    private String msg;

    public AppError(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        Map map = new HashMap();
        map.put("success", false);
        map.put("code", code);
        map.put("msg", msg);
        return JsonUtil.toJson(map);
    }
}
