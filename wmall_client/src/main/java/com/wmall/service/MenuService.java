package com.wmall.service;

import com.wmall.bean.Menu;
import com.wmall.qo.MenuQo;
import com.wmall.vo.Item;
import com.wmall.vo.Pager;
import com.wmall.vo.ReturnDO;
import com.wmall.vo.Tree;

import java.util.List;

/**
 * Created by asus-pc on 2017/5/22.
 */
public interface MenuService {

    List<Tree<Menu>> menuTree();

    List<Item> aceTree(String pId);

    ReturnDO<Pager<Menu>> listPage(MenuQo menuQo);
}
