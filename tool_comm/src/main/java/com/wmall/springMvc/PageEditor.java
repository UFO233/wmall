package com.wmall.springMvc;

import com.wmall.vo.Pager;

import java.beans.PropertyEditorSupport;

/**
 * Created by LWW on 2015/4/27 --10:09.
 */
public class PageEditor extends PropertyEditorSupport {
    private String  page;

    private String rows;


    /**
     *
     * @param page 页数
     * @param rows 每页列表
     */
    public PageEditor(String  page, String rows) {
        this.page = page;
        this.rows = rows;
    }



    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        Pager queryPage = new Pager();
        try {
            queryPage.setCurrentPage(Integer.parseInt(page));
            queryPage.setSize(Integer.parseInt(rows));
            setValue(queryPage);
        } catch (Exception ex) {
            throw new IllegalArgumentException("映射分页对象失败: " + ex.getMessage(), ex);
        }
    }

    /**
     * Format th Date as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        return "";
    }
}
