package com.boot.admin.controller;


import com.boot.admin.pojo.Role;
import com.boot.admin.service.RoleService;
import com.boot.common.utils.PageUtils;
import com.boot.common.utils.Query;
import com.boot.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RequestMapping("/role")
@RestController
public class RoleController {
    @Autowired
    RoleService roleService;
    //权限访问角色
    @PreAuthorize("hasAuthority('admin:role:role')")
    @GetMapping()
    PageUtils list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<Role> roleS = roleService.list(query);
        int total = roleService.count(query);
        PageUtils pageUtil = new PageUtils(roleS, total);
        return pageUtil;
    }

    @GetMapping("/userId/{userId}")
    List<Long> roleIdByUserId(@PathVariable Long userId){
        return roleService.RoleIdsByUserId(userId);
    }

    @PostMapping
    R save(@RequestBody Role role){
        if(roleService.save(role)>0){
            return R.ok();
        }
        return R.error();
    }

    @PutMapping
    R update(@RequestBody Role role){
        if(roleService.update(role)>0){
            return R.ok();
        }
        return R.error();
    }

}
