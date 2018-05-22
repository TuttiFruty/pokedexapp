package edu.pokemon.iut.pokedex.ui.pokemonlist;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import edu.pokemon.iut.pokedex.NavigationActivity;
import edu.pokemon.iut.pokedex.architecture.NavigationManager;
import edu.pokemon.iut.pokedex.PokedexApp;
import edu.pokemon.iut.pokedex.R;
import edu.pokemon.iut.pokedex.architecture.BaseFragment;

/**
 * Example Fragment
 * Created by becze on 11/25/2015.
 */
public class PokemonListFragment extends BaseFragment {

    private static final String TAG = PokemonListFragment.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    PokemonListViewModel viewModel;
    @Inject
    public NavigationManager navigationManager;

    @BindView(R.id.rv_pokemon_list)
    public RecyclerView pokemonListView;
    private LinearLayoutManager mLayoutManager;
    private PokemonAdapter mAdapter;

    /**
     * @return newInstance of SampleFragment
     */
    public static PokemonListFragment newInstance() {
        return new PokemonListFragment();
    }

    private View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PokedexApp.app().component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.pokemon_list_layout, null);
        }

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initActionBar(false, "Liste Pokemon");
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        pokemonListView.setHasFixedSize(true);

        // use a linear layout manager
        int orientation = getActivity().getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            mLayoutManager = new LinearLayoutManager(getContext());
        }else{
            mLayoutManager = new GridLayoutManager(getContext(), 4);
        }

        pokemonListView.setLayoutManager(mLayoutManager);
        mAdapter = new PokemonAdapter(getContext(), navigationManager);
        pokemonListView.setAdapter(mAdapter);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PokemonListViewModel.class);
        viewModel.init();
        viewModel.getPokemons().observe(this, pokemonList -> {
            // specify an adapter (see also next example)
            mAdapter.setData(pokemonList);
        });

    }
}