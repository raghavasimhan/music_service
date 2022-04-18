package com.plugsurfing.musicservice.config

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.plugsurfing.musicservice.repository.ArtistRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import java.io.IOException


@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = [ArtistRepository::class])
class MongoReactiveApplication : AbstractReactiveMongoConfiguration() {
    @Bean
    fun mongoClient(): MongoClient = MongoClients.create()

    override fun getDatabaseName() = "musicServiceDB"

    @Bean
    @Throws(IOException::class)
    fun mongoTemplate(mongoClient: MongoClient): ReactiveMongoTemplate {
        return ReactiveMongoTemplate(mongoClient, "musicServiceDB")
    }
}