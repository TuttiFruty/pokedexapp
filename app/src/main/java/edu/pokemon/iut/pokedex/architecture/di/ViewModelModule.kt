package edu.pokemon.iut.pokedex.architecture.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import edu.pokemon.iut.pokedex.architecture.viewmodel.ViewModelFactory
import edu.pokemon.iut.pokedex.architecture.viewmodel.ViewModelKey
import edu.pokemon.iut.pokedex.ui.pokemondetail.PokemonViewModel
import edu.pokemon.iut.pokedex.ui.pokemonlist.PokemonListViewModel

/**
 * Dependencies injection Module for accessing the ViewModel objects for the app
 * Add any new ViewModel here to allow it's access on any class who needs it.
 */
@Module
abstract class ViewModelModule {

    /**
     * Main factory to construct viewModel with non empty constructor.
     *
     * @param viewModelFactory an actual implementation of [ViewModelProvider.Factory] interface
     * @return an interface of [ViewModelProvider.Factory] who refers to the param.
     */
    @Binds
    internal abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    /**
     * Returns a ViewModel to manipulate a list of Pokemon
     *
     * @param pokemonListViewModel actual implementation of abstract class [ViewModel]
     * @return an instance of abstract class [ViewModel]
     */
    @Binds
    @IntoMap
    @ViewModelKey(PokemonListViewModel::class)
    internal abstract fun bindsPokemonListViewModel(pokemonListViewModel: PokemonListViewModel): ViewModel

    /**
     * Returns a ViewModel to manipulate a single Pokemon
     *
     * @param pokemonViewModel actual implementation of abstract class [ViewModel]
     * @return an instance of abstract class [ViewModel]
     */
    @Binds
    @IntoMap
    @ViewModelKey(PokemonViewModel::class)
    internal abstract fun bindsPokemonViewModel(pokemonViewModel: PokemonViewModel): ViewModel
}