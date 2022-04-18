package com.plugsurfing.musicservice

import com.plugsurfing.musicservice.model.Artist
import com.plugsurfing.musicservice.repository.ArtistRepository
import com.plugsurfing.musicservice.service.MusicArtistService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
class MusicServiceApp
fun main(args: Array<String>) {
    runApplication<MusicServiceApp>(*args)
}

