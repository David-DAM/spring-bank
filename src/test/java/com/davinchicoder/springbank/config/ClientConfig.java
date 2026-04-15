package com.davinchicoder.springbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8080")
                .configureMessageConverters(converters -> converters.addCustomConverter(new Jaxb2RootElementHttpMessageConverter()))
                .build();
    }

}
