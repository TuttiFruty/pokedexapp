package edu.pokemon.iut.pokedex.architecture.di

import java.util.concurrent.Executor
import java.util.concurrent.Executors

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import edu.pokemon.iut.pokedex.data.webservice.PokemonApiClient

/**
 * Dependencies injection Module for Api calling
 */
@Module
class ApiModule {
    /**
     * The client allow to request data about Pokemon on Pokeapi.co
     *
     * @return an instance of [PokemonApiClient]
     */
    @Provides
    fun providePokemonApiClient(): PokemonApiClient {
        return PokemonApiClient()
    }

    /**
     * Singleton for Multi Thread execution.
     *
     * @return an instance of [Executor]
     */
    @Provides
    @Singleton
    fun provideMultiThreadExecutor(): Executor {
        return Executors.newFixedThreadPool(10)
    }
}
