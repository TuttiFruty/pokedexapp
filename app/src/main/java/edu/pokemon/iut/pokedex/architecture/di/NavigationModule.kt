package edu.pokemon.iut.pokedex.architecture.di

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import edu.pokemon.iut.pokedex.architecture.NavigationManager

/**
 * Dependencies injection Module for Navigation
 */
@Module
class NavigationModule {

    /**
     * Give access to the navigation manager that provide methods to navigate through the app
     *
     * @return an instance of [NavigationManager]
     */
    @Provides
    @Singleton
    fun providesNavigationManager(): NavigationManager {
        return NavigationManager()
    }
}
