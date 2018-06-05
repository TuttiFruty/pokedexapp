package edu.pokemon.iut.pokedex.ui.pokemondetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionInflater;
import android.support.v4.view.GestureDetectorCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import javax.inject.Inject;

import butterknife.BindView;
import edu.pokemon.iut.pokedex.PokedexApp;
import edu.pokemon.iut.pokedex.R;
import edu.pokemon.iut.pokedex.architecture.BaseFragment;
import edu.pokemon.iut.pokedex.architecture.NavigationManager;
import edu.pokemon.iut.pokedex.architecture.listener.PokemonGestureListener;
import edu.pokemon.iut.pokedex.data.model.Pokemon;
import edu.pokemon.iut.pokedex.data.model.Type;

/**
 * Example Fragment
 * Created by becze on 11/25/2015.
 */
public class PokemonDetailFragment extends BaseFragment implements PokemonGestureListener.Listener {

    private static final String TAG = PokemonDetailFragment.class.getSimpleName();
    private static final String KEY_POKEMON_ID = "KEY_POKEMON_ID";
    private static final String KEY_TRANSITION_NAME = "KEY_TRANSITION_NAME";
    private static final String KEY_SHOW_NAVIGATION = "KEY_SHOW_NAVIGATION";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    PokemonViewModel viewModel;
    @Inject
    public NavigationManager navigationManager;

    @BindView(R.id.cl_pokemon_detail)
    View constraintLayoutPokemonDetail;
    @BindView(R.id.iv_pokemon_logo)
    ImageView imageViewPokemonLogo;
    @BindView(R.id.tv_pokemon_numero)
    TextView textViewPokemonId;
    @BindView(R.id.tv_pokemon_name)
    TextView textViewPokemonName;
    @BindView(R.id.tv_pokemon_base_exp)
    TextView textViewPokemonBaseExp;
    @BindView(R.id.tv_pokemon_height)
    TextView textViewPokemonHeight;
    @BindView(R.id.tv_pokemon_weight)
    TextView textViewPokemonWeight;
    @BindView(R.id.ll_pokemon_types)
    LinearLayout linearLayoutpokemonTypes;

    private int pokemonId;
    private View mRootView;
    private boolean isNavigationShown = true;
    private int idMaxPokemon;

    /**
     * @return newInstance of SampleFragment
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
        postponeEnterTransition();
        PokedexApp.app().component().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.pokemon_detail_layout, null);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String transitionName = "NO_TRANSITION";

        if (getArguments() != null) {
            isNavigationShown = getArguments().getBoolean(KEY_SHOW_NAVIGATION, true);
            transitionName = getArguments().getString(KEY_TRANSITION_NAME, "NO_TRANSITION");
        }

        initActionBar(isNavigationShown, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageViewPokemonLogo.setTransitionName(transitionName);
        }

        if (isNavigationShown) {
            PokemonGestureListener pokemonGestureListener = new PokemonGestureListener(this,null,this.getContext());
            constraintLayoutPokemonDetail.setOnTouchListener(pokemonGestureListener);
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PokemonViewModel.class);
        viewModel.init(this.pokemonId);
        viewModel.getPokemon().observe(this, this::initView);
        viewModel.getIdMaxPokemon().observe(this, integer -> idMaxPokemon = integer!=null?integer:0);
    }

    private void initView(Pokemon pokemon) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .dontAnimate();
        Glide.with(getContext())
                .load(pokemon.getSpritesString())
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }
                })
                .into(imageViewPokemonLogo);

        setTitle(isNavigationShown ? pokemon.getName() : null);

        textViewPokemonId.setText("No. " + pokemon.getStringId());
        textViewPokemonName.setText(pokemon.getName());
        textViewPokemonBaseExp.setText(pokemon.getStringBaseExp() + " exp");
        textViewPokemonHeight.setText(pokemon.getStringHeight() + " m");
        textViewPokemonWeight.setText(pokemon.getStringWeight() + " kg");

        linearLayoutpokemonTypes.removeAllViews();
        for (Type type : pokemon.getTypes()) {
            TextView textViewType = new TextView(getContext());
            textViewType.setText(type.getType().getName());
            linearLayoutpokemonTypes.addView(textViewType);
        }

    }

    @Override
    public void onFling(int direction) {
        if(direction == PokemonGestureListener.LEFT){
            if(pokemonId != 1) {
                navigationManager.startPokemonDetail(pokemonId - 1, imageViewPokemonLogo, true);
            }
        }else if (direction == PokemonGestureListener.RIGHT){
            if(pokemonId != idMaxPokemon){
                navigationManager.startPokemonDetail(pokemonId + 1, imageViewPokemonLogo,true);
            }
        }
    }
}