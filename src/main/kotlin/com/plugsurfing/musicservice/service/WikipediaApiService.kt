package com.plugsurfing.musicservice.service

import com.plugsurfing.musicservice.config.WikipediaConfig
import com.plugsurfing.musicservice.model.WikipediaResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

interface WikipediaApiService {
    fun getWikipediaResponseById(id: String): Mono<WikipediaResponse>
}

@Service
class WikipediaApiServiceImpl(
    val consumerService: ConsumerService,
    @Qualifier("wikipediaClient") val webClient: WebClient,
    val wikipediaConfig: WikipediaConfig,
    val wikiDataApiService: WikiDataApiService
) : WikipediaApiService {
    override fun getWikipediaResponseById(id: String): Mono<WikipediaResponse> {
        return wikiDataApiService.getWikiUrlById(id).flatMap {
            consumerService.fetch(
                webClient,
                wikipediaConfig.getUri.replace("{title}", it.split("/").last()),
                WikipediaResponse::class.java
            )
        }
    }
}