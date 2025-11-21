package com.sky.challenge.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rsa")
public class RSAKeyProperties {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
}
