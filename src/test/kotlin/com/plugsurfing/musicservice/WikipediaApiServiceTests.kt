package com.plugsurfing.musicservice

import com.plugsurfing.musicservice.config.WikipediaConfig
import com.plugsurfing.musicservice.model.WikipediaResponse
import com.plugsurfing.musicservice.service.ConsumerService
import com.plugsurfing.musicservice.service.WikiDataApiService
import com.plugsurfing.musicservice.service.WikipediaApiServiceImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WikipediaApiServiceTests {
    private val consumerService = mockk<ConsumerService>()
    private val wikipediaConfig = mockk<WikipediaConfig>()
    private val webClient = mockk<WebClient>()
    private val wikiDataApiService = mockk<WikiDataApiService>()
    private val systemUnderTest = WikipediaApiServiceImpl(consumerService, webClient, wikipediaConfig, wikiDataApiService)
    private val testUri = "testUri"

    @Test
    fun it_should_verify_wikipedia_request_response_and_validate() {
        every { wikiDataApiService.getWikiUrlById("1234") } returns Mono.just(testUri)
        every { wikipediaConfig.getUri } returns testUri
        every { consumerService.fetch(webClient, testUri, WikipediaResponse::class.java) } returns Mono.just(
            Fixtures.getWikipediaResponse()
        )
        StepVerifier.create(systemUnderTest.getWikipediaResponseById("1234")).assertNext {
            Assertions.assertEquals(it.description, Fixtures.getWikipediaResponse().description)
        }.verifyComplete()
    }
}
