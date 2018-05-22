package edu.pokemon.iut.pokedex.data.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.pokemon.iut.pokedex.data.dao.PokemonDao;
import edu.pokemon.iut.pokedex.data.model.Pokemon;
import edu.pokemon.iut.pokedex.data.service.PokemonService;

@Singleton
public class PokemonRepositoryImpl implements PokemonRepository {

    private final PokemonService pokemonService;
    private final PokemonDao pokemonDao;
    private final Executor executor;


    @Inject
    public PokemonRepositoryImpl(PokemonService pokemonService, PokemonDao pokemonDao, Executor executor) {
        this.pokemonService = pokemonService;
        this.pokemonDao = pokemonDao;
        this.executor = executor;
    }

    @Override
    public LiveData<List<Pokemon>> getPokemons() {
        refreshPokemon(1, 151);

        return pokemonDao.loadAll();
    }

    @Override
    public LiveData<Pokemon> getPokemon(int pokemonId) {
        refreshPokemon(pokemonId, pokemonId);

        return pokemonDao.load(pokemonId);
    }

    private void refreshPokemon(int start, int end) {
        for (int idPokemon = start; idPokemon <= end; idPokemon++) {
            int finalIdPokemon = idPokemon;
            // running in a background thread
            // check if user was fetched recently
            executor.execute(() -> {
                boolean pokemonExists = pokemonDao.hasPokemon(finalIdPokemon) > 0;
                if (!pokemonExists) {
                    // refresh the data
                    Pokemon response = pokemonService.getPokemon(finalIdPokemon);

                    if (response != null) {
                        // Update the database.The LiveData will automatically refresh so
                        // we don't need to do anything else here besides updating the database
                        pokemonDao.save(response);
                    }
                }
            });
        }
    }
}
