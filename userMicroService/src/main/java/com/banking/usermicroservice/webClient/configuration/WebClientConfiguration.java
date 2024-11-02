package com.banking.usermicroservice.webClient.configuration;

import com.banking.usermicroservice.Enums.MonoFluxType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
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

    public Object callGetWebClient(HttpMethod httpMethod, String uri, String queryParam,
                                   Map<String, String> header, Object object, MonoFluxType monoFluxType, Object obj) {
        if (httpMethod.equals(HttpMethod.GET)) {
            WebClient.ResponseSpec retrieve = webClient().method(httpMethod)
                    .uri(uri, Objects.nonNull(queryParam) ? queryParam : "")
                    .retrieve();
            if (monoFluxType.equals(MonoFluxType.Mono))
                return retrieve
                        .bodyToMono(obj.getClass());
            else
                return retrieve
                        .bodyToFlux(obj.getClass());
        } else if (httpMethod.equals(HttpMethod.POST)) {
            WebClient.ResponseSpec retrieve = webClient().method(httpMethod)
                    .uri(uri, Objects.nonNull(queryParam) ? queryParam : "")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(object), Map.class)
                    .retrieve();
            if (monoFluxType.equals(MonoFluxType.Mono))
                return retrieve
                        .bodyToMono(obj.getClass());
            else
                return retrieve
                        .bodyToFlux(obj.getClass());
        }
        return null;
    }


}
