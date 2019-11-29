package com.sherlocky.halo.web.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 个性化 MVC 配置
 * @author: zhangcx
 * @date: 2019/1/29 13:35
 */
@Configuration
public class HaloMvcConfig implements WebMvcConfigurer {
    @Resource(name="thymeleafViewResolver")
    private ThymeleafViewResolver thymeleafViewResolver;
    @Value("${sherlock.blog.baseUrl}")
    private String blogBaseUrl;


/*    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // view 相关配置在 application.properties 中，使用
        registry.addViewController("/").setViewName("/search");
    }*/

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        if (thymeleafViewResolver != null) {
            Map<String, Object> vars = new HashMap<>(8);
            vars.put("blogBaseUrl", blogBaseUrl);
            // 静态参数，只取一次值
            thymeleafViewResolver.setStaticVariables(vars);
        }
    }
}
