package com.plugsurfing.musicservice.service

import com.plugsurfing.musicservice.utils.Utils
import com.plugsurfing.musicservice.config.WikiDataConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

interface WikiDataApiService {
    fun getWikiUrlById(id: String): Mono<String>
}

@Service
class WikiDataApiServiceImpl(
    val consumerService: ConsumerService,
    @Qualifier("wikiDataClient") val webClient: WebClient,
    val wikiDataConfig: WikiDataConfig
) : WikiDataApiService {
    override fun getWikiUrlById(id: String): Mono<String> {
        return consumerService.fetch(
            webClient,
            wikiDataConfig.getUri.replace("{id}", id),
            String::class.java
        ).map { Utils.parseWikiDataResponse(it, id) }
            .onErrorReturn("")
    }
}