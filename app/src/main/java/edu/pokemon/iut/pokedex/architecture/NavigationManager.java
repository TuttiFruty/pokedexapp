package edu.pokemon.iut.pokedex.architecture;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.view.View;

import edu.pokemon.iut.pokedex.R;
import edu.pokemon.iut.pokedex.ui.pokemondetail.PokemonDetailFragment;
import edu.pokemon.iut.pokedex.ui.pokemonlist.PokemonListFragment;

/**
 * Helper class to ease the navigation between screens.
 * Created by becze on 9/30/2015.
 */
public class NavigationManager {
    private boolean tabletNavigation;

    public void setTabletNavigation(boolean tabletNavigation) {
        this.tabletNavigation = tabletNavigation;
    }

    public boolean isTabletNavigation() {
        return tabletNavigation;
    }

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
        mFragmentManager.addOnBackStackChangedListener(() -> {
            if (mNavigationListener != null) {
                mNavigationListener.onBackstackChanged();
            }
        });
    }

    /**
     * Displays the next fragment
     *  @param fragment
     * @param sharedElement
     * @param isRoot
     */
    private void open(Fragment fragment, View sharedElement, boolean isRoot) {
        if (mFragmentManager != null) {
            //@formatter:off
            int idContainer = isTabletNavigation() && !isRoot?R.id.detail_container:R.id.main_container;

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

            if(sharedElement != null){
                fragmentTransaction.addSharedElement(sharedElement, ViewCompat.getTransitionName(sharedElement));
            }

            if(!isTabletNavigation() || isRoot){
                fragmentTransaction.addToBackStack(fragment.toString());
            }

            fragmentTransaction.replace(idContainer,fragment).commit();
            //@formatter:on
        }
    }

    /**
     * pops every fragment and starts the given fragment as a new one.
     *
     * @param fragment
     * @param sharedElement
     */
    private void openAsRoot(Fragment fragment, View sharedElement) {
        popEveryFragment();
        open(fragment, sharedElement, true);
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

    public void startPokemonList(View sharedElement) {
        Fragment fragment = PokemonListFragment.newInstance();
        openAsRoot(fragment, sharedElement);
    }

    public void startPokemonDetail(int pokemonId, View sharedElement) {
        Fragment fragment = PokemonDetailFragment.newInstance(pokemonId, ViewCompat.getTransitionName(sharedElement), !isTabletNavigation());
        open(fragment, sharedElement, false);
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
