package edu.pokemon.iut.pokedex.ui.pokemonlist;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.pokemon.iut.pokedex.R;
import edu.pokemon.iut.pokedex.architecture.BaseFragment;
import edu.pokemon.iut.pokedex.data.model.Pokemon;

/**
 * Fragment to show a List of Pokemon
 */
@SuppressWarnings("WeakerAccess")
public class PokemonListFragment extends BaseFragment implements PokemonAdapter.CaptureListener {

    private static final String TAG = PokemonListFragment.class.getSimpleName();


    /* VIEWS */
    // TODO 7) DECLARER ICI UNE VARIABLE POUR LA RECYCLERVIEW
    // TODO 8) "BINDER" AVEC DAGGER l'ID DE LA VUE SUR LA VARIABLE : @BindView(R.id.vue)

    /* ATTRIBUTES */
    private PokemonListViewModel viewModel;
    private PokemonAdapter adapter;
    private View rootView;

    /**
     * @param query to filter the pokemon list
     * @return newInstance of SampleFragment
     */
    public static PokemonListFragment newInstance(CharSequence query) {
        PokemonListFragment pokemonListFragment = new PokemonListFragment();
        return pokemonListFragment;
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.pokemon_list_layout, null);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initActionBar(false, null);

        // TODO 9) CREER UN LinearLayoutManager POUR DEFINIR LE STYLE D'AFFICHAGE DE LA RECYCLERVIEW

        // TODO 10) AJOUTER LE LinearLayoutManager SUR LA RECYCLERVIEW

        // TODO 11) CREER UN PokemonAdapter POUR GERER L'AFFICHAGE DE CHAQUE POKEMON DANS LA RECYCLERVIEW

        // TODO 12) AJOUTER L'ADAPTER A LA RECYCLEVIEW

        //Initialisation and observation of the ViewModel for this screen
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PokemonListViewModel.class);
        CharSequence query = null;
        viewModel.init(query);

        viewModel.getPokemons().observe(this, pokemonList -> {
            // TODO 13) LORSQUE LA LISTE DES POKEMONS ARRIVE IL FAUT LA DONNER A L'ADAPTER
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onCapture(Pokemon pokemon) {
    }
}