package com.plugsurfing.musicservice

import com.plugsurfing.musicservice.model.*
import org.json.JSONObject

class Fixtures {
    companion object {
        fun getMusicBarinzResponse(): MusicBrainzResponse {
            return MusicBrainzResponse(
                "123",
                "test name",
                "Male",
                "US",
                "POP_STAR",
                arrayListOf(Relation(Url("1234", "/test_data"), "wikidata")),
                listOf(ReleaseGroup("543", "test title"))
            )
        }

        fun getWikipediaResponse(): WikipediaResponse = WikipediaResponse("Test description")

        fun getCoverArtResponse(): CoverArtResponse =
            CoverArtResponse(listOf(ImageResponse("test_image", listOf("testType"))))

        fun getCoverImage() = listOf(CoverImage("543", "image_url"))

        fun getArtist() = Artist.map(
            getMusicBarinzResponse(),
            getWikipediaResponse().description,
            listOf(Album("543", "test title", "image_url"))
        )

        fun getWikiDataResponse(): String {
            return JSONObject("""{"entities":{"1234":{"sitelinks":{"enwiki": {"url": "wikiUrl"}}}}}"}""").toString()
        }
    }
}