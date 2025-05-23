package com.example.galaxy.configurer;

import com.example.galaxy.security.filter.SecurOncePerRequestFilter;
import com.example.galaxy.security.filter.SecurUsernamePasswordAuthenticationFilter;
import com.example.galaxy.security.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("authUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurOncePerRequestFilter securOncePerRequestFilter;
    @Autowired
    private SecurAuthenticationEntryPoint securAuthenticationEntryPoint;
    @Autowired
    private SecurAccessDeniedHandler securAccessDeniedHandler;

    //登录成功处理器
    @Autowired
    private SecurAuthenticationSuccessHandler securAuthenticationSuccessHandler;
    @Autowired
    private SecurAuthenticationFailureHandler securAuthenticationFailureHandler;

    //退出处理器
    @Autowired
    private SecurLogoutHandler securLogoutHandler;
    @Autowired
    private SecurLogoutSuccessHandler securLogoutSuccessHandler;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoderUtil;


    /**
     * 从容器中取出 AuthenticationManagerBuilder，执行方法里面的逻辑之后，放回容器
     *
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoderUtil);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //第1步：解决跨域问题。cors 预检请求放行,让Spring security 放行所有preflight request（cors 预检请求）
        http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
        //第2步：让Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl();

        //第3步：请求权限配置
        //放行注册API请求，其它任何请求都必须经过身份验证.
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,"/sys-user/register").permitAll()
                .antMatchers("/v2/api-docs", "/v2/feign-docs",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources","/swagger-resources/configuration/security",
                        "/swagger-ui.html", "/webjars/**").permitAll()
                .anyRequest().authenticated();

        //第4步：拦截账号、密码。覆盖 UsernamePasswordAuthenticationFilter过滤器
        http.addFilterAt(securUsernamePasswordAuthenticationFilter() , UsernamePasswordAuthenticationFilter.class);

        //第5步：拦截token，并检测。在 UsernamePasswordAuthenticationFilter 之前添加 JwtAuthenticationTokenFilter
        http.addFilterBefore(securOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);

        //第6步：处理异常情况：认证失败和权限不足
        http.exceptionHandling().authenticationEntryPoint(securAuthenticationEntryPoint).accessDeniedHandler(securAccessDeniedHandler);

        //第7步：登录,因为使用前端发送JSON方式进行登录，所以登录模式不设置也是可以的。
        http.formLogin();

        //第8步：退出
        http.logout().addLogoutHandler(securLogoutHandler).logoutSuccessHandler(securLogoutSuccessHandler);

    }
    /**
     * 手动注册账号、密码拦截器
     * @return
     * @throws Exception
     */
    @Bean
    SecurUsernamePasswordAuthenticationFilter securUsernamePasswordAuthenticationFilter() throws Exception {
        SecurUsernamePasswordAuthenticationFilter filter = new SecurUsernamePasswordAuthenticationFilter();
        //成功后处理
        filter.setAuthenticationSuccessHandler(securAuthenticationSuccessHandler);
        //失败后处理
        filter.setAuthenticationFailureHandler(securAuthenticationFailureHandler);

        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }
}
