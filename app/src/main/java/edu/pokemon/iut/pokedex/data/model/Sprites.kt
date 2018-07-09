package edu.pokemon.iut.pokedex.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
class Sprites {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("front_default")
    @Expose
    var frontDefault: String? = null
}
