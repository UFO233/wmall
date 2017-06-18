package com.wmall.qo;

import com.wmall.vo.Pager;

import java.io.Serializable;

/**
 * Created by asus-pc on 2017/6/16.
 */
public class UserQo implements Serializable {

    private Pager page;

    private String name;

    private String mobile;

    public Pager getPage() {
        return page;
    }

    public void setPage(Pager page) {
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
