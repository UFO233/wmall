package com.wmall.mapper;

import com.wmall.bean.User;
import com.wmall.qo.UserQo;

import java.util.List;

/**
 * Created by asus-pc on 2017/6/16.
 */
public interface UserDao {

    List<User> userListPage(UserQo userQo);
}
