package edu.pokemon.iut.pokedex.data.webservice;

import edu.pokemon.iut.pokedex.data.model.Pokemon;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokemonApi {

    @GET("pokemon/{pokemonid}")
    Call<Pokemon> getPokemon(@Path("pokemonid") int pokemonId);
}
