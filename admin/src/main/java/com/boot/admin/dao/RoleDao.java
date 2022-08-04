package com.boot.admin.dao;

import com.boot.admin.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleDao {
    Role get(Long roleId);

    List<Role> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(Role role);

    int update(Role role);

    int remove(Long roleId);

    int batchRemove(Long[] roleIds);

    List<Long> roleIdsByUserId(Long userId);
}
