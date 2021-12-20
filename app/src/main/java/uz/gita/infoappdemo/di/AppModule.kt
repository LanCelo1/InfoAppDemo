package uz.gita.infoappdemo.di

import androidx.paging.ExperimentalPagingApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.infoappdemo.api.ApiService
import uz.gita.infoappdemo.api.InfoApi
import uz.gita.infoappdemo.data.model.Info
import uz.gita.infoappdemo.repository.InfoRepository
import uz.gita.infoappdemo.repository.InfoRepositoryImpl
import uz.gita.infoappdemo.ui.adapter.paging.PagerSource

@ExperimentalPagingApi
@Module
@InstallIn(ViewModelComponent::class)
interface AppModule {

    @Binds
    fun bindRepository(repositoryImpl: InfoRepositoryImpl): InfoRepository


}