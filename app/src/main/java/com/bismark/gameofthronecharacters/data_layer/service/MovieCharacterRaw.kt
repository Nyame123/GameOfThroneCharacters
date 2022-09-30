package com.bismark.gameofthronecharacters.data_layer

import com.bismark.gameofthronecharacters.database.entities.MovieCharacter
import com.squareup.moshi.JsonClass
import com.bismark.gameofthronecharacters.empty
import com.bismark.gameofthronecharacters.toSafeString

@JsonClass(generateAdapter = true)
data class MovieCharacterRaw(
    internal val url: String? = String.empty(),
    internal val name: String? = String.empty(),
    internal val aliases: List<String>? = emptyList(),
    internal val gender: String? = String.empty(),
    internal val culture: String? = String.empty(),
    internal val books: List<String>? = emptyList()
){
    companion object{
        fun empty() = MovieCharacterRaw(
            String.empty(),
            String.empty(),
            emptyList(),
            String.empty(),
            String.empty(),
            emptyList()
        )
    }

    fun toMovieCharacter() = MovieCharacter(
        name = name.toSafeString(),
        url = url.toSafeString(),
        alias = aliases?.get(0) ?: String.empty(),
        gender = gender.toSafeString(),
        culture = culture.toSafeString(),
        books = books ?: emptyList()
    )
}

@JsonClass(generateAdapter = true)
data class MovieCharactersRaw(internal val movieCharacters: List<MovieCharacterRaw>? = null)
