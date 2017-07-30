package com.wmall.controller.system;

import com.wmall.bean.AppError;
import com.wmall.bean.User;
import com.wmall.comm.Constant;
import com.wmall.controller.BaseController;
import com.wmall.service.UserService;
import com.wmall.util.Md5Tool;
import com.wmall.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录
 * Created by asus-pc on 2017/6/20.
 */
@Controller
@RequestMapping("admin")
public class LoginController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("loginPage")
    public String login(){

        return "login";
    }

    /**
     * 验证登录
     */
    @RequestMapping("login")
    @ResponseBody
    public String loginVilid(HttpSession session,String loginName,String loginPwd){
        Map<String,Object> parms= new HashMap<>();
        try {
            if(StringUtil.isEmpty(loginName) || StringUtil.isEmpty(loginPwd)){
                return new AppError("200001","用户名或密码不能为空").toString();
            }else{
                parms.put("mobile",loginName);
                List<User> users = userService.getUser(parms);
                if(users == null || users.size() == 0){
                    return new AppError("200002","用户名或密码不正确").toString();
                }else{
                    String md5Pwd = Md5Tool.encrypt32(loginPwd);
                    if(!users.get(0).getPassword().equals(md5Pwd)){
                        return new AppError("200003","用户名或密码不正确").toString();
                    }
                }
                session.setAttribute(Constant.USER_KEY,users.get(0));
                return toObjJson("登录成功");
            }
        } catch (Exception e) {
            logger.error("登陆失败原因:", e);
            e.printStackTrace();
            return new AppError("999999","登陆失败，服务器网络异常").toString();
        }
    }

    /**
     * 系统退出,session清除
     * @param session
     * @return
     */
    @RequestMapping("logOut")
    public String logOut(HttpSession session){
        session.invalidate();
        return "redirect:/admin/loginPage.do";
    }
}
