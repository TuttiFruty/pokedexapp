package edu.pokemon.iut.pokedex

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View

import edu.pokemon.iut.pokedex.architecture.BaseActivity
import edu.pokemon.iut.pokedex.architecture.NavigationManager

/**
 * Main activity of the App, it will control all the navigation for the app.
 */
class NavigationActivity : BaseActivity(), NavigationManager.NavigationListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        // Initialize the NavigationManager with this activity's FragmentManager
        this.navigationManager!!.init(supportFragmentManager)
        this.navigationManager!!.setNavigationListener(this)

        val detailsFrame = findViewById<View>(R.id.detail_container)
        //If the R.layout.activity_navigation contains a detail_container we know we are on a Tablet/BigScreen
        this.navigationManager!!.isTabletNavigation = detailsFrame != null && detailsFrame.visibility == View.VISIBLE

        // start as the first screen the rules overview if there is no configuration change(start from scratch, else we stay where we are)
        if (savedInstanceState == null) {
            this.navigationManager!!.startPokemonList(null, null)
        }
    }

    override fun onBackPressed() {
        // Note: we intentionally not calling the super implementation since in the CIS app
        // we decided to use support fragment manager.

        //we pressed the back button, show the logout dialog
        if (supportFragmentManager.backStackEntryCount == 1) {
            // we have only one fragment left so we would close the application with this back
            showExitDialog()
        } else {
            this.navigationManager!!.navigateBack(this)
        }
    }

    /**
     * Shows the logout dialog. Stops the service and finishes the application.
     */
    private fun showExitDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(R.string.exit_message).setCancelable(false).setPositiveButton(android.R.string.yes
        ) { dialog, id -> finish() }.setNegativeButton(android.R.string.cancel, null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onBackstackChanged() {
        // check if we display a root fragment and enable drawer only on root fragments
        val rootFragment = this.navigationManager!!.isRootFragmentVisible
    }
}
