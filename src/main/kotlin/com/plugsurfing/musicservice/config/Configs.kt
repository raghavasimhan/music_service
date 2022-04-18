package com.plugsurfing.musicservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "apis.music-brainz")
data class MusicBrainzConfig(
    var baseUrl : String = "",
    var getUri : String = ""
)

@Component
@ConfigurationProperties(prefix = "apis.wiki-data")
data class WikiDataConfig(
    var baseUrl : String = "",
    var getUri : String = ""
)

@Component
@ConfigurationProperties(prefix = "apis.cover-art")
data class CoverArtConfig(
    var baseUrl : String = "",
    var getUri : String = ""
)

@Component
@ConfigurationProperties(prefix = "apis.wikipedia")
data class WikipediaConfig(
    var baseUrl : String = "",
    var getUri : String = ""
)