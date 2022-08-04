package com.boot.admin.dao;

import com.boot.admin.pojo.Token;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TokenDao {
    @Insert("insert into  sys_user_token (user_id,token,expire_time,update_time) values (#{userId},#{token},#{expireTime},#{updateTime})")
    int save(Token token);

    @Select("select * from sys_user_token where token =#{token}")
    Token getToken(String token);

    @Delete("delete from sys_user_token where token=#{token}")
    boolean removeToken(String token);

    @Select("select * from sys_user_token where user_id =#{userId}")
    Token getTokenByUserId(Long userId);

    @Update("update sys_user_token set token=#{token},expire_time=#{expireTime},update_time=#{updateTime} where user_id=#{userId}")
    int update(Token token);
}
