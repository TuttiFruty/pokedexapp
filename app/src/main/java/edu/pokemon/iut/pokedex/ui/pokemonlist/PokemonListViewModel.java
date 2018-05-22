package edu.pokemon.iut.pokedex.ui.pokemonlist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import edu.pokemon.iut.pokedex.data.model.Pokemon;
import edu.pokemon.iut.pokedex.data.repository.PokemonRepository;

import java.util.List;

import javax.inject.Inject;


public class PokemonListViewModel extends ViewModel {
    private final PokemonRepository pokemonRepository;
    LiveData<List<Pokemon>> pokemonList;

    @Inject
    public PokemonListViewModel(PokemonRepository pokemonRepository){
        this.pokemonRepository = pokemonRepository;
    }

    public void init(){
        if(this.pokemonList != null){
            return;
        }
        pokemonList = pokemonRepository.getPokemons();
    }

   public LiveData<List<Pokemon>> getPokemons(){
        return this.pokemonList;
   }
}
