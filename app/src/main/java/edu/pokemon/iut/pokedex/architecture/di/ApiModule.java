package edu.pokemon.iut.pokedex.architecture.di;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.pokemon.iut.pokedex.data.webservice.PokemonApiClient;

@Module
public class ApiModule {
    @Provides
    public PokemonApiClient provicePokemonApiClient(){
        return new PokemonApiClient();
    }

    @Provides
    @Singleton
    public Executor provideMultiThreadExecutor() {
        return Executors.newFixedThreadPool(10);
    }
}
