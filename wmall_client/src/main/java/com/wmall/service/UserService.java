package com.wmall.service;

import com.wmall.bean.User;
import com.wmall.qo.UserQo;
import com.wmall.vo.Pager;
import com.wmall.vo.ReturnDO;

/**
 * Created by asus-pc on 2017/6/16.
 */
public interface UserService {

    ReturnDO<Pager<User>> userPageData(UserQo userQo);
}
