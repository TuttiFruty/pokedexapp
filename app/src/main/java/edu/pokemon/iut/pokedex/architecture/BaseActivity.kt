package edu.pokemon.iut.pokedex.architecture

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout

import javax.inject.Inject

import butterknife.ButterKnife
import edu.pokemon.iut.pokedex.PokedexApp
import edu.pokemon.iut.pokedex.R

/**
 * Base activity for all the Activities, it provides some common operation for all of the sub-activities.<br></br>
 * - Set up the default [Toolbar], NavigationOnClickListener is set with onBackPressed from activity<br></br>
 * - onBackPressed from Activity is set with navigateBack from [NavigationManager]<br></br>
 * - [NavigationManager] is inject here via [PokedexApp] and [edu.pokemon.iut.pokedex.architecture.di.DaggerPokemonComponent]<br></br>
 * - The main content of the activity is set via setContentView on R.layout.activity_base<br></br>
 * - [ButterKnife] bind's all views on setContentView<br></br>
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var navigationManager: NavigationManager

    private var lastQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PokedexApp.app()!!.component()!!.inject(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(R.layout.activity_base)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener { view -> this.onBackPressed() }

        val mContentLayout = findViewById<FrameLayout>(R.id.content)
        // Get an inflater
        layoutInflater.inflate(layoutResID, mContentLayout)
        ButterKnife.bind(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.navigationManager!!.navigateBack(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        // Configure the search info and add any event listeners...
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                this@BaseActivity.lastQuery = query
                this@BaseActivity.navigationManager!!.startPokemonList(null, query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                this@BaseActivity.lastQuery = newText
                this@BaseActivity.navigationManager!!.startPokemonList(null, newText)
                return false
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                this@BaseActivity.lastQuery = null
                return true
            }
        })

        // Restore last Query if any exist and keep search bar activated
        // We do not rerun the query or re input the text
        if (this.lastQuery != null && !this.lastQuery!!.isEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(this.lastQuery, true)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putCharSequence(KEY_LAST_SEARCH_QUERY, lastQuery)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            this.lastQuery = savedInstanceState.getString(KEY_LAST_SEARCH_QUERY, null)
        }
    }

    companion object {

        private val KEY_LAST_SEARCH_QUERY = "KEY_LAST_SEARCH_QUERY"
    }
}