package com.wmall.interceptor;

import com.wmall.bean.User;
import com.wmall.comm.Constant;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * Created by asus-pc on 2017/6/23.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = Logger.getLogger(LoginInterceptor.class);

    public boolean proHandle(HttpServletRequest request,HttpServletResponse response) throws Exception{
        logger.info("login check in start");
        Object object = request.getSession().getAttribute(Constant.USER_KEY);
        HttpSession session = request.getSession();
        final StringBuffer urlBuffer = request.getRequestURL();
        if (urlBuffer.indexOf("admin/login.do") > 1) {
            String sid = request.getParameter("sid");
            logger.info("--------------- p_sid：" + sid);
            if (null != sid) {
                Cookie cookie = new Cookie("sid", sid);
                response.addCookie(cookie);
                return true;
            }
        }
        boolean isLogin;
        if (object != null) {
            User accountCenter = (User) object; // 账户中心
            String userName = accountCenter.getName();
            String sid = request.getSession().getId();
            String cook_sid = getSid(request);
            logger.info("-------------------- sid：" + sid);
            logger.info("--------------- cook_sid：" + cook_sid);
            isLogin = true;
        } else {
            isLogin = false;
        }
        if (!isLogin) {
            if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request
                    .getHeader("X-Requested-With") != null && request
                    .getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
                String urlPath = urlBuffer.toString();
                urlPath = urlPath.substring(urlPath.indexOf("://") + 3);
//                urlPath = webHost + urlPath.substring(urlPath.indexOf("/"));
                logger.info("loadURL:" + urlPath);
                session.setAttribute("loadURL", urlPath);
                request.getRequestDispatcher("/WEB-INF/page/comm/timeout.jsp").forward(request, response);
            } else {
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("success", false);
                jsonObject.put("timeout", true);
                jsonObject.put("code", "99999");
                jsonObject.put("msg", "系统登录超时");
                logger.info("msg:系统登录超时");
                out.write(jsonObject.toString());
            }
            return false;
        }
        return true;
    }

    private String getSid(HttpServletRequest request) {
        String jsessionid = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sid")) {
                    jsessionid = cookie.getValue();
                    break;
                }
            }
        }
        return jsessionid;
    }
}
