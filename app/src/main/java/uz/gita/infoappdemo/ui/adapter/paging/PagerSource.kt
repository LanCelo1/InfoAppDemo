package uz.gita.infoappdemo.ui.adapter.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import uz.gita.infoappdemo.api.ApiService
import uz.gita.infoappdemo.api.InfoApi
import uz.gita.infoappdemo.data.model.Data
import uz.gita.infoappdemo.db.InfoDatabase
import uz.gita.infoappdemo.di.RetrofitModule
import uz.gita.infoappdemo.di.anotation.OtherApiClient
import java.lang.Exception
import javax.inject.Inject

class PagerSource @Inject constructor (
    @OtherApiClient
    val api : InfoApi,
    val db : InfoDatabase
) : PagingSource<Int,Data>() {

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        var prevKey : Int? = null
        var nextKey : Int? = null
        var info : List<Data> = emptyList()
       return try {
           val response = api.getAllInfo(page, 3)
           if (response.isSuccessful){
                info = checkNotNull(response?.body()?.data?.map { it })
               prevKey = if (page == 1) null else page - 1
                nextKey = if (info.isEmpty()) null else page + 1
           }
           info.forEach {
               db.dataDao().insertData(it)
           }
           LoadResult.Page(
               data = info,
               prevKey = prevKey,
               nextKey = nextKey
           )
       }catch (e : Exception){
           return LoadResult.Error(e)
       }catch (e : HttpException){
           return LoadResult.Error(e)
       }
    }

}