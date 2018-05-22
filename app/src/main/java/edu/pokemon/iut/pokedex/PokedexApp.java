package edu.pokemon.iut.pokedex;

import android.app.Application;

import edu.pokemon.iut.pokedex.architecture.di.ApiModule;
import edu.pokemon.iut.pokedex.architecture.di.DaggerPokemonComponent;
import edu.pokemon.iut.pokedex.architecture.di.DatabaseModule;
import edu.pokemon.iut.pokedex.architecture.di.PokemonComponent;

public class PokedexApp extends Application {

    protected PokemonComponent pokemonComponent;
    protected static PokedexApp application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        //je créé mon githubcompoent, et le stock dans mon application
        pokemonComponent = DaggerPokemonComponent.builder()
                .databaseModule(new DatabaseModule(getApplicationContext()))
                .apiModule(new ApiModule())
                .build();
    }

    public static PokedexApp app() {
        return application;
    }

    //permet aux activités via .getApplication().appComponent() de récupérer le AppComponent
    public PokemonComponent component() {
        return pokemonComponent;
    }
}
