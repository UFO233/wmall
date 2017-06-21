package com.wmall.service.impl;

import com.wmall.bean.User;
import com.wmall.mapper.UserDao;
import com.wmall.qo.UserQo;
import com.wmall.service.UserService;
import com.wmall.vo.Pager;
import com.wmall.vo.ReturnDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by asus-pc on 2017/6/16.
 */
@Service("userSvice")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public ReturnDO<Pager<User>> userPageData(UserQo userQo) {
        ReturnDO<Pager<User>> rdo = new ReturnDO<>();
        Pager pager = userQo.getPage();
        List<User> menuList = userDao.userListPage(userQo);
        pager.setResult(menuList);
        rdo.setObj(pager);
        return rdo;
    }

    @Override
    public List<User> getUser(Map<String, Object> map) {

        return userDao.getUser(map);
    }
}
