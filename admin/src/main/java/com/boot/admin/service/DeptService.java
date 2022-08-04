package com.boot.admin.service;


import com.boot.admin.pojo.Dept;
import com.boot.admin.pojo.Tree;


import java.util.List;
import java.util.Map;

/**
 * 部门管理
 */
public interface DeptService {
	
	Dept get(Long deptId);
	
	List<Dept> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(Dept sysDept);
	
	int update(Dept sysDept);
	
	int remove(Long deptId);
	
	int batchRemove(Long[] deptIds);

	Tree<Dept> getTree();
	
	boolean checkDeptHasUser(Long deptId);
}
