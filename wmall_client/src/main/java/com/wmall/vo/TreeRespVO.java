package com.wmall.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus-pc on 2017/6/6.
 */
public class TreeRespVO implements java.io.Serializable {

    private List<Item>  data = new ArrayList<Item>();

    public List<Item> getData()
    {
        return data ;
    }

    public void setData(List<Item> data )
    {
        this .data = data;
    }
}
