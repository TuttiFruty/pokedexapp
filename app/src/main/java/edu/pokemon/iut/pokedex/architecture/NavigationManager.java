package edu.pokemon.iut.pokedex.architecture;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import edu.pokemon.iut.pokedex.R;
import edu.pokemon.iut.pokedex.ui.pokemondetail.PokemonDetailFragment;
import edu.pokemon.iut.pokedex.ui.pokemonlist.PokemonListFragment;

/**
 * Helper class to ease the navigation between screens.
 * Created by becze on 9/30/2015.
 */
public class NavigationManager {
    /**
     * Listener interface for navigation events.
     */
    public interface NavigationListener {

        /**
         * Callback on backstack changed.
         */
        void onBackstackChanged();
    }

    private FragmentManager mFragmentManager;

    private NavigationListener mNavigationListener;

    /**
     * Initialize the NavigationManager with a FragmentManager, which will be used at the
     * fragment transactions.
     *
     * @param fragmentManager
     */
    public void init(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (mNavigationListener != null) {
                    mNavigationListener.onBackstackChanged();
                }
            }
        });
    }

    /**
     * Displays the next fragment
     *
     * @param fragment
     */
    private void open(Fragment fragment) {
        if (mFragmentManager != null) {
            //@formatter:off
            mFragmentManager.beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .addToBackStack(fragment.toString())
                    .commit();
            //@formatter:on
        }
    }

    /**
     * pops every fragment and starts the given fragment as a new one.
     *
     * @param fragment
     */
    private void openAsRoot(Fragment fragment) {
        popEveryFragment();
        open(fragment);
    }


    /**
     * Pops all the queued fragments
     */
    private void popEveryFragment() {
        // Clear all back stack.
        int backStackCount = mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {

            // Get the back stack fragment id.
            int backStackId = mFragmentManager.getBackStackEntryAt(i).getId();

            mFragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
    }

    /**
     * Navigates back by popping teh back stack. If there is no more items left we finish the current activity.
     *
     * @param baseActivity
     */
    public void navigateBack(Activity baseActivity) {

        if (mFragmentManager.getBackStackEntryCount() == 0) {
            // we can finish the base activity since we have no other fragments
            baseActivity.finish();
        } else {
            mFragmentManager.popBackStackImmediate();
        }
    }

    public void startPokemonList() {
        Fragment fragment = PokemonListFragment.newInstance();
        openAsRoot(fragment);
    }

    public void startPokemonDetail(int pokemonId) {
        Fragment fragment = PokemonDetailFragment.newInstance(pokemonId);
        open(fragment);
    }

    /**
     * @return true if the current fragment displayed is a root fragment
     */
    public boolean isRootFragmentVisible() {
        return mFragmentManager.getBackStackEntryCount() <= 1;
    }

    public NavigationListener getNavigationListener() {
        return mNavigationListener;
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        mNavigationListener = navigationListener;
    }
}
