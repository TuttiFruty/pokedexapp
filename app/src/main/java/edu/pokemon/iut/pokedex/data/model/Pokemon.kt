package edu.pokemon.iut.pokedex.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
class Pokemon {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("base_experience")
    @Expose
    var baseExperience: Int = 0
    @SerializedName("height")
    @Expose
    var height: Int = 0
    @SerializedName("is_default")
    @Expose
    var isDefault: Boolean = false
    @SerializedName("order")
    @Expose
    var order: Int = 0
    @SerializedName("weight")
    @Expose
    var weight: Int = 0
    @SerializedName("sprites")
    @Expose
    @Ignore
    var sprites: Sprites? = null
    @SerializedName("types")
    @Expose
    @TypeConverters(PokemonTypeStringTypeConverters::class)
    var types: List<Type>? = null

    var spritesString: String? = null

    var isCapture = false

    val stringId: String
        get() = Integer.toString(this.id)

    val stringBaseExp: String
        get() = Integer.toString(this.baseExperience)

    val stringHeight: String
        get() = java.lang.Float.toString(this.height.toFloat() / 10)

    val stringWeight: String
        get() = java.lang.Float.toString(this.weight.toFloat() / 10)

    fun capture(): Pokemon {
        isCapture = !isCapture
        return this
    }
}
