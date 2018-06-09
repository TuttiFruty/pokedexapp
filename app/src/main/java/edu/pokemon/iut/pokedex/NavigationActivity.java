package edu.pokemon.iut.pokedex;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.pokemon.iut.pokedex.architecture.BaseActivity;
import edu.pokemon.iut.pokedex.architecture.NavigationManager;

public class NavigationActivity extends BaseActivity implements NavigationManager.NavigationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Initialize the NavigationManager with this activity's FragmentManager
        mNavigationManager.init(getSupportFragmentManager());
        mNavigationManager.setNavigationListener(this);

        View detailsFrame = findViewById(R.id.detail_container);
        mNavigationManager.setTabletNavigation(detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE);

        // start as the first screen the rules overview if there is no configuration change(start from scratch, else we stay where we are)
        if (savedInstanceState == null) {
            mNavigationManager.startPokemonList(null, null);
        }
    }

    @Override
    public void onBackPressed() {
        // Note: we intentionally not calling the super implementation since in the CIS app
        // we decided to use support fragment manager.

        //we pressed the back button, show the logout dialog
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            // we have only one fragment left so we would close the application with this back
            showExitDialog();
        } else {
            mNavigationManager.navigateBack(this);
        }
    }

    /**
     * Shows the logout dialog. Stops the service and finishes the application.
     */
    private void showExitDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.exit_message).setCancelable(false).setPositiveButton(android.R.string.yes,
                (dialog, id) -> finish()).setNegativeButton(android.R.string.cancel, null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackstackChanged() {
        // check if we display a root fragment and enable drawer only on root fragments
        boolean rootFragment = mNavigationManager.isRootFragmentVisible();
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
                mNavigationManager.startPokemonList(null, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mNavigationManager.startPokemonList(null, newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
