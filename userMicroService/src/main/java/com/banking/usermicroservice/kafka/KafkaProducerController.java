package com.banking.usermicroservice.kafka;

import com.banking.usermicroservice.dto.UserDtoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class KafkaProducerController {



    private final ObjectMapper objectMapper;

    private final ReactiveKafkaProducerTemplate<String,UserDtoResponse> kafkaProducerTemplate;


    public void sendMessage(UserDtoResponse userDtoResponse){
        ProducerRecord<String, UserDtoResponse> events = new ProducerRecord<>("events", UUID.randomUUID().toString(), userDtoResponse);
        kafkaProducerTemplate.send(events)
                .doOnSuccess(voidSenderResult -> {
                    RecordMetadata recordMetadata = voidSenderResult.recordMetadata();
                    System.out.println("event is sent to "+recordMetadata.topic()+" partition "+recordMetadata.partition()+" actual event"+ recordMetadata);
                });
    }


}
