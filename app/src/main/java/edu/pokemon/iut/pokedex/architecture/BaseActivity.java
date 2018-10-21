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

    //TODO 2) AJOUTER UNE CONSTANTE QUI SERVIRA DE CLE POUR RECUPERER LA DERNIERE RECHERCHE EFFECTUE
    //TODO 3) AJOUTER UNE VARIABLE QUI STOCKERA LA DERNIERER RECHERCHE EFFECTUE

    @Inject
    protected NavigationManager navigationManager;

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

    //TODO 4) OVERRIDE LA METHOD onCreateOptionMenu
    //TODO 5) DANS LA METHODE onCreateOptionMenu :
    //TODO 6) RECUPERER LE MenuInflater ET inflate LE LAYOUT DU MENU SUR LE PARAMETRE menu
    //TODO 7) GRACE AU PARAMETRE menu CHERCHER L'Item DE RECHERCHE ET SAUVEGARDER LE DANS UN MenuItem
    //TODO 8) RECUPERER L'ActionView DEPUIS LE MenuItem ET SAUVEGARDER DANS UNE SearchView
    //TODO 9) SUR LA SearchView VOUS ALLEZ setter UN LISTENER QUAND UNE REQUETE EST ECRITE
    //TODO 10) DANS LE LISTENER VOUS ALLER POUVOIR RECUPERER LA REQUETE EST RELANCER L'AFFICHAGE DE LA LISTE DE POKEMON AVEC LA REQUETE
    //TODO 11) N'OUBLIER PAS DE SAUVEGARDER LA REQUETE DANS LA VARIABLE DE CLASSE

    //TODO 12) RETOURNER A LA FIN DE LA METHODE super.onCreateOptionMenu(menu)

    //TODO 17) OVERRIDE LA METHODE onSaveInstanceState
    //TODO 18) DANS LA METHODE onSaveInstanceState SAUVEGARDER LA REQUETE EN COURS

    //TODO 19) OVERRIDE LA METHODE onRestoreInstanceState
    //TODO 20) DANS LA METHODE onRestoreInstanceState RECUPERER LA REQUETE PRECEDEMMENT SAUVEGARDER LA DANS LA VARIABLE DE CLASSE
    //TODO 21) CERTAIN DES SOUCIS DEVRAIT ETRE REGLER MAIS IL RESTE ENCORE QUELQUE BUG : BONUS SI VOUS ARRIVEZ A LES CORRIGERS
}