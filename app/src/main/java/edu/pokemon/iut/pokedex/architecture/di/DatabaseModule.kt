package edu.pokemon.iut.pokedex.architecture.di

import android.arch.persistence.room.Room
import android.content.Context

import javax.inject.Inject

import dagger.Module
import dagger.Provides
import edu.pokemon.iut.pokedex.data.dao.PokemonDao
import edu.pokemon.iut.pokedex.data.database.PokedexDatabase

/**
 * Dependencies injection Module for accessing the database
 */
@Module
class DatabaseModule
/**
 * Constructor of the module called by Dagger to provide the context
 *
 * @param context used to build the database
 */
(// The context is used to build the database
        private val context: Context) {

    /**
     * Allow the use of the database of Pokemon
     *
     * @return an instance of [PokedexDatabase]
     */
    @Provides
    fun providesPokedexDatabase(): PokedexDatabase {
        return Room.databaseBuilder(context,
                PokedexDatabase::class.java, "pokedex-database")
                .fallbackToDestructiveMigration()
                .build()
    }

    /**
     * Allow the use of the [PokemonDao] to access the database through database request
     *
     * @param pokedexDatabase wich we want to access
     * @return an instance of [PokemonDao]
     */
    @Provides
    @Inject
    fun providesPokemonDao(pokedexDatabase: PokedexDatabase): PokemonDao {
        return pokedexDatabase.pokemonDao()
    }
}
