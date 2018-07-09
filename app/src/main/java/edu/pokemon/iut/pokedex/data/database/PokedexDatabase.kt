package edu.pokemon.iut.pokedex.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import javax.inject.Singleton

import edu.pokemon.iut.pokedex.data.dao.PokemonDao
import edu.pokemon.iut.pokedex.data.model.Pokemon
import edu.pokemon.iut.pokedex.data.model.Sprites

/**
 * Abstract class for the Database. extends [RoomDatabase]<br></br>
 * You need to declare here any new DAO that you could need to access the database<br></br>
 * If any change is made on the database, you will need to increment the version<br></br>
 * Here you can declare any entity as Table for the database<br></br>
 * Current Table :<br></br>
 * - [Pokemon]<br></br>
 * - [Sprites]<br></br>
 */
@Singleton
@Database(entities = arrayOf(Pokemon::class, Sprites::class), version = 2, exportSchema = false)
abstract class PokedexDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}