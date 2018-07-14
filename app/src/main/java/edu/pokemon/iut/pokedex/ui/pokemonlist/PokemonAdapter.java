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
        holder.pokemonNumber.setText(context.getString(R.string.number, pokemon.getId()));
        holder.pokemonName.setText(pokemon.getName());

        //If the pokemon is captured we use the full pokeball, otherwise the empty one
        holder.pokemonCapture.setImageResource(pokemon.isCapture() ? R.drawable.ic_launcher_pokeball : R.drawable.ic_launcher_pokeball_empty);

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.ic_launcher_pokeball)
                .centerCrop();
        Glide.with(context)
                .load(pokemon.getSpritesString())
                .apply(options)
                .into(holder.pokemonLogo);

        //Preparation for the transition animation between fragments
        ViewCompat.setTransitionName(holder.pokemonLogo, pokemon.getName()+NavigationManager.IMAGE_VIEW_POKEMON_LOGO);
        ViewCompat.setTransitionName(holder.pokemonCapture, pokemon.getName()+NavigationManager.IMAGE_VIEW_POKEMON_CAPTURE);
        ViewCompat.setTransitionName(holder.pokemonCaptureShadow, pokemon.getName()+NavigationManager.IMAGE_VIEW_POKEMON_SHADOW);

        List<View> listTransitionView = new ArrayList<>();
        listTransitionView.add(holder.pokemonLogo);
        listTransitionView.add(holder.pokemonCapture);
        listTransitionView.add(holder.pokemonCaptureShadow);

        /* Init of the listeners */
        holder.pokemonLine.setOnClickListener(v -> navigationManager.startPokemonDetail(pokemon.getId(), listTransitionView, false));
        holder.pokemonCapture.setOnClickListener(v -> captureListener.onCapture(pokemon));
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
        final TextView pokemonName;
        final TextView pokemonNumber;
        final ImageView pokemonLogo;
        final View pokemonLine;
        final ImageView pokemonCapture;
        final View pokemonCaptureShadow;

        ViewHolder(View v) {
            super(v);
            pokemonName = v.findViewById(R.id.tv_pokemon_name);
            pokemonNumber = v.findViewById(R.id.tv_pokemon_number);
            pokemonLogo = v.findViewById(R.id.iv_pokemon_logo);
            pokemonLine = v.findViewById(R.id.cl_pokemon_line);
            pokemonCapture = v.findViewById(R.id.iv_pokemon_capture);
            pokemonCaptureShadow = v.findViewById(R.id.cv_pokemon_capture);
        }
    }
}
