package edu.pokemon.iut.pokedex.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import javax.inject.Singleton;

import edu.pokemon.iut.pokedex.data.dao.PokemonDao;
import edu.pokemon.iut.pokedex.data.model.Pokemon;
import edu.pokemon.iut.pokedex.data.model.Sprites;

@Singleton
@Database(entities = {Pokemon.class, Sprites.class}, version = 2, exportSchema = false)
public abstract class PokedexDatabase extends RoomDatabase {
    public abstract PokemonDao pokemonDao();
}