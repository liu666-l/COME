package com.boot.admin.controller;


import com.boot.admin.dto.DTo.UserConvert;
import com.boot.admin.dto.UserDTO;
import com.boot.admin.pojo.User;
import com.boot.admin.service.RoleService;
import com.boot.admin.service.UserService;
import com.boot.admin.utils.MD5Utils;
import com.boot.admin.utils.SecuityUtils;
import com.boot.common.annctation.Log;
import com.boot.common.context.FilterContextHandler;
import com.boot.common.dto.LoginUserDTO;
import com.boot.common.utils.PageUtils;
import com.boot.common.utils.Query;
import com.boot.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * 用户信息
 */
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
	UserService userService;
    @Autowired
	RoleService roleService;

	/**
	 * 登录的当前用户，前台需要验证用户登录的页面可以调用此方法
	 * @return
	 */
    @GetMapping("/currentUser")
	LoginUserDTO currentUser(){
		LoginUserDTO loginUserDTO = new LoginUserDTO();
		loginUserDTO.setUserId(SecuityUtils.getCurrentUser().getId().toString());
		loginUserDTO.setUsername(FilterContextHandler.getUsername());
		loginUserDTO.setName(SecuityUtils.getCurrentUser().getName());
		return loginUserDTO;
	}

	/**
	 * 根据用户id获取用户
	 * @param id
	 * @return
	 */
    @GetMapping("{id}")
	R get(@PathVariable("id") Long id ){
		UserDTO userDTO = UserConvert.MAPPER.do2dto(userService.get(id));
    	return R.ok().put("data",userDTO);
	}

	/**
	 * 分页查询用户
	 * @param params
	 * @return
	 */
	@Log("获取用户列表")
    @GetMapping()
    R listByPage(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<UserDTO> userDTOS = UserConvert.MAPPER.dos2dtos((userService.list(query)));
        int total = userService.count(query);
        PageUtils pageUtil = new PageUtils(userDTOS, total);
        return R.ok().put("page",pageUtil);
    }

	/**
	 * 增加用户
	 * @param user
	 * @return
	 */
	@PostMapping()
    R save(@RequestBody User user) {
		user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
		return R.operate(userService.save(user) > 0);
	}

	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@PutMapping()
	R update(@RequestBody User user) {
		return R.operate(userService.update(user) > 0);
	}


	/**
	 * 删除用户
	 * @param id
	 * @return
	 */
	@DeleteMapping()
	R remove( Long id) {
		return R.operate (userService.remove(id) > 0);
	}

	@PostMapping("/batchRemove")
	@ResponseBody
	R batchRemove(@RequestParam("ids[]") Long[] userIds) {
		int r = userService.batchremove(userIds);
		if (r > 0) {
			return R.ok();
		}
		return R.error();
	}

	@PostMapping("/exits")
	@ResponseBody
	boolean exits(@RequestParam Map<String, Object> params) {
		// 存在，不通过，false
		return !userService.exits(params);
	}

	@GetMapping("/tokenUser")
	public Principal user(Principal user){
		return user;
	}
}
