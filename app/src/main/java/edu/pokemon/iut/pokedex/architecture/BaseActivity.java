package edu.pokemon.iut.pokedex.architecture;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import edu.pokemon.iut.pokedex.PokedexApp;
import edu.pokemon.iut.pokedex.R;

/**
 * Base activity for all the Activities, it provides some common operation for all of the sub-activities.<br>
 * - Set up the default {@link Toolbar}, NavigationOnClickListener is set with onBackPressed from activity<br>
 * - onBackPressed from Activity is set with navigateBack from {@link NavigationManager}<br>
 * - {@link NavigationManager} is inject here via {@link PokedexApp} and {@link edu.pokemon.iut.pokedex.architecture.di.DaggerPokemonComponent}<br>
 * - The main content of the activity is set via setContentView on R.layout.activity_base<br>
 * - {@link ButterKnife} bind's all views on setContentView<br>
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private static final String KEY_LAST_SEARCH_QUERY = "KEY_LAST_SEARCH_QUERY";
    @Inject
    protected NavigationManager navigationManager;
    private String lastQuery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PokedexApp.app().component().inject(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(view -> this.onBackPressed());

        FrameLayout mContentLayout = findViewById(R.id.content);
        // Get an inflater
        getLayoutInflater().inflate(layoutResID, mContentLayout);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.navigationManager.navigateBack(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Configure the search info and add any event listeners...
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                BaseActivity.this.lastQuery = query;
                BaseActivity.this.navigationManager.startPokemonList(null, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                BaseActivity.this.lastQuery = newText;
                BaseActivity.this.navigationManager.startPokemonList(null, newText);
                return false;
            }
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                BaseActivity.this.lastQuery = null;
                return true;
            }
        });

        // Restore last Query if any exist and keep search bar activated
        // We do not rerun the query or re input the text
        if (this.lastQuery != null && !this.lastQuery.isEmpty()) {
            searchItem.expandActionView();
            searchView.setQuery(this.lastQuery, true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_night:
                PokedexApp.app().setIsNightModeEnabled(! PokedexApp.app().isNightModeEnabled());
                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence(KEY_LAST_SEARCH_QUERY, lastQuery);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            this.lastQuery = savedInstanceState.getString(KEY_LAST_SEARCH_QUERY, null);
        }
    }
}