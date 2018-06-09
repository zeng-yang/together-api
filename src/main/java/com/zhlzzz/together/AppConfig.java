package com.zhlzzz.together;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhlzzz.together.controllers.ApiAuthenticationMethodArgumentResolver;
import com.zhlzzz.together.controllers.Slices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AppConfig {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public static class WebConfigurer implements WebMvcConfigurer {

        private final ApplicationContext context;
        private final ObjectMapper objectMapper;

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new ApiAuthenticationMethodArgumentResolver());
            resolvers.add(new Slices.SliceIndicatorsMethodArgumentResolver(context));
        }

        @Override
        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            converters.add(0, new Slices.SliceHttpMessageConverter(objectMapper));
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            //设置允许跨域的路径
            registry.addMapping("/**")
                    //设置允许跨域请求的域名
                    .allowedOrigins("*")
                    //是否允许证书 不再默认开启
                    .allowCredentials(true)
                    //设置允许的方法
                    .allowedMethods("*")
                    //跨域允许时间
                    .maxAge(3600);
        }
    }

}
