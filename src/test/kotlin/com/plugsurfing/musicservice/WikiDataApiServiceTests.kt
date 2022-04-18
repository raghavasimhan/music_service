package com.plugsurfing.musicservice

import com.plugsurfing.musicservice.config.WikiDataConfig
import com.plugsurfing.musicservice.service.ConsumerService
import com.plugsurfing.musicservice.service.WikiDataApiServiceImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WikiDataApiServiceTests {
    private val consumerService = mockk<ConsumerService>()
    private val wikiDataConfig = mockk<WikiDataConfig>()
    private val webClient = mockk<WebClient>()
    private val systemUnderTest = WikiDataApiServiceImpl(consumerService, webClient, wikiDataConfig)
    private val testUri = "testUri"
    @Test
    fun it_should_verify_wikidata_request_response_and_validate() {
        every { wikiDataConfig.getUri } returns testUri
        every { consumerService.fetch(webClient, testUri, String::class.java) } returns Mono.just(Fixtures.getWikiDataResponse())
        StepVerifier.create(systemUnderTest.getWikiUrlById("1234")).assertNext {
            Assertions.assertEquals(it, "wikiUrl")
        }.verifyComplete()
    }
}
