package edu.pokemon.iut.pokedex.ui.pokemondetail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import edu.pokemon.iut.pokedex.PokedexApp;
import edu.pokemon.iut.pokedex.R;
import edu.pokemon.iut.pokedex.architecture.BaseFragment;
import edu.pokemon.iut.pokedex.data.model.Pokemon;
import edu.pokemon.iut.pokedex.data.model.Type;

/**
 * Example Fragment
 * Created by becze on 11/25/2015.
 */
public class PokemonDetailFragment extends BaseFragment {

    private static final String TAG = PokemonDetailFragment.class.getSimpleName();
    private static final String KEY_POKEMON_ID = "KEY_POKEMON_ID";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    PokemonViewModel viewModel;

    @BindView(R.id.iv_pokemon_logo)
    ImageView imageViewPokemonLogo;
    @BindView(R.id.tv_pokemon_id)
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

    /**
     * @return newInstance of SampleFragment
     */
    public static PokemonDetailFragment newInstance(int pokemonId) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POKEMON_ID, pokemonId);
        PokemonDetailFragment pokemonDetailFragment = new PokemonDetailFragment();
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
        PokedexApp.app().component().inject(this);
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

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PokemonViewModel.class);
        viewModel.init(this.pokemonId);
        viewModel.getPokemon().observe(this, this::initView);
    }

    private void initView(Pokemon pokemon) {
        Glide.with(getContext()).load(pokemon.getSpritesString()).into(imageViewPokemonLogo);
        setTitle(pokemon.getName());
        textViewPokemonId.setText(pokemon.getStringId());
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
}