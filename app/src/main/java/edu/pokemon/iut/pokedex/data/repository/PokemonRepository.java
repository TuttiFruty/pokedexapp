package edu.pokemon.iut.pokedex.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import edu.pokemon.iut.pokedex.data.model.Pokemon;

public interface PokemonRepository {
    LiveData<List<Pokemon>> getPokemons();

    LiveData<Pokemon> getPokemon(int pokemonId);
}
