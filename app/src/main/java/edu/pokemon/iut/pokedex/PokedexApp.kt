package edu.pokemon.iut.pokedex

import android.app.Application

import edu.pokemon.iut.pokedex.architecture.di.ApiModule
import edu.pokemon.iut.pokedex.architecture.di.DaggerPokemonComponent
import edu.pokemon.iut.pokedex.architecture.di.DatabaseModule
import edu.pokemon.iut.pokedex.architecture.di.PokemonComponent

/**
 * Core for the Dagger Dependency injections, and Database migration
 */
class PokedexApp : Application() {

    private var pokemonComponent: PokemonComponent? = null

    override fun onCreate() {
        super.onCreate()

        //Init of injection dependency
        application = this
        pokemonComponent = DaggerPokemonComponent.builder()
                .databaseModule(DatabaseModule(applicationContext))
                .apiModule(ApiModule())
                .build()
    }

    //allows activities to retrieve AppComponent thanks to .getApplication().appComponent()
    fun component(): PokemonComponent? {
        return pokemonComponent
    }

    companion object {
        private var application: PokedexApp? = null

        fun app(): PokedexApp? {
            return application
        }
    }
}
