package com.banking.apigateway.webClient.configuration;


import com.banking.apigateway.enums.MonoFluxType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;


public class WebClientConfiguration {

    private final String baseURL;

    public WebClientConfiguration(String baseURL) {
        this.baseURL = baseURL;
    }

    public WebClient webClient() {
        return WebClient.create(baseURL);
    }

    public Mono<Object> callGetWebClientGetMono(String uri, String queryParam,
                                                Map<String, String> header){
        return webClient().method(HttpMethod.GET)
                .uri(uri, Objects.nonNull(queryParam) ? queryParam : "")
                .retrieve().bodyToMono(Object.class);
    }

    public Mono<String> callGetWebClientGetMonoString(String uri, String queryParam,
                                                Map<String, String> header){
        return webClient().method(HttpMethod.GET)
                .uri(uri, Objects.nonNull(queryParam) ? queryParam : "")
                .retrieve().bodyToMono(String.class);
    }

    public Flux<Object> callGetWebClientGetFlux(String uri, String queryParam,
                                                Map<String, String> header){
        return webClient().method(HttpMethod.GET)
                .uri(uri, Objects.nonNull(queryParam) ? queryParam : "")
                .retrieve().bodyToFlux(Object.class);
    }

    public Mono<Object> callGetWebClientPostMono(String uri, String queryParam,
                                                Map<String, String> header,Object request){
        return webClient().method(HttpMethod.POST)
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),Object.class)
                .retrieve().bodyToMono(Object.class);
    }

    public Flux<Object> callGetWebClientPostFlux(String uri, String queryParam,
                                                Map<String, String> header,Object request){
        return webClient().method(HttpMethod.POST)
                .uri(uri, Objects.nonNull(queryParam) ? queryParam : "")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request,Map.class)
                .retrieve().bodyToFlux(Object.class);
    }


}
