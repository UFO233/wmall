package com.wmall.controller.system;

import com.wmall.bean.AppError;
import com.wmall.bean.User;
import com.wmall.controller.BaseController;
import com.wmall.qo.UserQo;
import com.wmall.service.UserService;
import com.wmall.vo.Pager;
import com.wmall.vo.ReturnDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by asus-pc on 2017/6/16.
 */
@Controller
@RequestMapping("sys/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("getUser")
    public String userPage(){

        return "system/user";
    }

    @RequestMapping("userData")
    @ResponseBody
    public String getUserData(HttpServletRequest request,String name,String mobile){
        UserQo userQo = new UserQo();
        userQo.setName(name);
        userQo.setMobile(mobile);
        userQo.setPage(getPage(request));
        ReturnDO<Pager<User>> rdo = userService.userPageData(userQo);
        if(rdo.isSuccess()){
            return toPageJson(rdo.getObj());
        }else{
            return new AppError(rdo.getCoder(),rdo.getErrorMsg()).toString();
        }
    }
}
