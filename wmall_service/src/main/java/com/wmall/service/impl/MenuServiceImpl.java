package com.wmall.service.impl;

import com.wmall.bean.Menu;
import com.wmall.mapper.MenuDao;
import com.wmall.qo.MenuQo;
import com.wmall.vo.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wmall.service.MenuService;

/**
 * Created by asus-pc on 2017/5/23.
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {

    private static Logger logger = Logger.getLogger(MenuServiceImpl.class);

    @Resource
    private MenuDao menuDao;

    /**
     * 获取所有菜单列表
     * @return
     */
    @Override
    public List<Tree<Menu>> menuTree() {
        List<Tree<Menu>> nodeList = null;
        List<Menu> parMenus = menuDao.getMenus("1");
        nodeList = getMenuTree(parMenus);
        return nodeList;

    }

    private List<Tree<Menu>> getMenuTree(List<Menu> childMenus) {
        List<Tree<Menu>> menuTreeList = new ArrayList<>();
        for(Menu child : childMenus){
            Tree<Menu> tree = new Tree<>();
            tree.setNode(child);
            Map<String,Object> maps = new HashMap<>();
            maps.put("pId", child.getId());
            int num = menuDao.countNum(maps);
            if(num != 0){
                tree.setChilds(getMenuTree(menuDao.getMenus(child.getId())));//递归
            }
            menuTreeList.add(tree);
        }
        return menuTreeList;
    }
    /**
     * 获取菜单的根节点(异步获取树 Ace tree)
     */
    @Override
    public List<Item> aceTree(String pId) {
        List<Menu> menuList = menuDao.getMenus(pId);
        List<Item> voItemList = new ArrayList<>();
        if (null != menuList && menuList .size() !=0)
        {
            //Tree和数据库对应的实体bean对象
            for (Menu menu : menuList )
            {
                Item item = new Item();
                Map<String,Object> maps = new HashMap<>();
                maps.put("pId",menu.getId());
                int child_count = menuDao.countNum(maps);//根据遍历的节点，查询该节点子节点的个数。
                        item .setText(menu.getName());
                if (child_count > 0)
                {
                    item .setType("folder" );//有子节点
                    AdditionalParameters adp = new AdditionalParameters();
                    adp.setLevel(Integer.valueOf(menu.getLevel()));
                    adp .setId(menu .getId());
                    adp.setName(menu.getName());
                    item .setAdditionalParameters(adp );
                }
                else
                {
                    AdditionalParameters adp = new AdditionalParameters();
                    adp .setId(menu.getId());
                    adp.setLevel(Integer.valueOf(menu.getLevel()));
                    adp.setName(menu.getName());
                    item .setAdditionalParameters(adp );
                    item .setType("item" );//无子节点
                }
                voItemList .add(item );
            }
        }
        return voItemList;
    }

    @Override
    public ReturnDO<Pager<Menu>> listPage(MenuQo menuQo) {
        ReturnDO<Pager<Menu>> rdo = new ReturnDO<>();
        Pager pager = menuQo.getPage();
        List<Menu> menuList = menuDao.menuListPage(menuQo);
        pager.setResult(menuList);
        rdo.setObj(pager);
        return rdo;
    }

    @Override
    public ReturnDO<Integer> addMenu(Menu menu) {
        ReturnDO<Integer> rdo = new ReturnDO<>();
        try {
            int flag = menuDao.insertSelective(menu);
            if(flag > 0){
                rdo.setObj(1);
            }
        } catch (Exception e) {
            rdo.setErrorMsg("999999","菜单新增出现异常");
            logger.error("菜单新增出现异常", e);
            e.printStackTrace();
        }
        return rdo;
    }

    @Override
    public ReturnDO<Integer> editMenu(Menu menu) {
        ReturnDO<Integer> rdo = new ReturnDO<>();
        try {
            int flag = menuDao.updateByPrimaryKeySelective(menu);
            if(flag > 0){
                rdo.setObj(1);
            }
        } catch (Exception e) {
            rdo.setErrorMsg("999999","菜单修改出现异常");
            logger.error("菜单修改出现异常", e);
            e.printStackTrace();
        }
        return rdo;
    }

    @Override
    public ReturnDO<Integer> deleteMenu(Menu menu) {
        ReturnDO<Integer> rdo = new ReturnDO<>();
        try {
            int flag = menuDao.updateByPrimaryKeySelective(menu);
            if(flag > 0){
                rdo.setObj(1);
            }
        } catch (Exception e) {
            rdo.setErrorMsg("999999","菜单删除出现异常");
            logger.error("菜单删除出现异常", e);
            e.printStackTrace();
        }
        return rdo;
    }

    @Override
    public ReturnDO<Integer> checkNameOrUrl(Map<String, Object> map) {
        ReturnDO<Integer> rdo = new ReturnDO<>();
        int coutn = menuDao.countNum(map);
        rdo.setObj(coutn);
        return rdo;
    }
}
