package edu.pokemon.iut.pokedex.ui.pokemondetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import edu.pokemon.iut.pokedex.data.model.Pokemon;
import edu.pokemon.iut.pokedex.data.repository.PokemonRepository;

public class PokemonViewModel extends ViewModel {

    private final PokemonRepository pokemonRepository;
    LiveData<Pokemon> pokemon;

    @Inject
    public PokemonViewModel(PokemonRepository pokemonRepository){
        this.pokemonRepository = pokemonRepository;
    }

    public void init(int pokemonId){
        if(this.pokemon != null){
            return;
        }
        pokemon = pokemonRepository.getPokemon(pokemonId);
    }

    public LiveData<Pokemon> getPokemon(){
        return this.pokemon;
    }

}
