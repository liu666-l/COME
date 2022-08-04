package com.boot.admin.service.impl;



import com.boot.admin.dao.DeptDao;
import com.boot.admin.dao.UserRoleDao;
import com.boot.admin.pojo.Dept;
import com.boot.admin.pojo.Tree;

import com.boot.admin.dao.UserDao;
import com.boot.admin.pojo.User;
import com.boot.admin.pojo.UserRole;
import com.boot.admin.service.UserService;
import com.boot.admin.utils.BuildTree;
import com.boot.admin.utils.MD5Utils;
import com.boot.admin.vo.UserVO;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;


@Transactional(rollbackFor = Exception.class)
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userMapper;
	@Autowired
	UserRoleDao userRoleMapper;
	@Autowired
	DeptDao deptDaoMapper;

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Override
	public User get(Long id) {
		List<Long> roleIds = userRoleMapper.listRoleId(id);
		User user = userMapper.get(id);
		user.setDeptName(deptDaoMapper.get(user.getDeptId()).getName());
		user.setroleIds(roleIds);
		return user;
	}

	@Override
	public List<User> list(Map<String, Object> map) {
		return userMapper.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return userMapper.count(map);
	}

	@Override
	public int save(User user) {
		int count = userMapper.save(user);
		Long userId = user.getUserId();
		List<Long> roles = user.getroleIds();
		remove(userId, roles);
		return count;
	}

	@Override
	public int update(User user) {
		int r = userMapper.update(user);
		Long userId = user.getUserId();
		List<Long> roles = user.getroleIds();
		if(null!=roles){
			remove(userId, roles);
		}
		return r;
	}

	private void remove(Long userId, List<Long> roles) {
		userRoleMapper.removeByUserId(userId);
		List<UserRole> list = new ArrayList<>();
		for (Long roleId : roles) {
			UserRole ur = new UserRole();
			ur.setUserId(userId);
			ur.setRoleId(roleId);
			list.add(ur);
		}
		if (list.size() > 0) {
			userRoleMapper.batchSave(list);
		}
	}

	@Override
	public int remove(Long userId) {
		userRoleMapper.removeByUserId(userId);
		return userMapper.remove(userId);
	}

	@Override
	public boolean exits(Map<String, Object> params) {
		boolean exits = userMapper.list(params).size() > 0;
		return exits;
	}

	@Override
	public Set<String> listRoles(Long userId) {
		return null;
	}

	@Override
	public int resetPwd(UserVO userVO, User user) throws Exception {
		if(Objects.equals(userVO.getUser().getUserId(),user.getUserId())){
			if(Objects.equals(MD5Utils.encrypt(user.getUsername(),userVO.getPwdOld()),user.getPassword())){
				user.setPassword(MD5Utils.encrypt(user.getUsername(),userVO.getPwdNew()));
				return userMapper.update(user);
			}else{
				throw new Exception("输入的旧密码有误！");
			}
		}else{
			throw new Exception("你修改的不是你登录的账号！");
		}
	}
	@Override
	public int adminResetPwd(UserVO userVO) throws Exception {
		User user =get(userVO.getUser().getUserId());
		if("admin".equals(user.getUsername())){
			throw new Exception("超级管理员的账号不允许直接重置！");
		}
		user.setPassword(MD5Utils.encrypt(user.getUsername(), userVO.getPwdNew()));
		return userMapper.update(user);


	}

	@Transactional
	@Override
	public int batchremove(Long[] userIds) {
		int count = userMapper.batchRemove(userIds);
		userRoleMapper.batchRemoveByUserId(userIds);
		return count;
	}

	@Override
	public Tree<Dept> getTree() {
		List<Tree<Dept>> trees = new ArrayList<Tree<Dept>>();
		List<Dept> deptDaos = deptDaoMapper.list(new HashMap<String, Object>(16));
		Long[] pDepts = deptDaoMapper.listParentDept();
		Long[] uDepts = userMapper.listAllDept();
		Long[] allDepts = (Long[]) ArrayUtils.addAll(pDepts, uDepts);
		for (Dept dept : deptDaos) {
			if (!ArrayUtils.contains(allDepts, dept.getDeptId())) {
				continue;
			}
			Tree<Dept> tree = new Tree<Dept>();
			tree.setId(dept.getDeptId().toString());
			tree.setParentId(dept.getParentId().toString());
			tree.setText(dept.getName());
			Map<String, Object> state = new HashMap<>(16);
			state.put("opened", true);
			state.put("mType", "dept");
			tree.setState(state);
			trees.add(tree);
		}
		List<User> users = userMapper.list(new HashMap<String, Object>(16));
		for (User user : users) {
			Tree<Dept> tree = new Tree<Dept>();
			tree.setId(user.getUserId().toString());
			tree.setParentId(user.getDeptId().toString());
			tree.setText(user.getName());
			Map<String, Object> state = new HashMap<>(16);
			state.put("opened", true);
			state.put("mType", "user");
			tree.setState(state);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<Dept> t = BuildTree.build(trees);
		return t;
	}

	@Override
	public int updatePersonal(User user) {
		return userMapper.update(user);
	}

	@Override
	public Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception {
		return null;
	}


}
