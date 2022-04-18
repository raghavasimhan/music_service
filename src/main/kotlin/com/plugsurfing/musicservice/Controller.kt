package com.plugsurfing.musicservice

import com.plugsurfing.musicservice.model.Artist
import com.plugsurfing.musicservice.service.MusicArtistService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class MusicController(val musicArtistService: MusicArtistService) {

    @GetMapping("/musify/music-artist/details/{mbid}")
    fun getArtistById(@PathVariable mbid : String) : Mono<Artist> {
        return musicArtistService.getMusicArtistById(mbid)
    }
}