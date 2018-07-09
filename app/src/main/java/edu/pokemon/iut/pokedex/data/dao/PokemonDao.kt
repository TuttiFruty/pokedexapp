package edu.pokemon.iut.pokedex.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import edu.pokemon.iut.pokedex.data.model.Pokemon

import android.arch.persistence.room.OnConflictStrategy.REPLACE

/**
 * DAO that give access to the Pokemon Database
 */
@Dao
interface PokemonDao {

    /**
     * Query to know the number of pokemon saved in database
     *
     * @return [Integer] number of pokemon, [LiveData] allow to observe any change in database and notify the observer.
     */
    @get:Query("SELECT COUNT(*) FROM pokemon")
    val numberOfPokemon: LiveData<Int>

    /**
     * Insert a pokemon, if it exist already the method will replace it.
     *
     * @param pokemon [Pokemon] to save
     */
    @Insert(onConflict = REPLACE)
    fun save(pokemon: Pokemon)

    /**
     * Query to retrieve a specific pokemon by is ID
     *
     * @param pokemonId of the pokemon to retrieve
     * @return [Pokemon] for the given id, [LiveData] allow to observe any change in database and notify the observer.
     */
    @Query("SELECT * FROM pokemon WHERE id = :pokemonId")
    fun load(pokemonId: Int): LiveData<Pokemon>

    /**
     * Query to retrieve ALL the pokemon
     *
     * @return [<], [LiveData] allow to observe any change in database and notify the observer.
     */
    @Query("SELECT * FROM pokemon")
    fun loadAll(): LiveData<List<Pokemon>>

    /**
     * Query to retrieve SOME pokemon filtered by their name
     *
     * @param query name or portion of name
     * @return [<], [LiveData] allow to observe any change in database and notify the observer.
     */
    @Query("SELECT * FROM pokemon WHERE name LIKE :query")
    fun loadAllWithFilter(query: String): LiveData<List<Pokemon>>

    /**
     * Query to know if a pokemon already exist or not in the database
     *
     * @param pokemonId of the one to check existence
     * @return id of the pokemon
     */
    @Query("SELECT id from pokemon where id = :pokemonId")
    fun hasPokemon(pokemonId: Int): Int

    /**
     * Query to update the status of capture for a pokemon
     *
     * @param pokemon [Pokemon] to update
     */
    @Update(onConflict = REPLACE)
    fun capture(pokemon: Pokemon)

}