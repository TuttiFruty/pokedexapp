package edu.pokemon.iut.pokedex.data.model;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

public class PokemonTypeStringTypeConverters {
    
    static Gson gson = new Gson();
    
    @TypeConverter
    public static List<Type> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        java.lang.reflect.Type listType = new TypeToken<List<Type>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Type> someObjects) {
        return gson.toJson(someObjects);
    }
}