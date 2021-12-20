package uz.gita.infoappdemo.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import uz.gita.infoappdemo.data.model.Data
import uz.gita.infoappdemo.data.model.Info

interface InfoRepository {
    suspend fun getAllInfo(page: Int,pageSize: Int) : Response<Info>

    suspend fun insertInfoToData(data: Data)

    fun getAllInfoFromDatabase() : Flow<PagingData<Data>>
}