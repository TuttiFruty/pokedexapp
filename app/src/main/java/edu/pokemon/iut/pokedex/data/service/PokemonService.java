package edu.pokemon.iut.pokedex.data.service;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import edu.pokemon.iut.pokedex.data.model.Pokemon;
import edu.pokemon.iut.pokedex.data.model.Type;
import edu.pokemon.iut.pokedex.data.webservice.PokemonApi;
import edu.pokemon.iut.pokedex.data.webservice.PokemonApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonService {
    private PokemonApi pokemonApi;

    @Inject
    public PokemonService(PokemonApiClient pokemonApiClient) {
        pokemonApi = pokemonApiClient.getPokemonApi();
    }

    public Pokemon getPokemon(int pokemonId) {
        // refresh the data
        Response<Pokemon> response = null;
        try {
            response = pokemonApi.getPokemon(pokemonId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

            Pokemon pokemon = null;
        if (response != null && response.body() != null) {
            pokemon =  new Pokemon();
            pokemon.setName(response.body().getName());
            pokemon.setWeight(response.body().getWeight());
            pokemon.setHeight(response.body().getHeight());
            pokemon.setId(response.body().getId());
            pokemon.setBaseExperience(response.body().getBaseExperience());
            pokemon.setDefault(response.body().isDefault());
            pokemon.setOrder(response.body().getOrder());
            pokemon.setSprites(response.body().getSprites());
            pokemon.getSprites().setId(pokemon.getId());
            pokemon.setSpritesString(pokemon.getSprites().getFrontDefault());
            pokemon.setTypes(response.body().getTypes());
        }

        return pokemon;
    }

    public MutableLiveData<List<Pokemon>> getPokemons(int from, int to) {
        MutableLiveData<List<Pokemon>> data = new MutableLiveData<>();

        final List<Pokemon> list = new ArrayList<>();

        for(int i = from; i < to ; i++){
            pokemonApi.getPokemon(i).enqueue(new Callback<Pokemon>() {
                @Override
                public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                    list.add(response.body());
                    data.setValue(list);
                }

                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {
                    Log.e("PokemonRepository", "Error request : " + call.request().toString(), t);
                }
            });
        }

        data.setValue(list);

        return data;
    }
}
