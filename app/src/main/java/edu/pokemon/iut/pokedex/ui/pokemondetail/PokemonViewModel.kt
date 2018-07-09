package edu.pokemon.iut.pokedex.ui.pokemondetail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

import javax.inject.Inject

import edu.pokemon.iut.pokedex.data.model.Pokemon
import edu.pokemon.iut.pokedex.data.repository.PokemonRepository

/**
 * ViewModel for One unique Pokemon
 */
class PokemonViewModel
/**
 * Dagger injects automatically all parameters<br></br>
 * The ViewModel need the [PokemonRepository] to request what it needs
 *
 * @param pokemonRepository [PokemonRepository]
 */
@Inject
constructor(private val pokemonRepository: PokemonRepository) : ViewModel() {
    /**
     * @return a [LiveData] of [Pokemon] that can be observe to upload the view automatically in case of change
     */
    var pokemon: LiveData<Pokemon>? = null
        private set
    /**
     * @return a [LiveData] of [Integer] to know the number of pokemon which exist for the app
     */
    var idMaxPokemon: LiveData<Int>? = null
        private set

    /**
     * Init the ViewModel for a given Pokemon ID
     *
     * @param pokemonId for the pokemon to Observe
     */
    fun init(pokemonId: Int) {
        //If we already load the pokemon, no need to do it again, the observation will allow us to update
        if (this.pokemon != null) {
            return
        }
        pokemon = pokemonRepository.getPokemon(pokemonId)
        idMaxPokemon = pokemonRepository.numberOfPokemon
    }

    /**
     * Allow to update the capture status of a pokemon
     * @param pokemon to update
     */
    fun capture(pokemon: Pokemon) {
        pokemonRepository.capture(pokemon.capture())
    }
}
