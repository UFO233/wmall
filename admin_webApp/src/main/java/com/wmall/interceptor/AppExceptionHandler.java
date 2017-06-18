package com.wmall.interceptor;

import com.wmall.bean.AppError;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ivenhf on 14-9-12.
 */
public class AppExceptionHandler implements HandlerExceptionResolver {

    private Logger logger = Logger.getLogger(AppExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object object, Exception exception) {

        logger.error("控制器层出现异常", exception);
        if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request
                .getHeader("X-Requested-With") != null && request
                .getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
            return new ModelAndView("comm/error");
        } else {// JSON格式返回
            try {
                PrintWriter writer = response.getWriter();
                response.setCharacterEncoding("UTF-8");
                // 返回客户端系统异常
                writer.write(new AppError("00001", "系统或网络异常,请稍候重试").toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
    }
}
