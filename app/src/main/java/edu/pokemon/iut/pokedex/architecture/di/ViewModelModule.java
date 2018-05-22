package edu.pokemon.iut.pokedex.architecture.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import edu.pokemon.iut.pokedex.ui.pokemondetail.PokemonViewModel;
import edu.pokemon.iut.pokedex.ui.pokemonlist.PokemonListViewModel;
import edu.pokemon.iut.pokedex.architecture.viewmodel.ViewModelFactory;
import edu.pokemon.iut.pokedex.architecture.viewmodel.ViewModelKey;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFacotry (ViewModelFactory viewModelFactory);


    @Binds
    @IntoMap
    @ViewModelKey(PokemonListViewModel.class)
    abstract ViewModel bindsPokemonListViewModel(PokemonListViewModel pokemonListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PokemonViewModel.class)
    abstract ViewModel bindsPokemonViewModel(PokemonViewModel pokemonViewModel);
}