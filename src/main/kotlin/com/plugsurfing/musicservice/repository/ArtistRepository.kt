package com.plugsurfing.musicservice.repository

import com.plugsurfing.musicservice.model.Artist
import org.springframework.data.mongodb.repository.ReactiveMongoRepository


interface ArtistRepository : ReactiveMongoRepository<Artist, String>