package uz.gita.infoappdemo.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.gita.infoappdemo.data.model.Info

interface InfoApi {

    @GET("/service/v2/upcomingGuides/")
    suspend fun getAllInfo(
       @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int?= null
    ) : Response<Info>


}