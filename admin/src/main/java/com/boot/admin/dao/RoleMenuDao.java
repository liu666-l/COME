package com.boot.admin.dao;

import com.boot.admin.pojo.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleMenuDao {
    RoleMenu get(Long id);

    List<RoleMenu> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(RoleMenu roleMenu);

    int update(RoleMenu roleMenu);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<Long> listMenuIdByRoleId(Long roleId);

    int removeByRoleId(Long roleId);

    int removeByMenuId(Long menuId);

    int batchSave(List<RoleMenu> list);
}
