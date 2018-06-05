package edu.pokemon.iut.pokedex.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import edu.pokemon.iut.pokedex.data.model.Pokemon;
import edu.pokemon.iut.pokedex.data.model.Sprites;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PokemonDao {
    @Insert(onConflict = REPLACE)
    void save(Pokemon pokemon);

    @Query("SELECT * FROM pokemon WHERE id = :pokemonId")
    LiveData<Pokemon> load(int pokemonId);

    @Query("SELECT * FROM pokemon")
    LiveData<List<Pokemon>> loadAll();

    @Query("SELECT * FROM pokemon WHERE name LIKE :query")
    LiveData<List<Pokemon>> loadAllWithFilter(String query);

    @Query("SELECT id from pokemon where id = :pokemonId")
    int hasPokemon(int pokemonId);

    @Query("SELECT COUNT(*) FROM pokemon")
    LiveData<Integer> getNumberOfPokemon();

    @Update(onConflict = REPLACE)
    void capture(Pokemon pokemon);

}