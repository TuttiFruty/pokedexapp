package edu.pokemon.iut.pokedex.architecture

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewCompat
import android.view.View

import javax.inject.Singleton

import edu.pokemon.iut.pokedex.R
import edu.pokemon.iut.pokedex.ui.pokemondetail.PokemonDetailFragment
import edu.pokemon.iut.pokedex.ui.pokemonlist.PokemonListFragment

/**
 * Helper class to ease the navigation between screens.
 */
@Singleton
class NavigationManager {
    //Allow to know if we the app is launch in a tablet or big screen allow
    /**
     * @return true if we are on Tablet/BigScreen, false otherwise
     */
    /**
     * @param tabletNavigation Set with true if the app is on Tablet/BigScreen, false otherwise
     */
    var isTabletNavigation: Boolean = false
    //Allow to know if we navigate through the app with swipe
    private var isSwipe = false

    private var fragmentManager: FragmentManager? = null
    private var navigationListener: NavigationListener? = null

    /**
     * @return true if the current fragment displayed is a root fragment
     */
    val isRootFragmentVisible: Boolean
        get() = this.fragmentManager!!.backStackEntryCount <= 1

    /**
     * Initialize the NavigationManager with a FragmentManager, which will be used at the
     * fragment transactions.
     *
     * @param fragmentManager provide by the activity
     */
    fun init(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
        this.fragmentManager!!.addOnBackStackChangedListener {
            if (this.navigationListener != null) {
                this.navigationListener!!.onBackstackChanged()
            }
        }
    }

    /**
     * Displays the next fragment<br></br>
     * The parameter isRoot allow the NavigationManager to pop all the current backstack<br></br>
     * @param fragment      which fragment we want to show
     * @param sharedElements any view that is shared between the current fragment and the new one
     * @param isRoot        true if the new fragment must considered as root of the application
     */
    private fun open(fragment: Fragment, sharedElements: List<View>?, isRoot: Boolean) {
        if (this.fragmentManager != null) {
            //If we are on tablet navigation we can show both list and detail on the same screen if not we always show on the main_container
            val idContainer = if (isTabletNavigation && !isRoot) R.id.detail_container else R.id.main_container

            val fragmentTransaction = this.fragmentManager!!.beginTransaction()

            //In cas we have common view on old and new fragment, we add it to the transaction
            if (sharedElements != null) {
                for (sharedElement in sharedElements) {
                    val transition = ViewCompat.getTransitionName(sharedElement)
                    if (transition != null) {
                        fragmentTransaction.addSharedElement(sharedElement, transition)
                    }
                }
            }

            //If we are on tablet or we are not the root we don't need to add the fragment to the backstack
            if (!isTabletNavigation || isRoot) {
                fragmentTransaction.addToBackStack(fragment.toString())
            }

            fragmentTransaction.replace(idContainer, fragment).commit()
        }
    }

    /**
     * pops every fragment and starts the given fragment as a new one.
     *
     * @param sharedElements any view that is shared between the current fragment and the new one
     * @param fragment      which fragment we want to show
     */
    private fun openAsRoot(fragment: Fragment, sharedElements: List<View>?) {
        popEveryFragment()
        open(fragment, sharedElements, true)
    }

    /**
     * Pops all the queued fragments
     */
    private fun popEveryFragment() {
        // Clear all back stack.
        val backStackCount = this.fragmentManager!!.backStackEntryCount
        for (i in 0 until backStackCount) {

            // Get the back stack fragment id.
            val backStackId = this.fragmentManager!!.getBackStackEntryAt(i).id

            this.fragmentManager!!.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        }
    }

    /**
     * Navigates back by popping the back stack. If there is no more items left we finish the current activity.<br></br>
     * In case of swipe navigation we start de main fragment.
     *
     * @param baseActivity activity to finish in case of backstack empty
     */
    fun navigateBack(baseActivity: Activity) {
        if (isSwipe) {
            isSwipe = false
            startPokemonList(null, null)
        } else {
            if (this.fragmentManager!!.backStackEntryCount == 0) {
                // we can finish the base activity since we have no other fragments
                baseActivity.finish()
            } else {
                this.fragmentManager!!.popBackStackImmediate()
            }
        }
    }


    /**
     * Start the pokemon list fragment.<br></br>
     * We can pass it a View as a shared element between fragments.<br></br>
     * Also a query can be used to filter the list shown<br></br>
     * @param sharedElement [View] shared between both fragments
     * @param query         [CharSequence] that filter the list
     */
    fun startPokemonList(sharedElement: List<View>?, query: CharSequence?) {
        val fragment = PokemonListFragment.newInstance(query)
        openAsRoot(fragment, sharedElement)
    }

    /**
     * Start the pokemon detail view for the pokemonId<br></br>
     * We can pass it a View as a shared element between fragments.<br></br>
     * @param pokemonId     the pokemon id to show
     * @param sharedElements [View] shared between both fragments
     * @param isSwipe       true if we swipe to show the new pokemon, false otherwise
     */
    fun startPokemonDetail(pokemonId: Int, sharedElements: List<View>, isSwipe: Boolean) {
        this.isSwipe = isSwipe
        val fragment = PokemonDetailFragment.newInstance(pokemonId, sharedElements, !isTabletNavigation)
        open(fragment, sharedElements, false)
    }

    fun setNavigationListener(navigationListener: NavigationListener) {
        this.navigationListener = navigationListener
    }

    /**
     * Listener interface for navigation events.
     */
    interface NavigationListener {

        /**
         * Callback on backstack changed.
         */
        fun onBackstackChanged()
    }

    companion object {
        //Keys used to transition Views between Fragments
        val IMAGE_VIEW_POKEMON_LOGO = "IMAGE_VIEW_POKEMON_LOGO"
        val IMAGE_VIEW_POKEMON_CAPTURE = "IMAGE_VIEW_POKEMON_CAPTURE"
        val IMAGE_VIEW_POKEMON_SHADOW = "IMAGE_VIEW_POKEMON_SHADOW"
    }
}
