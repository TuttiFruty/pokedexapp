package edu.pokemon.iut.pokedex.ui.pokemondetail

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

import java.util.ArrayList

import butterknife.BindView
import edu.pokemon.iut.pokedex.PokedexApp
import edu.pokemon.iut.pokedex.R
import edu.pokemon.iut.pokedex.architecture.BaseFragment
import edu.pokemon.iut.pokedex.architecture.NavigationManager
import edu.pokemon.iut.pokedex.architecture.listener.PokemonGestureListener
import edu.pokemon.iut.pokedex.data.model.Pokemon
import edu.pokemon.iut.pokedex.data.model.Type

/**
 * Fragment to show Unique Pokemon
 */
class PokemonDetailFragment : BaseFragment(), PokemonGestureListener.Listener {

    /* VIEWS */
    @BindView(R.id.cl_pokemon_detail)
    lateinit var constraintLayoutPokemonDetail: View
    @BindView(R.id.iv_pokemon_logo)
    lateinit var imageViewPokemonLogo: ImageView
    @BindView(R.id.iv_pokemon_capture)
    lateinit var imageViewPokemonCapture: ImageView
    @BindView(R.id.cv_pokemon_capture)
    lateinit var imageViewPokemonCaptureShadow: View
    @BindView(R.id.tv_pokemon_numero)
    lateinit var textViewPokemonId: TextView
    @BindView(R.id.tv_pokemon_name)
    lateinit var textViewPokemonName: TextView
    @BindView(R.id.tv_pokemon_base_exp)
    lateinit var textViewPokemonBaseExp: TextView
    @BindView(R.id.tv_pokemon_height)
    lateinit var textViewPokemonHeight: TextView
    @BindView(R.id.tv_pokemon_weight)
    lateinit var textViewPokemonWeight: TextView
    @BindView(R.id.ll_pokemon_types)
    lateinit var linearLayoutPokemonTypes: LinearLayout

    /* ATTRIBUTES */
    private var pokemonId: Int = 0
    private var rootView: View? = null
    private var isNavigationShown = true
    private var idMaxPokemon: Int = 0

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (arguments != null) {
            this.pokemonId = arguments!!.getInt(KEY_POKEMON_ID)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Let's know the fragment that we may have a transition to show
        postponeEnterTransition()
        PokedexApp.app()!!.component()!!.inject(this)
        //The sharedElement between screens is only available for version above of LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.pokemon_detail_layout, null)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var transitionNames: ArrayList<String>? = null

        //Retrieve of the different arguments passed when creating the instance
        if (arguments != null) {
            isNavigationShown = arguments!!.getBoolean(KEY_SHOW_NAVIGATION, true)
            transitionNames = arguments!!.getStringArrayList(KEY_TRANSITION_NAME)
        }

        initActionBar(isNavigationShown, null)

        //The sharedElement between screens is only available for version above of LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (transitionNames != null) {
                for (transitionName in transitionNames) {
                    if (transitionName.contains(NavigationManager.IMAGE_VIEW_POKEMON_LOGO)) {
                        imageViewPokemonLogo!!.transitionName = transitionName
                    } else if (transitionName.contains(NavigationManager.IMAGE_VIEW_POKEMON_CAPTURE)) {
                        imageViewPokemonCapture!!.transitionName = transitionName
                    } else if (transitionName.contains(NavigationManager.IMAGE_VIEW_POKEMON_SHADOW)) {
                        imageViewPokemonCaptureShadow!!.transitionName = transitionName
                    }
                }
            }
        }

        //If we can show the navigation, we can then swipe between pokemons
        if (isNavigationShown) {
            val pokemonGestureListener = PokemonGestureListener(this, null, this.context!!)
            constraintLayoutPokemonDetail!!.setOnTouchListener(pokemonGestureListener)
        }

        //Initialisation and observation of the ViewModel for this screen
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(PokemonViewModel::class.java)
        viewModel.init(this.pokemonId)
        //Once we get the pokemon from the ViewModel or if he is updated we call initView
        viewModel.pokemon!!.observe(this, Observer<Pokemon>{ pokemon -> initView(pokemon, viewModel) })
        //Once we get the number max of pokemon from the ViewModel or if he is updated we call update the value
        //That allow us to not swipe further than the last one in database
        viewModel.idMaxPokemon!!.observe(this, Observer<Int>{ integer -> idMaxPokemon = integer ?: 0 })
    }

    /**
     * Initialise the view with the given pokemon
     *
     * @param pokemon [Pokemon] to show
     * @param viewModel allows access to capture method
     */
    private fun initView(pokemon: Pokemon?, viewModel: PokemonViewModel) {
        if (pokemon != null) {
            //To be able to use the Shared element we need to disable animation from Glide
            val options = RequestOptions()
                    .centerCrop()
                    .dontAnimate()
            if (context != null) {
                //Loading of the Image of the pokemon
                Glide.with(context!!)
                        .load(pokemon.spritesString)
                        .apply(options)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                //Let the fragment run is transition animation from postponeEnterTransition();
                                startPostponedEnterTransition()
                                return false
                            }

                            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                //Let the fragment run is transition animation from postponeEnterTransition();
                                startPostponedEnterTransition()
                                return false
                            }
                        })
                        .into(imageViewPokemonLogo!!)
            }

            //If we can Navigate between pokemons we show his name on the actionBar, else we keep the default name
            setTitle(if (isNavigationShown) pokemon.name else null)

            /* Mapping the info from ViewModel into view */
            textViewPokemonId!!.text = getString(R.string.number, pokemon.id)
            textViewPokemonName!!.text = pokemon.name
            textViewPokemonBaseExp!!.text = getString(R.string.exp, pokemon.baseExperience)
            textViewPokemonHeight!!.text = getString(R.string.height, pokemon.height)
            textViewPokemonWeight!!.text = getString(R.string.weight, pokemon.weight)

            //If the pokemon is captured we use the full pokeball, otherwise the empty one
            imageViewPokemonCapture!!.setImageResource(if (pokemon.isCapture) R.drawable.ic_launcher_pokeball else R.drawable.ic_launcher_pokeball_empty)
            imageViewPokemonCapture!!.setOnClickListener { view -> viewModel.capture(pokemon) }
            /* Avoid multiplication of types from ViewModel triggering to much time */
            linearLayoutPokemonTypes!!.removeAllViews()
            for (type in pokemon.types!!) {
                val textViewType = TextView(context)
                textViewType.text = type?.type?.name
                linearLayoutPokemonTypes!!.addView(textViewType)
            }
        }
    }

    override fun onSwipe(direction: Int) {
        val transitionView = ArrayList<View>()
        transitionView.add(imageViewPokemonLogo!!)
        transitionView.add(imageViewPokemonCapture!!)

        if (direction == PokemonGestureListener.LEFT) {
            //If the current pokemon is the first one we don't take account of the swipe
            if (pokemonId != 1) {
                navigationManager!!.startPokemonDetail(pokemonId - 1, transitionView, true)
            }
        } else if (direction == PokemonGestureListener.RIGHT) {
            //If the current pokemon is the last one we don't take account of the swipe
            if (pokemonId != idMaxPokemon) {
                navigationManager!!.startPokemonDetail(pokemonId + 1, transitionView, true)
            }
        }
    }

    companion object {

        private val TAG = PokemonDetailFragment::class.java.simpleName

        /* BUNDLE KEYS */
        private val KEY_POKEMON_ID = "KEY_POKEMON_ID"
        private val KEY_TRANSITION_NAME = "KEY_TRANSITION_NAME"
        private val KEY_SHOW_NAVIGATION = "KEY_SHOW_NAVIGATION"

        /**
         * @param pokemonId         id of the pokemon shown
         * @param transitionView    id of the transition view
         * @param isNavigationShown true if we show the arrow, false otherwise
         * @return newInstance of PokemonDetailFragment
         */
        fun newInstance(pokemonId: Int, transitionView: List<View>, isNavigationShown: Boolean): PokemonDetailFragment {
            val pokemonDetailFragment = PokemonDetailFragment()
            val transitionNames = ArrayList<String>()
            for (view in transitionView) {
                transitionNames.add(view.transitionName)
            }
            val bundle = Bundle()
            bundle.putInt(KEY_POKEMON_ID, pokemonId)
            bundle.putStringArrayList(KEY_TRANSITION_NAME, transitionNames)
            bundle.putBoolean(KEY_SHOW_NAVIGATION, isNavigationShown)

            pokemonDetailFragment.arguments = bundle
            return pokemonDetailFragment
        }
    }
}