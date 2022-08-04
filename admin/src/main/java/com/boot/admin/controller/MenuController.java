package com.boot.admin.controller;


import com.boot.admin.pojo.Menu;
import com.boot.admin.pojo.Tree;
import com.boot.admin.service.MenuService;
import com.boot.admin.utils.SecuityUtils;
import com.boot.common.annctation.Log;
import com.boot.common.context.FilterContextHandler;
import com.boot.common.dto.MenuDTO;
import com.boot.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequestMapping("/menu")
@RestController()
public class MenuController {
    @Autowired
    MenuService menuService;

    @Log("获取当前用户的菜单")
    @GetMapping("currentUserMenus")
    R currentUserMenus() {
        return R.ok().put("currentUserMenus",menuService.RouterDTOsByUserId(SecuityUtils.getCurrentUser().getId()));
    }

    @Log("访问菜单")
    @GetMapping("tree")
    Tree<Menu> tree() {
        return menuService.getTree();
    }

    @GetMapping
    List<Tree<Menu>> list() {
        return menuService.getTree().getChildren();
    }

    @GetMapping("{id}")
    Menu get(@PathVariable("id") Long id) {
        Menu menu = menuService.get(id);
        return menu;
    }

    @GetMapping("list")
    List<Menu> list(@RequestParam Map<String, Object> params) {
        List<Menu> menus = menuService.list(params);
        return menus;
    }

    @PutMapping()
    R update(@RequestBody Menu menuDO) {
        if (menuService.update(menuDO) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @PostMapping
    R save(@RequestBody Menu menuDO) {
        return R.operate(menuService.save(menuDO) > 0);
    }

    @DeleteMapping()
    R remove(Long id) {
        if (menuService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @GetMapping("userMenus")
    List<MenuDTO> userMenus() {
        List<Menu> menuDOS = menuService.userMenus(Long.parseLong(FilterContextHandler.getUserID()));
        List<MenuDTO> menuDTOS = new ArrayList<>();
        for (Menu menuDO : menuDOS) {
            MenuDTO menuDTO = new MenuDTO();
            menuDTO.setMenuId(menuDO.getMenuId());
            menuDTO.setUrl(menuDO.getUrl());
            menuDTO.setPerms(menuDO.getPerms());
            menuDTOS.add(menuDTO);
        }
        return menuDTOS;
    }

    @GetMapping("clearCache")
    R clearCache() {
        Boolean flag = menuService.clearCache(Long.parseLong(FilterContextHandler.getUserID()));
        if (flag) {
            return R.ok();
        }
        return R.error();
    }

//    /**
//     * 当前用户菜单的树形结构
//     *
//     * @return
//     */
//    @RequestMapping("/currentUserMenus")
//    List<Tree<Menu>> currentUserMenus() {
//        List<Tree<Menu>> menus = menuService.listMenuTree(Long.parseLong(FilterContextHandler.getUserID()));
//        return menus;
//    }

    @GetMapping("/roleId")
    List<Long> menuIdsByRoleId(Long roleId) {
        return menuService.MenuIdsByRoleId(roleId);
    }
}
