package com.plugsurfing.musicservice.service

import com.plugsurfing.musicservice.config.MusicBrainzConfig
import com.plugsurfing.musicservice.model.MusicBrainzResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

interface MusicBrainzApiService {
    fun getBrainzByMbId(mbId: String): Mono<MusicBrainzResponse>
}

@Service
class MusicBrainzApiServiceImpl(
    val consumerService: ConsumerService,
    @Qualifier("musicBrandClient") val webClient: WebClient,
    val musicBrainzConfig: MusicBrainzConfig
) : MusicBrainzApiService {
    override fun getBrainzByMbId(mbId: String): Mono<MusicBrainzResponse> {
        return consumerService.fetch(
            webClient,
            musicBrainzConfig.getUri.replace("{id}", mbId),
            MusicBrainzResponse::class.java
        )
    }
}