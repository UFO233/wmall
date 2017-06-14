package com.wmall.controller;

import com.wmall.util.JsonUtil;
import com.wmall.vo.Pager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivenhf on 14-9-1.
 * <p/>
 */
public class BaseController {
    private Logger logger = Logger.getLogger(BaseController.class);
    public SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Pager getPage(HttpServletRequest request) {
        Pager queryPage = new Pager();
        try {
            String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
            String rows = request.getParameter("rows"); // 取得每页显示行数，,注意这是jqgrid自身的参数

            queryPage.setCurrentPage(Integer.parseInt(page));
            queryPage.setSize(Integer.parseInt(rows));
        } catch (Exception e) {
            logger.error("获取请求的翻页对象失败", e);
        }
        return queryPage;
    }

    public String toPageJson(Pager pager) {
        Map resultMap = new HashMap();
        resultMap.put("total", pager.getTotalPage());
        resultMap.put("page", pager.getCurrentPage());
        resultMap.put("records", pager.getTotal());
        resultMap.put("rows", pager.getResult());
        resultMap.put("success", true);
        return JsonUtil.toJson(resultMap);
    }

    public String toObjJson(Object obj) {
        Map resultMap = new HashMap();
        resultMap.put("data", obj);
        resultMap.put("success", true);
        return JsonUtil.toJson(resultMap);
    }
}
