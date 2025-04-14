package com.example.sky.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sky.jwt")
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtProperties {
    private String adminSecretKey;
    private String userSecretKey;
    private Long adminTimeout;
    private Long userTimeout;
    private String adminTokenName;
    private String userTokenName;
}
