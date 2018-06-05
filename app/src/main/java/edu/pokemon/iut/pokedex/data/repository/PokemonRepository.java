package edu.pokemon.iut.pokedex.data.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import edu.pokemon.iut.pokedex.data.model.Pokemon;

public interface PokemonRepository {
    LiveData<List<Pokemon>> getPokemons(CharSequence query);

    LiveData<Pokemon> getPokemon(int pokemonId);

    void capture(Pokemon pokemon);

    LiveData<Integer> getNumberOfPokemon();
}
