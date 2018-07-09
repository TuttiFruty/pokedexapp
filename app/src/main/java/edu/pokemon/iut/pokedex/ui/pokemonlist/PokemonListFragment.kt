package edu.pokemon.iut.pokedex.ui.pokemonlist

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import butterknife.BindView
import edu.pokemon.iut.pokedex.R
import edu.pokemon.iut.pokedex.architecture.BaseFragment
import edu.pokemon.iut.pokedex.data.model.Pokemon

/**
 * Fragment to show a List of Pokemon
 */
class PokemonListFragment : BaseFragment(), PokemonAdapter.CaptureListener {

    /* VIEWS */
    @BindView(R.id.rv_pokemon_list)
    lateinit var pokemonListView: RecyclerView

    /* ATTRIBUTES */
    private var viewModel: PokemonListViewModel? = null
    private var adapter: PokemonAdapter? = null
    private var rootView: View? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.pokemon_list_layout, null)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar(false, null)
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        pokemonListView!!.setHasFixedSize(true)

        // use a linear layout manager if in portrait or a grid layout manager in landscape or tablet view
        var orientation = Configuration.ORIENTATION_PORTRAIT
        if (activity != null && activity!!.resources != null) {
            orientation = activity!!.resources.configuration.orientation
        }
        val mLayoutManager: LinearLayoutManager
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = LinearLayoutManager(context)
        } else {
            //We change the number of columns shown if we are or not on a tablet
            if (navigationManager!!.isTabletNavigation) {
                mLayoutManager = GridLayoutManager(context, 2)
            } else {
                mLayoutManager = GridLayoutManager(context, 3)

            }
        }
        pokemonListView!!.layoutManager = mLayoutManager

        adapter = PokemonAdapter(context!!, navigationManager!!, this)
        pokemonListView!!.adapter = adapter
        if (pokemonListView!!.adapter != null) {
            pokemonListView!!.adapter!!.notifyDataSetChanged()
        }

        //Initialisation and observation of the ViewModel for this screen
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PokemonListViewModel::class.java)
        var query: CharSequence? = null
        if (arguments != null) {
            query = arguments!!.getCharSequence(KEY_SEARCH_QUERY, null)
        }
        viewModel!!.init(query)

        viewModel?.pokemons?.observe(this, Observer<List<Pokemon>>{ pokemonList ->
            adapter!!.setData(pokemonList!!)
        })

    }

    override fun onCapture(pokemon: Pokemon) {
        viewModel!!.capture(pokemon)
    }

    companion object {

        private val TAG = PokemonListFragment::class.java.simpleName

        /* BUNDLE KEYS */
        private val KEY_SEARCH_QUERY = "KEY_SEARCH_QUERY"

        /**
         * @param query to filter the pokemon list
         * @return newInstance of SampleFragment
         */
        fun newInstance(query: CharSequence?): PokemonListFragment {
            val pokemonListFragment = PokemonListFragment()

            val bundle = Bundle()
            bundle.putCharSequence(KEY_SEARCH_QUERY, query)

            pokemonListFragment.arguments = bundle
            return pokemonListFragment
        }
    }
}