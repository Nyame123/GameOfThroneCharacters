package com.bismark.gameofthronecharacters.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bismark.gameofthronecharacters.data_layer.service.MovieCharacterRaw
import com.bismark.gameofthronecharacters.empty

@Entity
data class MovieCharacter(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String?,
    val alias: String,
    val culture: String,
    val url: String,
    val gender: String,
    val books: List<String> = emptyList()
){
    companion object{
        fun empty() = MovieCharacter(
            0L,
            String.empty(),
            String.empty(),
            String.empty(),
            String.empty(),
            String.empty(),
            emptyList()
        )
    }
}
