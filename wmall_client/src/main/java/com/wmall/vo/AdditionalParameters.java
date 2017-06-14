package com.wmall.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by asus-pc on 2017/5/27.
 */
public class AdditionalParameters implements java.io.Serializable {

    private String id;

    private Integer level;

    private String name;

    /**
     * 子节点列表
     */
    private List<Item> children = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<Item> getChildren() {
        return children;
    }

    public void setChildren(List<Item> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
