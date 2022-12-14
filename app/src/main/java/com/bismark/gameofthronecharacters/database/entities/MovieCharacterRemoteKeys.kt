package com.bismark.gameofthronecharacters.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieCharacterRemoteKeys(
    @PrimaryKey(autoGenerate = true) val pageID: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
