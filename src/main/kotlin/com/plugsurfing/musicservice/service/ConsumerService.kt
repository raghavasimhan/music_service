package com.plugsurfing.musicservice.service

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration
import java.time.temporal.ChronoUnit

interface ConsumerService {
    fun  <T: Any> fetch(webClient: WebClient, uri: String, typeRef: Class<T>) : Mono<T> {
        return webClient.get()
            .uri(uri)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(typeRef)
           // .retryWhen(Retry.backoff(3, Duration.of(2, ChronoUnit.SECONDS)))
    }
}

@Service
class WebClientConsumerService : ConsumerService
