package edu.pokemon.iut.pokedex.ui.pokemonlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import edu.pokemon.iut.pokedex.architecture.NavigationManager;
import edu.pokemon.iut.pokedex.R;
import edu.pokemon.iut.pokedex.data.model.Pokemon;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {
    private final Context context;
    private final NavigationManager navigationManager;
    private List<Pokemon> mDataset;
    private CaptureListener captureListener;

    public PokemonAdapter(Context context, NavigationManager navigationManager, CaptureListener captureListener) {
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
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = mDataset.get(position);
        holder.pokemonNumber.setText(Integer.toString(pokemon.getId()));
        holder.pokemonName.setText(pokemon.getName());

        holder.pokemonCapture.setImageResource(pokemon.isCapture()? R.drawable.ic_launcher_pokeball:R.drawable.ic_launcher_pokeball_empty);

        RequestOptions options = new RequestOptions()
                .centerCrop();
        Glide.with(context)
                .load(pokemon.getSpritesString())
                .apply(options)
                .into(holder.pokemonLogo);

        ViewCompat.setTransitionName(holder.pokemonLogo, pokemon.getName());
        holder.pokemonLine.setOnClickListener(v -> navigationManager.startPokemonDetail(pokemon.getId(), holder.pokemonLogo));
        holder.pokemonCapture.setOnClickListener(v -> captureListener.onCapture(pokemon.capture()));
    }

    @Override
    public int getItemCount() {
        if (mDataset != null) {
            return mDataset.size();
        } else {
            return 0;
        }
    }

    public void setData(List<Pokemon> data) {
        this.mDataset = data;
        notifyDataSetChanged();
    }

    public interface CaptureListener {
        void onCapture(Pokemon pokemon);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView pokemonName;
        public TextView pokemonNumber;
        public ImageView pokemonLogo;
        public View pokemonLine;
        public ImageView pokemonCapture;

        public ViewHolder(View v) {
            super(v);
            pokemonName = v.findViewById(R.id.tv_pokemon_name);
            pokemonNumber = v.findViewById(R.id.tv_pokemon_number);
            pokemonLogo = v.findViewById(R.id.iv_pokemon_logo);
            pokemonLine = v.findViewById(R.id.cl_pokemon_line);
            pokemonCapture = v.findViewById(R.id.iv_pokemon_capture);
        }
    }
}
