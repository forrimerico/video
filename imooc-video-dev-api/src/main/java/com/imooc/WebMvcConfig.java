package com.imooc;

import com.imooc.interceptor.MiniInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:E:/dev/product/imoocvideodev/imooc_resources/")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    /**
     * 先把拦截器注册到bean里
     *
     * @return
     */
    @Bean
    public MiniInterceptor miniInterceptor()
    {
        return new MiniInterceptor();
    }

    /**
     * 再把拦截器注册到拦截器注册中心
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**");
        super.addInterceptors(registry);
    }
}
