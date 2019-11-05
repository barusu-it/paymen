package it.barusu.paymen.channel

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing

@EnableMongoAuditing
@EnableAutoConfiguration
@Configuration
class MongodbTestConfiguration