package com.wmall.service.impl;

import com.wmall.bean.Menu;
import com.wmall.mapper.MenuDao;
import com.wmall.qo.MenuQo;
import com.wmall.vo.*;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import com.wmall.service.MenuService;

/**
 * Created by asus-pc on 2017/5/23.
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {

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
            int num = menuDao.countNum(child.getId());
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
    public TreeRespVO aceTree(String pId) {
        List<Menu> menuList = menuDao.getMenus(pId);
        TreeRespVO vo = new TreeRespVO();
        List<Item> voItemList = new ArrayList<>();
        if (null != menuList && menuList .size() !=0)
        {
            //Tree和数据库对应的实体bean对象
            for (Menu menu : menuList )
            {
                Item item = new Item();
                int child_count = menuDao.countNum(menu.getId());//根据遍历的节点，查询该节点子节点的个数。
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
                    item .setAdditionalParameters(adp );
                    item .setType("item" );//无子节点
                }
                voItemList .add(item );
            }
        }
        vo.setData( voItemList );
        return vo;
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
}
