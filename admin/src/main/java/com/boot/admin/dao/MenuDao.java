package com.boot.admin.dao;

import com.boot.admin.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MenuDao {

    Menu get(Long menuId);

    List<Menu> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(Menu menu);

    int update(Menu menu);

    int remove(Long menuId);

    int batchRemove(Long[] menuIds);

    List<Menu> listMenuByUserId(Long id);

    List<String> listUserPerms(Long id);
}
