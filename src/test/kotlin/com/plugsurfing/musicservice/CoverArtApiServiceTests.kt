package com.plugsurfing.musicservice

import com.plugsurfing.musicservice.config.CoverArtConfig
import com.plugsurfing.musicservice.model.CoverArtResponse
import com.plugsurfing.musicservice.service.ConsumerService
import com.plugsurfing.musicservice.service.CoverArtApiServiceImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CoverArtApiServiceTests {
    private val consumerService = mockk<ConsumerService>()
    private val coverArtConfig = mockk<CoverArtConfig>()
    private val webClient = mockk<WebClient>()
    private val systemUnderTest = CoverArtApiServiceImpl(consumerService, webClient, coverArtConfig)
    private val testUri = "testUri"

    @Test
    fun it_should_verify_cover_art_request_response_and_validate() {
        every { coverArtConfig.getUri } returns testUri
        every { consumerService.fetch(webClient, testUri, CoverArtResponse::class.java) } returns Mono.just(
            Fixtures.getCoverArtResponse()
        )
        StepVerifier.create(systemUnderTest.getCoverArtByIds(listOf("1234"))).assertNext {
            Assertions.assertEquals(it.first().image, "test_image")
            Assertions.assertEquals(it.first().id, "1234")
        }.verifyComplete()
    }
}
