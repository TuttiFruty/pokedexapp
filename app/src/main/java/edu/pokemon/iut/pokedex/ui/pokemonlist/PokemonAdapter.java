package edu.pokemon.iut.pokedex.ui.pokemonlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import edu.pokemon.iut.pokedex.R;
import edu.pokemon.iut.pokedex.architecture.NavigationManager;
import edu.pokemon.iut.pokedex.data.model.Pokemon;

/**
 * Custom adapter to show each pokemon in a single line view
 */
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {
    private final Context context;
    private final NavigationManager navigationManager;
    private final CaptureListener captureListener;
    private List<Pokemon> dataSet;

    PokemonAdapter(Context context, NavigationManager navigationManager, CaptureListener captureListener) {
        this.context = context;
        this.navigationManager = navigationManager;
        this.captureListener = captureListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.pokemon_line_view;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /* Mapping of the data into the view*/
        Pokemon pokemon = dataSet.get(position);

        // TODO 5) RECUPERER VIA LE HOLDER LA TEXTVIEW

        // TODO 6) INSERER LE NOM DU POKEMON DANS LA TEXTVIEW
    }

    @Override
    public int getItemCount() {
        if (dataSet != null) {
            return dataSet.size();
        } else {
            return 0;
        }
    }

    public void setData(List<Pokemon> data) {
        this.dataSet = data;
        notifyDataSetChanged();
    }

    public interface CaptureListener {
        void onCapture(Pokemon pokemon);
    }

    /**
     * Inner ViewHolder for the Pokemon's view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //TODO 3) DECLARER ICI UNE VARIABLE POUR LA TEXTVIEW

        ViewHolder(View v) {
            super(v);
            //TODO 4) RECUPERER ICI UNE INSTANCE DE LA TEXTVIEW ET LA SAUVEGARGER DANS LA VARIABLE
        }
    }
}
