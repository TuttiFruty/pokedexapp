package edu.pokemon.iut.pokedex.architecture.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import edu.pokemon.iut.pokedex.data.dao.PokemonDao;
import edu.pokemon.iut.pokedex.data.database.PokedexDatabase;

@Module
public class DatabaseModule {
    Context context;

    public DatabaseModule(Context context) {
        this.context = context;
    }

    @Provides
    public PokedexDatabase providesPokedexDatabase() {
        return Room.databaseBuilder(context,
                PokedexDatabase.class, "pokedex-database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Inject
    public PokemonDao providesPokemonDao(PokedexDatabase pokedexDatabase) {
        return pokedexDatabase.pokemonDao();
    }
}
