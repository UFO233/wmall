package com.wmall.springMvc;

import com.wmall.vo.Pager;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * Created by LWW on 2015/4/27 --10:05.
 */
public class PageBinder implements WebBindingInitializer {


    @Override
    public void initBinder(WebDataBinder binder, WebRequest request) {
        binder.registerCustomEditor(Pager.class, new PageEditor(request.getParameter("page"),request.getParameter("rows")));
    }
}
