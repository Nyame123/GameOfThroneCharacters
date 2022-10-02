package com.bismark.gameofthronecharacters.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class BookTypeConverter {

    @TypeConverter
    fun toJson(item: List<String>): String{
        val gSon = Gson()
        return gSon.toJson(item)
    }

    @TypeConverter
    fun fromJson(item: String): List<String>{
        val gSon = Gson()
        return gSon.fromJson(item, object : TypeToken<List<String>>(){}.type)
    }

}
