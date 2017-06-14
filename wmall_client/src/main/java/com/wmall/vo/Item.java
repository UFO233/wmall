package com.wmall.vo;

/**
 * Created by asus-pc on 2017/6/6.
 */
public class Item  implements java.io.Serializable{

    /**
     * 节点的名字
     */
    private String text ;

    /**
     * 节点的类型："item":文件  "folder":目录
     */
    private String type ;

    /**
     * 子节点的信息
     */
    private AdditionalParameters additionalParameters ;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AdditionalParameters getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameters(AdditionalParameters additionalParameters) {
        this.additionalParameters = additionalParameters;
    }
}
