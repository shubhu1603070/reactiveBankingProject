package com.banking.usermicroservice.kafka;

import com.banking.usermicroservice.dto.UserDtoResponse;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaConfiguration {

    @Bean
    public ReactiveKafkaProducerTemplate<String, UserDtoResponse> reactiveKafkaProducerTemplate(final KafkaProperties kafkaProperties){
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(kafkaProperties.buildProducerProperties(new DefaultSslBundleRegistry())));
    }

}
