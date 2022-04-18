package com.plugsurfing.musicservice.service

import com.plugsurfing.musicservice.model.*
import com.plugsurfing.musicservice.repository.ArtistRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

interface MusicArtistService {
    fun getMusicArtistById(mbId: String): Mono<Artist>
}

@Service
class MusicArtistServiceImpl(
    val musicBrainzApiService: MusicBrainzApiService,
    val wikipediaApiService: WikipediaApiService,
    val coverArtApiService: CoverArtApiService,
    val artistRepository: ArtistRepository
) : MusicArtistService {
    override fun getMusicArtistById(mbId: String): Mono<Artist> {
        return artistRepository.findById(mbId).switchIfEmpty {
            musicBrainzApiService.getBrainzByMbId(mbId)
                .flatMap { musicBrainz ->
                    Mono.zip(
                        fetchWikipediaResponseById(musicBrainz.relations),
                        fetchCoverArt(musicBrainz.releaseGroups)
                    )
                        .map { tuple ->
                            Artist.map(musicBrainz, tuple.t1.description, getAlbums(musicBrainz, tuple.t2))
                        }.flatMap {
                            artistRepository.save(it)
                        }
                }
        }
    }

    fun fetchCoverArt(releaseGroups: List<ReleaseGroup>) = coverArtApiService.getCoverArtByIds(releaseGroups.map { it.id })

    fun fetchWikipediaResponseById(relations: List<Relation>) =
        wikipediaApiService.getWikipediaResponseById(getWikiDataLink(relations))

    fun getAlbums(musicBrainz: MusicBrainzResponse, coverImages: List<CoverImage>): List<Album> {
        return musicBrainz.releaseGroups.map {
            Album(it.id, it.title, coverImages.find { t1 -> t1.id == it.id }?.image)
        }
    }

    fun getWikiDataLink(relations: List<Relation>): String {
        return relations.first { it.type == "wikidata" }.url?.resource?.split("/")?.last() ?: ""
    }
}

