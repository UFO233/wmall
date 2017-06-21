package com.wmall.mapper;

import com.wmall.bean.User;
import com.wmall.qo.UserQo;

import java.util.List;
import java.util.Map;

/**
 * Created by asus-pc on 2017/6/16.
 */
public interface UserDao {

    List<User> userListPage(UserQo userQo);

    List<User> getUser(Map<String,Object> map);
}
