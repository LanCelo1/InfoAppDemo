package uz.gita.infoappdemo.utils

import androidx.room.TypeConverter
import uz.gita.infoappdemo.data.model.Venue

class Conventors {
    @TypeConverter
    fun fromVenue(venue : Venue) : String = venue.name ?: ""

    @TypeConverter
    fun toVenue(name : String) : Venue = Venue(name)
}