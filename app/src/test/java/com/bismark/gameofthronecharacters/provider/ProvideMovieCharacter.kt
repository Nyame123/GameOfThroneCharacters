package com.bismark.gameofthronecharacters.provider

import com.bismark.gameofthronecharacters.data_layer.service.MovieCharacterRaw
import com.bismark.gameofthronecharacters.database.entities.MovieCharacter
import com.bismark.gameofthronecharacters.empty

fun provideMovieCharacter(
    id: Long = 1,
    name: String = String.empty(),
    alias: String = String.empty(),
    culture: String = String.empty(),
    url: String = String.empty(),
    gender: String = String.empty(),
    books: List<String> = emptyList()
) = MovieCharacter(
    id = id,
    name = name,
    alias = alias,
    culture = culture,
    url = url,
    gender = gender,
    books = books
)

fun provideMovieCharacterRaw(
    name: String = String.empty(),
    alias: List<String> = emptyList(),
    culture: String = String.empty(),
    url: String = String.empty(),
    gender: String = String.empty(),
    books: List<String> = emptyList()
) = MovieCharacterRaw(
    name = name,
    aliases = alias,
    culture = culture,
    url = url,
    gender = gender,
    books = books
)
