package uz.gita.infoappdemo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.gita.infoappdemo.data.model.Data
import uz.gita.infoappdemo.data.model.RemoteKeys
import uz.gita.infoappdemo.utils.Conventors

@Database(entities = [Data::class, RemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(Conventors::class)
abstract class InfoDatabase : RoomDatabase() {
    abstract fun dataDao() : DataDao
    abstract fun remoteKeys() : RemoteKeysDao
}