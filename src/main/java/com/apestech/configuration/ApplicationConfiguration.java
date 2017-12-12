package com.apestech.configuration;

import com.apestech.oap.RopServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 功能：druid 配置
 *
 * @author xul
 * @create 2017-11-18 10:04
 */
@Configuration
@ImportResource(locations={"classpath:applicationContext.xml"})
public class ApplicationConfiguration {

    @Value("${spring.oap.url-pattern}")
    private String urlPattern;

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new RopServlet(), urlPattern);// ServletName默认值为首字母小写，即ropServlet
    }
}
