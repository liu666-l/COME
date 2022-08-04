package com.boot.admin.controller;


import com.boot.admin.service.MenuService;
import com.boot.admin.service.TokenService;
import com.boot.admin.service.UserService;
import com.boot.admin.utils.SecuityUtils;
import com.boot.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping()
@RestController
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    MenuService menuService;
    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @GetMapping("/router")
    R router() {
        return R.ok().put("router", menuService.RouterDTOsByUserId(SecuityUtils.getCurrentUser().getId()));
    }


    @RequestMapping("/logout")
    R logout(String token) {
        consumerTokenServices.revokeToken(token);
        return R.ok();
    }


}
