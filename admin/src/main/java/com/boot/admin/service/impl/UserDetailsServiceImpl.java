package com.boot.admin.service.impl;


import com.boot.admin.dao.UserDao;
import com.boot.admin.pojo.User;
import com.boot.admin.secuity.CurrentUser;
import com.boot.admin.service.MenuService;
import com.boot.common.exception.CDException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserDao userDao;
    @Autowired
    MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> userDOS = userDao.list(new HashMap<String, Object>() {{
            put("username", username);
        }});
        if (userDOS.size() < 1) {
            throw new CDException("用户名或密码错误！");
        }
        User user = userDOS.get(0);
        Set<String> perms = menuService.listPerms(user.getUserId());
        Set<GrantedAuthority> authorities = perms.stream().filter(Objects::nonNull).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        return new CurrentUser(username, user.getPassword(), user.getUserId(), user.getName(),authorities);
    }

}
