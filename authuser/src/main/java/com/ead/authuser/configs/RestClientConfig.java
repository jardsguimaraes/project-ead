package com.ead.authuser.configs;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    private static final int TIMEOUT = 5000;

    @SuppressWarnings("null")
    @LoadBalanced
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder().requestFactory(customRequestFactory());
    }

    @SuppressWarnings("null")
    private JdkClientHttpRequestFactory customRequestFactory() {
        var httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(TIMEOUT))
                .build();

        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofMillis(TIMEOUT));

        return requestFactory;
    }
}
