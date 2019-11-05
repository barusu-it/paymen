package it.barusu.paymen

import it.barusu.paymen.mapstruct.Blog
import it.barusu.paymen.mapstruct.UserMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory

class MapstructTests {
    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @DisplayName("test mapstruct")
    @Test
    fun testMapstruct() {

        val blog = Blog(title = "hello")
        val mapper = Mappers.getMapper(UserMapper::class.java)
        val user = mapper.from(blog)
        log.info("user: $user")
        assertThat(user.firstName).isEqualTo(blog.title)
    }
}