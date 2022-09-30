package com.bismark.gameofthronecharacters.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bismark.gameofthronecharacters.database.entities.MovieCharacterRemoteKeys

@Dao
interface MovieCharacterRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<MovieCharacterRemoteKeys>)

    @Query("Select * from MovieCharacterRemoteKeys where pageID = :remoteKey")
    suspend fun movieCharacterRemoteKeyId(remoteKey: Int)

    @Query("Delete from MovieCharacterRemoteKeys")
    suspend fun deleteAll()
}
