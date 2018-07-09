package edu.pokemon.iut.pokedex.architecture

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.view.View

import javax.inject.Inject

import butterknife.ButterKnife
import edu.pokemon.iut.pokedex.PokedexApp
import edu.pokemon.iut.pokedex.R

/**
 * Base class for every Fragment, it provides some custom behavior for all of the fragments.<br></br>
 * - [NavigationManager] is inject here via [PokedexApp] and [edu.pokemon.iut.pokedex.architecture.di.DaggerPokemonComponent]<br></br>
 * - [ViewModelProvider.Factory] is inject here via [PokedexApp] and [edu.pokemon.iut.pokedex.architecture.di.DaggerPokemonComponent]<br></br>
 * - The {@value DEFAULT_TITLE} of the app is use in the action_bar it can be change by setTitle<br></br>
 * - [ButterKnife] bind's all views at onViewCreated<br></br>
 * - The action bar is init at onViewCreated, the buttonHome is shown by default, at it use the default title
 */
open class BaseFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PokedexApp.app()!!.component()!!.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        initActionBar(true, getString(R.string.app_name))
    }

    /**
     * Initializes the ActionBar
     *
     * @param showHomeButton true if we want to show the Home/Arrow icon, false otherwise
     * @param title          the text we want to show on the action bar, if null we show the default one
     */
    protected fun initActionBar(showHomeButton: Boolean, title: String?) {
        if (activity != null && activity is BaseActivity) {
            val supportActionBar = (activity as BaseActivity).supportActionBar
            if (supportActionBar != null) {
                supportActionBar.setDisplayHomeAsUpEnabled(showHomeButton)
                supportActionBar.title = title ?: DEFAULT_TITLE
            }
        }
    }

    /**
     * Allow to change the action bar title at demand
     *
     * @param title the new title, if null we use the default one
     */
    protected fun setTitle(title: String?) {
        if (activity != null && activity is BaseActivity) {
            val supportActionBar = (activity as BaseActivity).supportActionBar
            if (supportActionBar != null) {
                supportActionBar.title = title?.toUpperCase() ?: DEFAULT_TITLE
            }
        }
    }

    companion object {
        private val DEFAULT_TITLE = "Pokedex"
    }
}