package com.boot.admin.dao;

import com.boot.admin.pojo.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserRoleDao {
    UserRole get(Long id);

    List<UserRole> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(UserRole userRole);

    int update(UserRole userRole);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<Long> listRoleId(Long userId);

    int removeByUserId(Long userId);

    int batchSave(List<UserRole> list);

    int batchRemoveByUserId(Long[] ids);
}
