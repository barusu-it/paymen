package it.barusu.paymen.channel

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
// load jpa auto configurations
@EnableAutoConfiguration
@Configuration
class DataJpaTestConfiguration