package edu.pokemon.iut.pokedex.data.model

import android.arch.persistence.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.Collections

/**
 * [TypeConverter] are used to convert some complex object in a way that [android.arch.persistence.room.RoomDatabase] can store.<br></br>
 * This class allow to convert Any complex [<] in Json to store in the database, and to convert them back to object when reading it.
 */
class PokemonTypeStringTypeConverters {

    private val gson = Gson()

    /**
     * Convert from Json [String] to [<]
     *
     * @param data json strong to convert
     * @return [<]
     */
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Type> {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<Type>>() {

        }.type

        return gson.fromJson(data, listType)
    }

    /**
     * Convert from [<] to Json [String]
     *
     * @param someObjects [<] to convert
     * @return json as [String]
     */
    @TypeConverter
    fun someObjectListToString(someObjects: List<Type>): String {
        return gson.toJson(someObjects)
    }
}