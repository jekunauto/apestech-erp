package com.apestech.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * 功能：druid 配置
 *
 * @author xul
 * @create 2017-11-18 10:04
 */
@Configuration
public class DruidConfiguration {

    @Value("${spring.datasource.druid.stat-view-servlet.url-pattern}")
    private String urlPattern;

    @Value("${spring.datasource.druid.stat-view-servlet.login-username}")
    private String loginUsername;

    @Value("${spring.datasource.druid.stat-view-servlet.login-password}")
    private String loginPassword;

    @Value("${spring.datasource.druid.stat-view-servlet.reset-enable}")
    private String resetEnable;

    @Value("${spring.datasource.druid.stat-view-servlet.allow}")
    private String allow;

    @Value("${spring.datasource.druid.stat-view-servlet.deny}")
    private String deny;

    @Value("${spring.datasource.druid.web-stat-filter.exclusions}")
    private String exclusions;

    /**
     * 注册一个StatViewServlet
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean DruidStatViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), urlPattern);

        //添加初始化参数：initParams
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", loginUsername);
        servletRegistrationBean.addInitParameter("loginPassword", loginPassword);
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", resetEnable);
        //白名单：
        servletRegistrationBean.addInitParameter("allow", allow);
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        servletRegistrationBean.addInitParameter("deny", deny);
        return servletRegistrationBean;
    }

    /**
     * 注册一个：filterRegistrationBean
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");

        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", exclusions);
        return filterRegistrationBean;
    }

}

