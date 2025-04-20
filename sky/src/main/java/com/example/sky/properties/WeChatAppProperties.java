package com.example.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sky.wechat.app")
@Component
@Data
public class WeChatAppProperties {
    private String appid;
    private String secret;
}
