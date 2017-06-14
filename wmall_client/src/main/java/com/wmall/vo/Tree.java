package com.wmall.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus-pc on 2017/5/26.
 */
public class Tree<Node> implements Serializable {

    private static final long serialVersionUID = -9026941089661615421L;

    private Node node = null;
    private List<Tree<Node>> childs = new ArrayList<Tree<Node>>();

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public List<Tree<Node>> getChilds() {
        return childs;
    }

    public void setChilds(List<Tree<Node>> childs) {
        this.childs = childs;
    }
}
