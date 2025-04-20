package com.example.sky.config;

import com.example.sky.interceptor.EmployeeInterceptor;
import com.example.sky.interceptor.UserInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private EmployeeInterceptor employeeInterceptor;

    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加管理端拦截器
        registry.addInterceptor(employeeInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login")
                .excludePathPatterns("/admin/employee/test2");

        // 添加用户端拦截器
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status");
        log.info("拦截器就位");
    }

    /**
     * 拓展MVC的消息转换器
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("开始扩展消息转换器...");
        // 创建自定义的ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // 创建 java 8 的时间支持模块
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 设置全局时间格式
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        javaTimeModule.addSerializer(java.time.LocalDateTime.class, new LocalDateTimeSerializer(pattern));
        // 注册 javaTimeModule，并经用时间戳格式
        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        // 创建一个消息转化器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        // 将转换器放入spring MVC框架的容器中
        converters.add(0, converter);
    }
}
