package uz.gita.infoappdemo.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "data_table")
data class Data(
    val id : Int? = null,
    val endDate: String,
    val icon: String,
    val loginRequired: Boolean,
    @PrimaryKey
    val name: String,
    val objType: String,
    val startDate: String,
    val url: String,
    val venue: Venue
) : Serializable{

}