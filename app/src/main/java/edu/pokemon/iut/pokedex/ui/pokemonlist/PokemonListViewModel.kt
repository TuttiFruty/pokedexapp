package edu.pokemon.iut.pokedex.ui.pokemonlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import edu.pokemon.iut.pokedex.data.model.Pokemon
import edu.pokemon.iut.pokedex.data.repository.PokemonRepository

/**
 * ViewModel for a list of Pokemon
 */
class PokemonListViewModel
/**
 * Dagger injects automatically all parameters<br></br>
 * The ViewModel need the [PokemonRepository] to request what it needs
 *
 * @param pokemonRepository [PokemonRepository]
 */
@Inject
constructor(private val pokemonRepository: PokemonRepository) : ViewModel() {
    /**
     * @return a [LiveData] of a [List] of [Pokemon] that can be observe to upload the view automatically in case of change
     */
    var pokemons: LiveData<List<Pokemon>>? = null
        private set

    /**
     * Init the ViewModel for a given query(based on pokemon name), if empty or null you get all pokemon
     *
     * @param query to filter which pokemon to retrieve
     */
    fun init(query: CharSequence?) {
        if (this.pokemons != null) {
            return
        }
        pokemons = pokemonRepository.getPokemons(query)
    }

    /**
     * Allow to update the capture status of a pokemon
     * @param pokemon to update
     */
    fun capture(pokemon: Pokemon) {
        pokemonRepository.capture(pokemon.capture())
    }
}
