package edu.pokemon.iut.pokedex;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import edu.pokemon.iut.pokedex.architecture.BaseActivity;
import edu.pokemon.iut.pokedex.architecture.NavigationManager;

/**
 * Main activity of the App, it will control all the navigation for the app.
 */
public class NavigationActivity extends BaseActivity implements NavigationManager.NavigationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        if (PokedexApp.app().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Initialize the NavigationManager with this activity's FragmentManager
        this.navigationManager.init(getSupportFragmentManager());
        this.navigationManager.setNavigationListener(this);

        View detailsFrame = findViewById(R.id.detail_container);
        //If the R.layout.activity_navigation contains a detail_container we know we are on a Tablet/BigScreen
        this.navigationManager.setTabletNavigation(detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE);

        // start as the first screen the rules overview if there is no configuration change(start from scratch, else we stay where we are)
        if (savedInstanceState == null) {
            this.navigationManager.startPokemonList(null, null);
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
            this.navigationManager.navigateBack(this);
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
        boolean rootFragment = this.navigationManager.isRootFragmentVisible();
    }
}
