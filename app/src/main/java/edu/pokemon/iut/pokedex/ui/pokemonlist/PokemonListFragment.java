package edu.pokemon.iut.pokedex.ui.pokemonlist;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import edu.pokemon.iut.pokedex.R;
import edu.pokemon.iut.pokedex.architecture.BaseFragment;
import edu.pokemon.iut.pokedex.data.model.Pokemon;

/**
 * Example Fragment
 * Created by becze on 11/25/2015.
 */
@SuppressWarnings("WeakerAccess")
public class PokemonListFragment extends BaseFragment implements PokemonAdapter.CaptureListener {

    private static final String TAG = PokemonListFragment.class.getSimpleName();
    private static final String KEY_SEARCH_QUERY = "KEY_SEARCH_QUERY";
    @BindView(R.id.rv_pokemon_list)
    protected RecyclerView pokemonListView;
    private PokemonListViewModel viewModel;
    private PokemonAdapter mAdapter;
    private View mRootView;

    /**
     * @param query to filter the pokemon list
     * @return newInstance of SampleFragment
     */
    public static PokemonListFragment newInstance(CharSequence query) {
        PokemonListFragment pokemonListFragment = new PokemonListFragment();

        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_SEARCH_QUERY, query);

        pokemonListFragment.setArguments(bundle);
        return pokemonListFragment;
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.pokemon_list_layout, null);
        }

        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initActionBar(false, null);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        pokemonListView.setHasFixedSize(true);

        // use a linear layout manager
        int orientation = Configuration.ORIENTATION_PORTRAIT;
        if(getActivity() != null && getActivity().getResources() != null) {
            orientation = getActivity().getResources().getConfiguration().orientation;
        }
        LinearLayoutManager mLayoutManager;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new LinearLayoutManager(getContext());
        } else {
            if (navigationManager.isTabletNavigation()) {
                mLayoutManager = new GridLayoutManager(getContext(), 2);
            } else {
                mLayoutManager = new GridLayoutManager(getContext(), 3);

            }
        }

        pokemonListView.setLayoutManager(mLayoutManager);
        mAdapter = new PokemonAdapter(getContext(), navigationManager, this);
        pokemonListView.setAdapter(mAdapter);
        if(pokemonListView.getAdapter() != null) {
            pokemonListView.getAdapter().notifyDataSetChanged();
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PokemonListViewModel.class);
        CharSequence query = null;
        if (getArguments() != null) {
            query = getArguments().getCharSequence(KEY_SEARCH_QUERY, null);
        }
        viewModel.init(query);

        viewModel.getPokemons().observe(this, pokemonList -> {
            // specify an adapter (see also next example)
            mAdapter.setData(pokemonList);
        });

    }

    @Override
    public void onCapture(Pokemon pokemon) {
        viewModel.capture(pokemon);
    }
}