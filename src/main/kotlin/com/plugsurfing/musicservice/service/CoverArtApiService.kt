package com.plugsurfing.musicservice.service

import com.plugsurfing.musicservice.config.CoverArtConfig
import com.plugsurfing.musicservice.model.CoverArtResponse
import com.plugsurfing.musicservice.model.CoverImage
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CoverArtApiService {
    fun getCoverArtByIds(ids: List<String>): Mono<List<CoverImage>>
}

@Service
class CoverArtApiServiceImpl(
    val consumerService: ConsumerService,
    @Qualifier("coverArtClient") val webClient: WebClient,
    val coverArtConfig: CoverArtConfig
) : CoverArtApiService {
    override fun getCoverArtByIds(ids: List<String>): Mono<List<CoverImage>> {
        return Flux.concat(ids
            .map {
                getCoverArtById(it)
                    .map { response -> CoverImage(it, response.images.first().image) }
                    .onErrorReturn (CoverImage(it, "") )
            }).collectList()
    }

    fun getCoverArtById(id: String): Mono<CoverArtResponse> {
        return consumerService.fetch(
            webClient,
            coverArtConfig.getUri.replace("{id}", id),
            CoverArtResponse::class.java
        )
    }
}