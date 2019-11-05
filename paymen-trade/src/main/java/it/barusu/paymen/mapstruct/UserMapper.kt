package it.barusu.paymen.mapstruct

import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface UserMapper {
    @Mapping(source = "title", target = "firstName")
    fun from(blog: Blog): User
}
