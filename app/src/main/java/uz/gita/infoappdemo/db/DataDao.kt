package uz.gita.infoappdemo.db

import androidx.paging.PagingSource
import androidx.room.*
import uz.gita.infoappdemo.data.model.Data

@Dao
interface DataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(data : Data)

    @Query("SELECT * FROM data_table")
    fun getAllDataWithDB() : PagingSource<Int, Data>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<Data>)

    @Query("DELETE FROM data_table")
    suspend fun clearAllDoggos()

}