package com.boot.admin.dao;

import com.boot.admin.pojo.Dept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeptDao {
    Dept get(Long deptId);

    List<Dept> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(Dept dept);

    int update(Dept dept);

    int remove(Long deptId);

    int batchRemove(Long[] deptIds);

    Long[] listParentDept();

    int getDeptUserNumber(Long deptId);

}
