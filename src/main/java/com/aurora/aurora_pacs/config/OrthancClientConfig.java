package com.aurora.aurora_pacs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OrthancClientConfig {

    @Bean
    public WebClient orthancRestClient(
            @Value("${orthanc.base-url}") String baseUrl,
            @Value("${orthanc.username:}") String username,
            @Value("${orthanc.password:}") String password) {

        WebClient.Builder builder = WebClient.builder().baseUrl(baseUrl);
        if (username != null && !username.isBlank()) {
            builder.defaultHeaders(headers -> headers.setBasicAuth(username, password == null ? "" : password));
        }
        return builder.build();
    }

    @Bean
    public WebClient dicomwebClient(
            @Value("${orthanc.dicomweb-base}") String baseUrl,
            @Value("${orthanc.username:}") String username,
            @Value("${orthanc.password:}") String password) {

        WebClient.Builder builder = WebClient.builder().baseUrl(baseUrl);
        if (username != null && !username.isBlank()) {
            builder.defaultHeaders(headers -> headers.setBasicAuth(username, password == null ? "" : password));
        }
        return builder.build();
    }
}
