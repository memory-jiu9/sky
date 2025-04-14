package com.example.sky.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sky.alioss")
@Component
@Data
public class AliOSSProperties {
    private String endPoint;
    private String accessId;
    private String accessKey;
    private String bucketName;
}
