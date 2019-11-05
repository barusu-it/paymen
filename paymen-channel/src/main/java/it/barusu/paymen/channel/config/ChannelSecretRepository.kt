package it.barusu.paymen.channel.config

import org.springframework.data.mongodb.repository.MongoRepository

interface ChannelSecretRepository : MongoRepository<ChannelSecret, String>