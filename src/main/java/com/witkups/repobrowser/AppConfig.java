package com.witkups.repobrowser;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig
{
    @Bean
    @ConfigurationProperties(prefix = "custom.rest.connection")
    public HttpComponentsClientHttpRequestFactory customHttpRequestFactory()
    {
        return new HttpComponentsClientHttpRequestFactory();
    }

    @Bean
    public RestTemplate customRestTemplate()
    {
        return new RestTemplate(customHttpRequestFactory());
    }
}