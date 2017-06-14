package com.wmall.mapper;

import com.wmall.bean.Menu;
import com.wmall.qo.MenuQo;

import java.util.List;

/**
 * Created by asus-pc on 2017/5/20.
 */
public interface MenuDao {

    List<Menu> getMenus(String pId);

    int countNum(String pId);

    List<Menu> menuListPage(MenuQo menuQo);
}
