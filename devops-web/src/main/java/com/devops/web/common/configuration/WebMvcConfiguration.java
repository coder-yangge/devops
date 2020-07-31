package com.devops.web.common.configuration;

import com.devops.web.common.interceptor.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yangge
 * @version 1.0.0
 * @title: WebMvcConfiguration
 * @description: web配置
 * @date 2020/4/16 10:13
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    public static final String SYSTEM_API = "/**";

    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns(SYSTEM_API)
                .excludePathPatterns("/page/login/login.html")
                .excludePathPatterns("/account/login")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/index.html")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/**/*.js", "/**/*.css","/**/*.ttf");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       /* registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/js/*")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/css/*")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/images/*")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/json/*")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/page/*")
                .addResourceLocations("classpath:/static/");*/
        registry.addResourceHandler("index.html")
                .addResourceLocations(new String[]{"classpath:/static/", "classpath:/resources/static/", "classpath:"});
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


}
