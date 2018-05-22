package edu.pokemon.iut.pokedex.architecture.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.pokemon.iut.pokedex.architecture.NavigationManager;

@Module
public class NavigationModule {
    @Provides
    @Singleton
    public NavigationManager providesNavigationManager() {
        return new NavigationManager();
    }
}
