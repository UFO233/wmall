package com.wmall.mapper;

import com.wmall.bean.Menu;
import com.wmall.qo.MenuQo;

import java.util.List;
import java.util.Map;

/**
 * Created by asus-pc on 2017/5/20.
 */
public interface MenuDao {

    List<Menu> getMenus(String pId);

    int countNum(Map<String,Object> map);

    List<Menu> menuListPage(MenuQo menuQo);

    int insertSelective(Menu menu);

    int updateByPrimaryKeySelective(Menu menu);
}
