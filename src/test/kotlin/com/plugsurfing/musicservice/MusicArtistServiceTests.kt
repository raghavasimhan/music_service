package com.plugsurfing.musicservice

import com.plugsurfing.musicservice.config.MusicBrainzConfig
import com.plugsurfing.musicservice.model.MusicBrainzResponse
import com.plugsurfing.musicservice.service.ConsumerService
import com.plugsurfing.musicservice.service.MusicBrainzApiService
import com.plugsurfing.musicservice.service.MusicBrainzApiServiceImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.*
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MusicArtistServiceTests {
    private val consumerService = mockk<ConsumerService>()
    private val brainzConfig = mockk<MusicBrainzConfig>()
    private val webClient = mockk<WebClient>()
    private val systemUnderTest: MusicBrainzApiService =
        MusicBrainzApiServiceImpl(consumerService, webClient, brainzConfig)

    @Test
    fun it_should_verify_music_brainz_request_response_validate() {
        every { brainzConfig.getUri } returns "testpath"
        every { consumerService.fetch(webClient, "testpath", MusicBrainzResponse::class.java) } returns Mono.just(
            Fixtures.getMusicBarinzResponse()
        )
        StepVerifier.create(systemUnderTest.getBrainzByMbId("1234")).assertNext {
            Assertions.assertEquals(it.id, "123")
            Assertions.assertEquals(it.name, "test name")
        }.verifyComplete()
    }
}
