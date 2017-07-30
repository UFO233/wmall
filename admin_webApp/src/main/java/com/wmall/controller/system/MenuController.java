package com.wmall.controller.system;

import com.wmall.bean.AppError;
import com.wmall.bean.Menu;
import com.wmall.controller.BaseController;
import com.wmall.qo.MenuQo;
import com.wmall.service.MenuService;
import com.wmall.util.StringUtil;
import com.wmall.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus-pc on 2017/5/24.
 */
@Controller
@RequestMapping("menu")
public class MenuController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @RequestMapping("home")
    public String getMenus(ModelMap map1){
        List<Tree<Menu>> menuList = menuService.menuTree();
        map1.put("menuList", menuList);
        return "home";
    }

    /**
     * 获取菜单列表
     */
    @RequestMapping("menuPage")
    public String menuList(){

        return "system/menuList";
    }
    /**
     * 生成aceTree
     */
    @RequestMapping("getTreeData")
    @ResponseBody
    public String jsonTree(String pId){
        List<Item> items = menuService.aceTree(pId);
        return toObjJson(items);
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "menuList")
    @ResponseBody
    public String getData(HttpServletRequest request,String parentId){
        MenuQo menuQo = new MenuQo();
        if(StringUtil.isEmpty(parentId)){
            parentId = "1";
        }
        Pager<Menu> pager = getPage(request);
        menuQo.setpId(parentId);
        menuQo.setPage(pager);
        ReturnDO<Pager<Menu>> returnDO = menuService.listPage(menuQo);
        if(returnDO.isSuccess()){
            return toPageJson(returnDO.getObj());
        }else{
            logger.error("菜单列表数据获取失败");
            return new AppError(returnDO.getCoder(), returnDO.getErrorMsg()).toString();
        }
    }

    /**
     * 菜单增加功能
     */
    @RequestMapping("addMenu")
    public String addMenu(){

        return "system/addMenu";
    }

    @RequestMapping("doAddMenu")
    @ResponseBody
    public String add(Menu menu){
        Map<String,Object> maps = new HashMap<>();
        maps.put("name",menu.getName());

        ReturnDO<Integer> rdo = menuService.addMenu(menu);
        if(rdo.isSuccess()){
            return toObjJson("数据新增成功");
        }else{
            return new AppError(rdo.getCoder(),rdo.getErrorMsg()).toString();
        }
    }
}
