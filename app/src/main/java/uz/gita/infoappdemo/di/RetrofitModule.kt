package uz.gita.infoappdemo.di

import android.content.Context
import androidx.room.Room
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.gita.infoappdemo.App
import uz.gita.infoappdemo.api.ApiService
import uz.gita.infoappdemo.api.InfoApi
import uz.gita.infoappdemo.db.InfoDatabase
import uz.gita.infoappdemo.di.anotation.OtherApiClient
import uz.gita.infoappdemo.di.anotation.OtherDatabaseClient
import uz.gita.infoappdemo.ui.adapter.paging.PagerSource
import uz.gita.infoappdemo.utils.Commons.BASE_URL
import javax.inject.Singleton
import okhttp3.OkHttpClient




@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {


    @OtherApiClient
    @Singleton
    @Provides
    fun bindInfoApi(@ApplicationContext context: Context) : InfoApi {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY}

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(ChuckInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(InfoApi::class.java)
    }
    @OtherDatabaseClient
    @Provides
    fun bindDatabase(@ApplicationContext context: Context) : InfoDatabase{
        val database = Room.databaseBuilder(
            context,
            InfoDatabase::class.java,
            "info_database"
        ).build()
        return database
    }


    @Provides
    fun bindPagingSource(@OtherApiClient api : InfoApi, @OtherDatabaseClient db : InfoDatabase) : PagerSource =  PagerSource(api,db)
}