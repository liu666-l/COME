package com.boot.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class OauthwServiceConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    DataSource dataSource;
    @Override
    public void configure(ClientDetailsServiceConfigurer client) throws Exception {
        client.inMemory().withClient("app").scopes("app")
                .authorizedGrantTypes("password","authorization_code", "refresh_token")
                .secret(new BCryptPasswordEncoder().encode("123456"));
    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.tokenStore(jdbcTokenStore());
    }

    JdbcTokenStore jdbcTokenStore(){
        return new JdbcTokenStore(dataSource);
    }

//    @Bean
//    RedisTokenStore redisTokenStore(){
//        return new RedisTokenStore(connectionFactory);
//    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // ??????/oauth/token_key???????????????????????????
                .tokenKeyAccess("permitAll()")
                // ??????/oauth/check_token??????????????????????????????
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }


}
