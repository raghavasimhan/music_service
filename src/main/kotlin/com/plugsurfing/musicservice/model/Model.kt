package com.plugsurfing.musicservice.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

data class MusicBrainzResponse(
    var id: String,
    var name: String,
    var gender: String?,
    var country: String,
    var disambiguation: String?,
    var relations: ArrayList<Relation>,
    @JsonProperty("release-groups") var releaseGroups: List<ReleaseGroup>
)

data class Relation(var url: Url?, var type: String)

data class Url(var id: String, var resource: String?)

data class ReleaseGroup(var id: String, var title: String)

data class WikipediaResponse(var description: String)

data class CoverArtResponse(var images: List<ImageResponse>)

data class CoverImage(var id : String, var image : String)

data class ImageResponse(var image: String, var types: List<String>)

data class Album(var id: String, var title: String, var imageUrl : String?)

@Document
data class Artist(@Id var id : String,
                  var name: String,
                  var gender: String?,
                  var country: String,
                  var disambiguation: String?,
                  var description: String,
                  var albums: List<Album> = emptyList()
) {
    companion object {
        fun map(musicBrainzResponse : MusicBrainzResponse, description: String, albums: List<Album> ) : Artist {
            return Artist(musicBrainzResponse.id, musicBrainzResponse.name, musicBrainzResponse.gender,
            musicBrainzResponse.country, musicBrainzResponse.disambiguation, description, albums)
        }
    }
}