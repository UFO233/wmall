package com.wmall.qo;

import com.wmall.vo.Pager;

import java.io.Serializable;

/**
 * Created by asus-pc on 2017/6/9.
 */
public class MenuQo implements Serializable {

    private Pager page;

    private String pId;

    public Pager getPage() {
        return page;
    }

    public void setPage(Pager page) {
        this.page = page;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
