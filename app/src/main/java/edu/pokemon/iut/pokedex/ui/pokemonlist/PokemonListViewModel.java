package edu.pokemon.iut.pokedex.ui.pokemonlist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import edu.pokemon.iut.pokedex.data.model.Pokemon;
import edu.pokemon.iut.pokedex.data.repository.PokemonRepository;


public class PokemonListViewModel extends ViewModel {
    private final PokemonRepository pokemonRepository;
    private LiveData<List<Pokemon>> pokemonList;

    @Inject
    public PokemonListViewModel(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public void init(CharSequence query) {
        if (this.pokemonList != null) {
            return;
        }
        pokemonList = pokemonRepository.getPokemons(query);
    }

    public LiveData<List<Pokemon>> getPokemons() {
        return this.pokemonList;
    }

    public void capture(Pokemon pokemon) {
        pokemonRepository.capture(pokemon);
    }
}
