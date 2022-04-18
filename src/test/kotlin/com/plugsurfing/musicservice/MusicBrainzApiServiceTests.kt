package com.plugsurfing.musicservice

import com.plugsurfing.musicservice.model.Artist
import com.plugsurfing.musicservice.model.MusicBrainzResponse
import com.plugsurfing.musicservice.repository.ArtistRepository
import com.plugsurfing.musicservice.service.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.*
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MusicBrainzApiServiceTests {
    private val musicBrainzApiService = mockk<MusicBrainzApiService>()
    private val wikipediaApiService = mockk<WikipediaApiService>()
    private val coverArtApiService = mockk<CoverArtApiService>()
    private val artistRepository = mockk<ArtistRepository>()
    private val systemUnderTest = MusicArtistServiceImpl(musicBrainzApiService, wikipediaApiService, coverArtApiService, artistRepository)

    @Test
    fun it_should_get_artist_when_doesnt_exists_in_db_fetch_from_apis_and_validate() {
        val artist = Fixtures.getArtist()
        every { artistRepository.findById("1234") } returns Mono.empty()
        every { musicBrainzApiService.getBrainzByMbId("1234") } returns Mono.just(Fixtures.getMusicBarinzResponse())
        every { wikipediaApiService.getWikipediaResponseById("test_data") } returns Mono.just(Fixtures.getWikipediaResponse())
        every { coverArtApiService.getCoverArtByIds(listOf("543")) } returns Mono.just(Fixtures.getCoverImage())
        every { artistRepository.save(artist) } returns Mono.just(artist)

        StepVerifier.create(systemUnderTest.getMusicArtistById("1234")).assertNext {
            Assertions.assertEquals(it.id, "123")
            Assertions.assertEquals(it.name, "test name")
            Assertions.assertEquals(it.albums.first().imageUrl, "image_url")
        }.verifyComplete()
    }

    @Test
    fun it_should_get_artist_from_db_when_exists_and_validate() {
        val artist = Fixtures.getArtist()
        every { artistRepository.findById("1234") } returns Mono.just(artist)

        StepVerifier.create(systemUnderTest.getMusicArtistById("1234")).assertNext {
            Assertions.assertEquals(it.id, "123")
            Assertions.assertEquals(it.name, "test name")
            Assertions.assertEquals(it.albums.first().imageUrl, "image_url")
        }.verifyComplete()
    }
}
