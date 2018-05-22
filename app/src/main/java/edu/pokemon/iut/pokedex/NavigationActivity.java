package edu.pokemon.iut.pokedex;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import javax.inject.Inject;

import edu.pokemon.iut.pokedex.architecture.BaseActivity;
import edu.pokemon.iut.pokedex.architecture.NavigationManager;

public class NavigationActivity extends BaseActivity implements NavigationManager.NavigationListener {

    @Inject
    public NavigationManager mNavigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Inject members
        PokedexApp.app().component().inject(this);

        // Initialize the NavigationManager with this activity's FragmentManager
        mNavigationManager.init(getSupportFragmentManager());
        mNavigationManager.setNavigationListener(this);

        // start as the first screen the rules overview if there is no configuration change(start from scratch, else we stay where we are)
        if(savedInstanceState == null){
            mNavigationManager.startPokemonList();
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
    protected void showExitDialog() {
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
}
