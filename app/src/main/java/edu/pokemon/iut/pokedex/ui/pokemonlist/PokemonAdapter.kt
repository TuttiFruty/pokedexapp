package edu.pokemon.iut.pokedex.ui.pokemonlist

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import java.util.ArrayList

import edu.pokemon.iut.pokedex.R
import edu.pokemon.iut.pokedex.architecture.NavigationManager
import edu.pokemon.iut.pokedex.data.model.Pokemon

/**
 * Custom adapter to show each pokemon in a single line view
 */
class PokemonAdapter internal constructor(private val context: Context, private val navigationManager: NavigationManager, private val captureListener: CaptureListener) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {
    private var dataSet: List<Pokemon>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val layoutIdForListItem = R.layout.pokemon_line_view
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(layoutIdForListItem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /* Mapping of the data into the view*/
        val pokemon = dataSet!![position]
        holder.pokemonNumber.text = context.getString(R.string.number, pokemon.id)
        holder.pokemonName.text = pokemon.name

        //If the pokemon is captured we use the full pokeball, otherwise the empty one
        holder.pokemonCapture.setImageResource(if (pokemon.isCapture) R.drawable.ic_launcher_pokeball else R.drawable.ic_launcher_pokeball_empty)

        val options = RequestOptions()
                .centerCrop()
        Glide.with(context)
                .load(pokemon.spritesString)
                .apply(options)
                .into(holder.pokemonLogo)

        //Preparation for the transition animation between fragments
        ViewCompat.setTransitionName(holder.pokemonLogo, pokemon.name + NavigationManager.IMAGE_VIEW_POKEMON_LOGO)
        ViewCompat.setTransitionName(holder.pokemonCapture, pokemon.name + NavigationManager.IMAGE_VIEW_POKEMON_CAPTURE)
        ViewCompat.setTransitionName(holder.pokemonCaptureShadow, pokemon.name + NavigationManager.IMAGE_VIEW_POKEMON_SHADOW)

        val listTransitionView = ArrayList<View>()
        listTransitionView.add(holder.pokemonLogo)
        listTransitionView.add(holder.pokemonCapture)
        listTransitionView.add(holder.pokemonCaptureShadow)

        /* Init of the listeners */
        holder.pokemonLine.setOnClickListener { v -> navigationManager.startPokemonDetail(pokemon.id, listTransitionView, false) }
        holder.pokemonCapture.setOnClickListener { v -> captureListener.onCapture(pokemon) }
    }

    override fun getItemCount(): Int {
        return if (dataSet != null) {
            dataSet!!.size
        } else {
            0
        }
    }

    fun setData(data: List<Pokemon>) {
        this.dataSet = data
        notifyDataSetChanged()
    }

    interface CaptureListener {
        fun onCapture(pokemon: Pokemon)
    }

    /**
     * Inner ViewHolder for the Pokemon's view
     */
    class ViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        internal val pokemonName: TextView
        internal val pokemonNumber: TextView
        internal val pokemonLogo: ImageView
        internal val pokemonLine: View
        internal val pokemonCapture: ImageView
        internal val pokemonCaptureShadow: View

        init {
            pokemonName = v.findViewById(R.id.tv_pokemon_name)
            pokemonNumber = v.findViewById(R.id.tv_pokemon_number)
            pokemonLogo = v.findViewById(R.id.iv_pokemon_logo)
            pokemonLine = v.findViewById(R.id.cl_pokemon_line)
            pokemonCapture = v.findViewById(R.id.iv_pokemon_capture)
            pokemonCaptureShadow = v.findViewById(R.id.cv_pokemon_capture)
        }
    }
}
