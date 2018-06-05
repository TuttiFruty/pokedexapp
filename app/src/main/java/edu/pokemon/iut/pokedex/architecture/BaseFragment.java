package edu.pokemon.iut.pokedex.architecture;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import edu.pokemon.iut.pokedex.R;

/**
 * Base class for every Fragment, it provides some custom behavior for all of the fragments
 * sucn as dependency injection.
 * Created by becze on 9/21/2015.
 */
public class BaseFragment extends Fragment {

    private static final String DEFAULT_TITLE = "Pokedex";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initActionBar(true, getString(R.string.app_name));


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("POKEDEX", "onDestroy: " + this);
    }

    /**
     * Initializes the ActionBar
     *
     * @param showHomeButton
     * @param title
     */
    protected void initActionBar(boolean showHomeButton, String title) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ActionBar supportActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayHomeAsUpEnabled(showHomeButton);
                supportActionBar.setTitle(title != null ? title : DEFAULT_TITLE);
            }
        }
    }

    protected void setTitle(String title) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ActionBar supportActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(title != null ? title.toUpperCase() : DEFAULT_TITLE);
            }
        }
    }

    /**
     * Shows and hides the actionbar
     *
     * @param isShown
     */
    protected void showActionbar(boolean isShown) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ActionBar supportActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
            if (supportActionBar != null) {
                if (isShown) {
                    supportActionBar.show();
                } else {
                    supportActionBar.hide();
                }
            }
        }
    }

}