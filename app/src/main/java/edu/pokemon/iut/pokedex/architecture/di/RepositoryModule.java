package edu.pokemon.iut.pokedex.architecture.di;

import dagger.Binds;
import dagger.Module;
import edu.pokemon.iut.pokedex.data.repository.PokemonRepository;
import edu.pokemon.iut.pokedex.data.repository.PokemonRepositoryImpl;

@Module
public abstract class RepositoryModule {
    @Binds
    abstract PokemonRepository bindsPokemonRepository(PokemonRepositoryImpl pokemonRepositoryImpl);
}
