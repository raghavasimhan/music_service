package com.plugsurfing.musicservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.rmi.ServerException


@Configuration
class WebClientConfig {

    @Bean(name = ["musicBrandClient"])
    fun musicBrandWebClient(musicBrainzConfig: MusicBrainzConfig): WebClient = buildWebClient(musicBrainzConfig.baseUrl)

    @Bean(name = ["wikiDataClient"])
    fun wikiDataClient(wikiDataConfig: WikiDataConfig): WebClient = buildWebClient(wikiDataConfig.baseUrl)

    @Bean(name = ["wikipediaClient"])
    fun wikipediaClient(wikipediaConfig: WikipediaConfig): WebClient = buildWebClient(wikipediaConfig.baseUrl)

    @Bean(name = ["coverArtClient"])
    fun musicBrandWebClient(coverArtConfig: CoverArtConfig): WebClient = buildWebClient(coverArtConfig.baseUrl)

    private fun buildWebClient(baseUrl: String): WebClient {
        return WebClient
            .builder()
            .exchangeStrategies(getStrategies())
            .clientConnector(
                ReactorClientHttpConnector(
                    HttpClient.create().followRedirect(true)
                )
            )
            .filter(errorHandler())
            .baseUrl(baseUrl)
            .build()
    }

    fun getStrategies(): ExchangeStrategies {
        val size = 16 * 1024 * 1024
        return ExchangeStrategies.builder()
            .codecs { codecs: ClientCodecConfigurer ->
                codecs.defaultCodecs().maxInMemorySize(size)
            }
            .build()
    }

    fun errorHandler(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
            if (clientResponse.statusCode().is5xxServerError) {
                return@ofResponseProcessor clientResponse.bodyToMono(String::class.java)
                    .flatMap { errorBody: String? ->
                        Mono.error(
                            ServerException(errorBody)
                        )
                    }
            } else if (clientResponse.statusCode().is4xxClientError) {
                return@ofResponseProcessor clientResponse.bodyToMono(String::class.java)
                    .flatMap { errorBody: String? ->
                        Mono.error(
                            ServerException(errorBody)
                        )
                    }
            } else {
                return@ofResponseProcessor Mono.just(clientResponse)
            }
        }
    }
}