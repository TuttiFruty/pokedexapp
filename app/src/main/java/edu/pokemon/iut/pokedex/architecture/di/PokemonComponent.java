package edu.pokemon.iut.pokedex.architecture.di;

import javax.inject.Singleton;

import dagger.Component;
import edu.pokemon.iut.pokedex.NavigationActivity;
import edu.pokemon.iut.pokedex.architecture.BaseActivity;
import edu.pokemon.iut.pokedex.ui.pokemondetail.PokemonDetailFragment;
import edu.pokemon.iut.pokedex.ui.pokemonlist.PokemonListFragment;

/**
 * Main component of the app for Dependency Injection
 * Regroup all the modules needed for each Class.
 * Add a line with any new class who needs injection to access the different object provide by the modules.
 */
@Singleton
@Component(modules = {RepositoryModule.class, ViewModelModule.class, ApiModule.class, DatabaseModule.class, NavigationModule.class})
public interface PokemonComponent {
    void inject(NavigationActivity navigationActivity);

    void inject(BaseActivity baseActivity);

    void inject(PokemonListFragment pokemonListFragment);

    void inject(PokemonDetailFragment pokemonDetailFragment);
}
