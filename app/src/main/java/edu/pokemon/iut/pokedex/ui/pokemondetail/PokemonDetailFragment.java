package edu.pokemon.iut.pokedex.ui.pokemondetail;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import edu.pokemon.iut.pokedex.PokedexApp;
import edu.pokemon.iut.pokedex.R;
import edu.pokemon.iut.pokedex.architecture.BaseFragment;
import edu.pokemon.iut.pokedex.architecture.listener.PokemonGestureListener;
import edu.pokemon.iut.pokedex.data.model.Pokemon;
import edu.pokemon.iut.pokedex.data.model.Type;

/**
 * Fragment to show Unique Pokemon
 */
@SuppressWarnings("WeakerAccess")
public class PokemonDetailFragment extends BaseFragment implements PokemonGestureListener.Listener {

    private static final String TAG = PokemonDetailFragment.class.getSimpleName();

    /* BUNDLE KEYS */
    private static final String KEY_POKEMON_ID = "KEY_POKEMON_ID";
    private static final String KEY_TRANSITION_NAME = "KEY_TRANSITION_NAME";
    private static final String KEY_SHOW_NAVIGATION = "KEY_SHOW_NAVIGATION";

    /* VIEWS */
    @BindView(R.id.cl_pokemon_detail)
    protected View constraintLayoutPokemonDetail;
    @BindView(R.id.iv_pokemon_logo)
    protected ImageView imageViewPokemonLogo;
    @BindView(R.id.tv_pokemon_numero)
    protected TextView textViewPokemonId;
    @BindView(R.id.tv_pokemon_name)
    protected TextView textViewPokemonName;
    @BindView(R.id.tv_pokemon_base_exp)
    protected TextView textViewPokemonBaseExp;
    @BindView(R.id.tv_pokemon_height)
    protected TextView textViewPokemonHeight;
    @BindView(R.id.tv_pokemon_weight)
    protected TextView textViewPokemonWeight;
    @BindView(R.id.ll_pokemon_types)
    protected LinearLayout linearLayoutPokemonTypes;

    /* ATTRIBUTES */
    private int pokemonId;
    private View rootView;
    private boolean isNavigationShown = true;
    private int idMaxPokemon;

    /**
     * @param pokemonId         id of the pokemon shown
     * @param transitionName    id of the transition view
     * @param isNavigationShown true if we show the arrow, false otherwise
     * @return newInstance of PokemonDetailFragment
     */
    public static PokemonDetailFragment newInstance(int pokemonId, String transitionName, boolean isNavigationShown) {
        PokemonDetailFragment pokemonDetailFragment = new PokemonDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POKEMON_ID, pokemonId);
        bundle.putString(KEY_TRANSITION_NAME, transitionName);
        bundle.putBoolean(KEY_SHOW_NAVIGATION, isNavigationShown);

        pokemonDetailFragment.setArguments(bundle);
        return pokemonDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            this.pokemonId = getArguments().getInt(KEY_POKEMON_ID);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Let's know the fragment that we may have a transition to show
        postponeEnterTransition();
        PokedexApp.app().component().inject(this);
        //The sharedElement between screens is only available for version above of LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.pokemon_detail_layout, null);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String transitionName = "NO_TRANSITION";

        //Retrieve of the different arguments passed when creating the instance
        if (getArguments() != null) {
            isNavigationShown = getArguments().getBoolean(KEY_SHOW_NAVIGATION, true);
            transitionName = getArguments().getString(KEY_TRANSITION_NAME, "NO_TRANSITION");
        }

        initActionBar(isNavigationShown, null);

        //The sharedElement between screens is only available for version above of LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageViewPokemonLogo.setTransitionName(transitionName);
        }

        //If we can show the navigation, we can then swipe between pokemons
        if (isNavigationShown) {
            PokemonGestureListener pokemonGestureListener = new PokemonGestureListener(this, null, this.getContext());
            constraintLayoutPokemonDetail.setOnTouchListener(pokemonGestureListener);
        }

        //Initialisation and observation of the ViewModel for this screen
        PokemonViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(PokemonViewModel.class);
        viewModel.init(this.pokemonId);
        //Once we get the pokemon from the ViewModel or if he is updated we call initView
        viewModel.getPokemon().observe(this, this::initView);
        //Once we get the number max of pokemon from the ViewModel or if he is updated we call update the value
        //That allow us to not swipe further than the last one in database
        viewModel.getIdMaxPokemon().observe(this, integer -> idMaxPokemon = integer != null ? integer : 0);
    }

    /**
     * Initialise the view with the given pokemon
     *
     * @param pokemon {@link Pokemon} to show
     */
    private void initView(Pokemon pokemon) {
        if(pokemon != null) {
            //To be able to use the Shared element we need to disable animation from Glide
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .dontAnimate();
            if (getContext() != null) {
                //Loading of the Image of the pokemon
                Glide.with(getContext())
                        .load(pokemon.getSpritesString())
                        .apply(options)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //Let the fragment run is transition animation from postponeEnterTransition();
                                startPostponedEnterTransition();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                //Let the fragment run is transition animation from postponeEnterTransition();
                                startPostponedEnterTransition();
                                return false;
                            }
                        })
                        .into(imageViewPokemonLogo);
            }

            //If we can Navigate between pokemons we show his name on the actionBar, else we keep the default name
            setTitle(isNavigationShown ? pokemon.getName() : null);

            /* Mapping the info from ViewModel into view */
            textViewPokemonId.setText(getString(R.string.number, pokemon.getId()));
            textViewPokemonName.setText(pokemon.getName());
            textViewPokemonBaseExp.setText(getString(R.string.exp, pokemon.getBaseExperience()));
            textViewPokemonHeight.setText(getString(R.string.height, pokemon.getHeight()));
            textViewPokemonWeight.setText(getString(R.string.weight, pokemon.getWeight()));

            /* Avoid multiplication of types from ViewModel triggering to much time */
            linearLayoutPokemonTypes.removeAllViews();
            for (Type type : pokemon.getTypes()) {
                TextView textViewType = new TextView(getContext());
                textViewType.setText(type.getType().getName());
                linearLayoutPokemonTypes.addView(textViewType);
            }
        }
    }

    @Override
    public void onSwipe(int direction) {
        if (direction == PokemonGestureListener.LEFT) {
            //If the current pokemon is the first one we don't take account of the swipe
            if (pokemonId != 1) {
                navigationManager.startPokemonDetail(pokemonId - 1, imageViewPokemonLogo, true);
            }
        } else if (direction == PokemonGestureListener.RIGHT) {
            //If the current pokemon is the last one we don't take account of the swipe
            if (pokemonId != idMaxPokemon) {
                navigationManager.startPokemonDetail(pokemonId + 1, imageViewPokemonLogo, true);
            }
        }
    }
}