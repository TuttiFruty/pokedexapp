package edu.pokemon.iut.pokedex.data.webservice;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokemonApiClient
{
    private final Retrofit retrofit;

    public PokemonApiClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        //TODO okHttpClientBuilder add headers ?
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://pokeapi.co/api/v2/")
                .client(okHttpClientBuilder.build())
                .build();
    }

    public PokemonApi getPokemonApi() {
        return retrofit.create(PokemonApi.class);
    }
}
