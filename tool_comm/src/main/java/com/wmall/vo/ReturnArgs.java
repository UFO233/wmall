package com.wmall.vo;

import java.io.Serializable;

/**
 * Created by iven on 2016/4/2.
 */
public class ReturnArgs implements Serializable {

    private static final long serialVersionUID = -8247903820990532882L;
    private String code;
    private String desc;

    public ReturnArgs() {
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
