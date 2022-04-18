package com.plugsurfing.musicservice

import com.fasterxml.jackson.databind.ObjectMapper
import com.plugsurfing.musicservice.model.WikipediaResponse
import com.plugsurfing.musicservice.service.WebClientConsumerService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import java.io.IOException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
 class WebClientConsumerTest {
    private val objectMapper: ObjectMapper = ObjectMapper()
    private val systemUnderTest: WebClientConsumerService = WebClientConsumerService()
    private val webClient: WebClient = WebClient.create("http://localhost:8443")
    private var mockBackEnd: MockWebServer = MockWebServer()

    @BeforeAll
    @Throws(IOException::class)
    fun setUp() {
        mockBackEnd = MockWebServer()
        mockBackEnd.start(8443)
    }

    @AfterAll
    @Throws(IOException::class)
    fun tearDown() {
        mockBackEnd.shutdown()
    }

    @Test
    fun it_should_verify_webclient_request_response_and_validate() {
        mockBackEnd.enqueue(
            MockResponse()
                .setBody(objectMapper.writeValueAsString(Fixtures.getWikipediaResponse()))
                .addHeader("Content-Type", "application/json")
        )

        StepVerifier.create(systemUnderTest.fetch(webClient, "/ws/2/artist",  WikipediaResponse::class.java)).assertNext{
            Assertions.assertEquals(it.description, Fixtures.getWikipediaResponse().description)
        }.verifyComplete()

        val recordedRequest = mockBackEnd.takeRequest()

        Assertions.assertEquals("GET", recordedRequest.method)
        Assertions.assertEquals("/ws/2/artist", recordedRequest.path)
    }
}
