package com.boot.admin.service.impl;


import com.boot.admin.dao.TokenDao;
import com.boot.admin.pojo.Token;
import com.boot.admin.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    TokenDao tokenDao;
    //过期时间，单位s
    private final static int EXPIRE = 60*30;
    @Override
    public String createToken(Long userId) {
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime()+EXPIRE*1000);
        Token tokenDO = new Token();
        tokenDO.setUserId(userId);
        tokenDO.setUpdateTime(now);
        tokenDO.setToken(UUID.randomUUID().toString());
        tokenDO.setExpireTime(expireTime);
        if(tokenDao.getTokenByUserId(userId)!=null){
            tokenDao.update(tokenDO);
        }else{
            tokenDao.save(tokenDO);
        }

        return tokenDO.getToken();
    }

    @Override
    public Long getUserIdByToken(String token) {
        Token token1= tokenDao.getToken(token);
        if (token==null){
            return -1L;
        }else {
            if (token1.getExpireTime().getTime()<System.currentTimeMillis()){
                tokenDao.removeToken(token);
                return -1L;
            }
            return token1.getUserId();
        }
    }

    @Override
    public boolean removeToken(String token) {
        return tokenDao.removeToken(token);
    }
}
