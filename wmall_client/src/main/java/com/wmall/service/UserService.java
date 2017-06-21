package com.wmall.service;

import com.wmall.bean.User;
import com.wmall.qo.UserQo;
import com.wmall.vo.Pager;
import com.wmall.vo.ReturnDO;

import java.util.List;
import java.util.Map;

/**
 * Created by asus-pc on 2017/6/16.
 */
public interface UserService {

    ReturnDO<Pager<User>> userPageData(UserQo userQo);

    List<User> getUser(Map<String,Object> map);
}
