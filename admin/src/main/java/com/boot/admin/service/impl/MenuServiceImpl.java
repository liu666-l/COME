package com.boot.admin.service.impl;


import com.boot.admin.dao.MenuDao;
import com.boot.admin.dao.RoleMenuDao;
import com.boot.admin.pojo.Menu;
import com.boot.admin.pojo.Tree;
import com.boot.admin.service.MenuService;
import com.boot.admin.utils.BuildTree;
import com.boot.common.dto.RouterDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {
    @Autowired
    MenuDao menuMapper;
    @Autowired
    RoleMenuDao roleMenuMapper;

    /**
     * @param
     * @return 树形菜单
     */
    @Cacheable
    @Override
    public Tree<Menu> getSysMenuTree(Long id) {
        List<Tree<Menu>> trees = new ArrayList<Tree<Menu>>();
        List<Menu> menuDOs = userMenus(id);
        for (Menu menuDO : menuDOs) {
            Tree<Menu> tree = new Tree<Menu>();
            tree.setId(menuDO.getMenuId().toString());
            tree.setParentId(menuDO.getParentId().toString());
            tree.setText(menuDO.getName());
            Map<String, Object> attributes = new HashMap<>(16);
            attributes.put("url", menuDO.getUrl());
            attributes.put("icon", menuDO.getIcon());
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<Menu> t = BuildTree.build(trees);
        return t;
    }

    @Cacheable(value = "permission", key = "#userId")
    @Override
    public List<Menu> userMenus(Long userId) {
        return menuMapper.listMenuByUserId(userId);
    }

    @Override
    @CacheEvict(value = "permission", key = "#userId")
    public boolean clearCache(Long userId) {
        return true;
    }

    @Override
    public List<Menu> list(Map<String, Object> params) {
        List<Menu> menus = menuMapper.list(params);
        return menus;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public int remove(Long id) {
        int result = menuMapper.remove(id);
        roleMenuMapper.removeByMenuId(id);
        return result;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public int save(Menu menu) {
        int r = menuMapper.save(menu);
        return r;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public int update(Menu menu) {
        int r = menuMapper.update(menu);
        return r;
    }

    @Override
    public Menu get(Long id) {
        Menu menuDO = menuMapper.get(id);
        return menuDO;
    }

    @Override
    public Tree<Menu> getTree() {
        List<Tree<Menu>> trees = new ArrayList<Tree<Menu>>();
        List<Menu> menuDOs = menuMapper.list(new HashMap<>(16));
        for (Menu menuDO : menuDOs) {
            Tree<Menu> tree = new Tree<Menu>();
            tree.setId(menuDO.getMenuId().toString());
            tree.setParentId(menuDO.getParentId().toString());
            tree.setText(menuDO.getName());
            tree.setObject(menuDO);
//			Map<String,Object> map =new HashMap<>(16);
//			map.put("url",menuDO.getUrl());
//			map.put("perms",menuDO.getPerms());
//
//			tree.setAttributes(map);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<Menu> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public Tree<Menu> getTree(Long id) {
        // 根据roleId查询权限
        List<Menu> menus = menuMapper.list(new HashMap<String, Object>(16));
        List<Long> menuIds = roleMenuMapper.listMenuIdByRoleId(id);
        List<Long> temp = menuIds;
        for (Menu menu : menus) {
            if (temp.contains(menu.getParentId())) {
                menuIds.remove(menu.getParentId());
            }
        }
        List<Tree<Menu>> trees = new ArrayList<Tree<Menu>>();
        List<Menu> menuDOs = menuMapper.list(new HashMap<String, Object>(16));
        for (Menu menuDO : menuDOs) {
            Tree<Menu> tree = new Tree<Menu>();
            tree.setId(menuDO.getMenuId().toString());
            tree.setParentId(menuDO.getParentId().toString());
            tree.setText(menuDO.getName());
            Map<String, Object> state = new HashMap<>(16);
            Long menuId = menuDO.getMenuId();
            if (menuIds.contains(menuId)) {
                state.put("selected", true);
            } else {
                state.put("selected", false);
            }
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<Menu> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public Set<String> listPerms(Long userId) {
        List<String> perms = menuMapper.listUserPerms(userId);
        return new HashSet<>(perms);
    }

    /**
     * 获取角色下的权限所有id
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Long> MenuIdsByRoleId(Long roleId) {
        // 根据roleId查询权限,只保留子节点，父节点的选中或半选中状态，前台自动调整
        List<Menu> menus = menuMapper.list(new HashMap<String, Object>(16));
        List<Long> menuIds = roleMenuMapper.listMenuIdByRoleId(roleId);
        List<Long> temp = menuIds;
        for (Menu menu : menus) {
            if (temp.contains(menu.getParentId())) {
                menuIds.remove(menu.getParentId());
            }
        }
        return menuIds;
    }

    /**
     * 用户的路由
     *
     * @return
     */
    @Override
    public List<RouterDTO> RouterDTOsByUserId(Long userId) {
        List<Menu> menuDOs = userMenus(userId);
        List<RouterDTO> routerDTOs = new ArrayList<>();
        for (Menu menuDO : menuDOs) {
            RouterDTO routerDTO = new RouterDTO();
            routerDTO.setId(menuDO.getMenuId());
            routerDTO.setParentId(menuDO.getParentId());
            routerDTO.setPath(menuDO.getUrl());
            routerDTO.setComponent(menuDO.getComponent());
            routerDTO.setName(menuDO.getName());
            routerDTO.setIconCls(menuDO.getIcon());
            routerDTO.setMenuShow(true);
            routerDTO.setChildren(new ArrayList<>());
            routerDTO.setLeaf(null!=menuDO.getType()&&menuDO.getType()==1);
            routerDTOs.add(routerDTO);
        }
        return RouterDTO.buildList(routerDTOs, 0L);
    }

    @Override
    public List<String> PermsByUserId(Long userId) {
        List<String> permsList = new ArrayList<>();
        List<Menu> menuDOs = userMenus(userId);
        for (Menu menuDO:menuDOs){
            if(menuDO.getPerms()!=null && ""!=menuDO.getPerms()){
                permsList.add(menuDO.getPerms());
            }
        }
        return permsList;
    }

    @Override
    public List<Tree<Menu>> listMenuTree(Long id) {
        List<Tree<Menu>> trees = new ArrayList<Tree<Menu>>();
        List<Menu> menuDOs = menuMapper.listMenuByUserId(id);
        for (Menu menuDO : menuDOs) {
            Tree<Menu> tree = new Tree<Menu>();
            tree.setId(menuDO.getMenuId().toString());
            tree.setParentId(menuDO.getParentId().toString());
            tree.setText(menuDO.getName());
            Map<String, Object> attributes = new HashMap<>(16);
            attributes.put("url", menuDO.getUrl());
            attributes.put("icon", menuDO.getIcon());
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        List<Tree<Menu>> list = BuildTree.buildList(trees, "0");
        return list;
    }

}
