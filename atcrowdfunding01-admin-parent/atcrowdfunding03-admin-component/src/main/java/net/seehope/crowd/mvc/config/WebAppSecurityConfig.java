/*
package net.seehope.crowd.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

*/
/**
 * @author JoinYang
 * @date 2022/5/7 20:24
 *//*

@Configuration
@EnableWebSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("lamb")
                .password("123456")
                .roles("tom");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/to/login/page.html","/admin/security/do/login.html",
                        "/bootstrap/**","/css/**","/fonts/**","/img/**",
                        "/jquery/**","/layer/**","/script/**","/ztree/**")
                .permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/admin/to/login/page.html")                 // 默认登录页面表单
                .loginProcessingUrl("/admin/security/do/login.html")    // 默认登录页面表单提交地址
                .defaultSuccessUrl("/admin/to/main/page.html")          // 登录验证成功后跳转的地址


                .and()
                .logout()
                .logoutUrl("/admin/security/do/lagout.html")            // 退出登录处理地址
                .logoutSuccessUrl("/admin/to/login/page.html")          // 退出登录时跳转的地址

                .and()
                .csrf()
                .disable();
    }
}
*/
