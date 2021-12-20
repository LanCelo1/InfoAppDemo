package uz.gita.infoappdemo.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import uz.gita.infoappdemo.api.ApiService
import uz.gita.infoappdemo.api.InfoApi
import uz.gita.infoappdemo.data.model.Data
import uz.gita.infoappdemo.data.model.Info
import uz.gita.infoappdemo.db.InfoDatabase
import uz.gita.infoappdemo.di.RetrofitModule
import uz.gita.infoappdemo.di.anotation.OtherApiClient
import uz.gita.infoappdemo.di.anotation.OtherDatabaseClient
import uz.gita.infoappdemo.ui.adapter.paging.InfoMediator
import javax.inject.Inject

@ExperimentalPagingApi
class InfoRepositoryImpl @Inject constructor(
   @OtherApiClient
    val api : InfoApi,
   @OtherDatabaseClient
    val db: InfoDatabase
) : InfoRepository {

    val DEFAULT_SIZE = 3

    override suspend fun getAllInfo(page: Int, pageSize: Int) =api.getAllInfo(page, pageSize)
    override suspend fun insertInfoToData(data : Data) = db.dataDao().insertData(data)
    override fun getAllInfoFromDatabase() : Flow<PagingData<Data>> {
        if (db == null) throw IllegalStateException("Database is not initialized")

        val pagingSourceFactory = { db.dataDao().getAllDataWithDB() }
        return Pager(
            config = PagingConfig(pageSize =DEFAULT_SIZE ),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = InfoMediator(api, db)
        ).flow

        db.dataDao().getAllDataWithDB()
    }


    //override fun getAllInfo(page: Int) = api.getAllInfo(page)
}