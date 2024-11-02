package com.banking.apigateway.webClient.service;


import com.banking.apigateway.webClient.configuration.WebClientConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebClientService {

    @Value("${application.base.url}")
    private String baseURL;

    private WebClientConfiguration webClientConfiguration;

    public WebClientService() {
    }

    public WebClientConfiguration getWebClient(){
        return new WebClientConfiguration(this.baseURL);
    }


}
