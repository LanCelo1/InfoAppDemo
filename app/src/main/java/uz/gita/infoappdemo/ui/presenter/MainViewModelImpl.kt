package uz.gita.infoappdemo.ui.presenter

import androidx.lifecycle.*
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.gita.infoappdemo.api.ApiService
import uz.gita.infoappdemo.api.InfoApi
import uz.gita.infoappdemo.data.model.Data
import uz.gita.infoappdemo.data.model.Info
import uz.gita.infoappdemo.repository.InfoRepository
import uz.gita.infoappdemo.repository.InfoRepositoryImpl
import uz.gita.infoappdemo.ui.adapter.paging.PagerSource
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModelImpl @Inject constructor(
    val repository: InfoRepository,
    val pagerSource: PagerSource,
) : MainViewModel, ViewModel() {
    //  private lateinit var source: PagerSource
    override val getAllInfoLiveData = MutableSharedFlow<List<Data>>()
    override val getAllInfoFromDatabaseLiveData = MutableSharedFlow<List<Data>>()
    override val getErrorMessageLiveData = MutableSharedFlow<String>()

    override var getAllInfo: Flow<PagingData<Data>> = Pager<Int, Data>(
        PagingConfig(pageSize = 3)) {
        pagerSource
    }.flow.cachedIn(viewModelScope)
//        .flow.stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())


    override fun getAllInfo(page: Int, pageSize: Int) {


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getAllInfo(page, pageSize)
                if (response.isSuccessful) {
                    response?.body()?.let {
                        getAllInfoLiveData.emit(it.data)
                    }
                } else {
                    getErrorMessageLiveData.emit(response.message())
                }
            } catch (e: Exception) {
                getErrorMessageLiveData.emit(e.message.toString())
            }
        }
    }

    override fun insertDataToDatabase(data: Data) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertInfoToData(data)
        }
    }

    override fun getAllInfoFromDatabase(): Flow<PagingData<Data>> {
//        viewModelScope.launch(Dispatchers.IO) {
//            getAllInfoFromDatabaseLiveData.emit(repository.getAllInfoFromDatabase())
//        }


        return repository.getAllInfoFromDatabase().cachedIn(viewModelScope)
    }

    override var getAllInfoFromDb: Flow<PagingData<Data>> =
        repository.getAllInfoFromDatabase().cachedIn(viewModelScope)

}
/*
*    init {
        val api = ApiService.retrofit.create(InfoApi::class.java)
        source = PagerSource(api)
    }
    var getAllInfo : StateFlow<PagingData<Data>> = Pager<Int,Data>(
        PagingConfig(pageSize = 5)
    ){
        source
    }.flow.stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

*/

/*
//    override val getAllInfoLiveData = MutableLiveData<List<Data>>()
//
//    override val getErrorMessageLiveData = MutableLiveData<String>()

* //    override fun getAllInfo(page: Int)  {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getAllInfo(page).enqueue(object : Callback<Info> {
//                override fun onResponse(call: Call<Info>, response: Response<Info>) {
//                    if (response.isSuccessful) {
//                        response.body()?.let {
//                            getAllInfoLiveData.postValue(it.data)
//                        }
//                    } else {
//                        getErrorMessageLiveData.postValue(response.message())
//                    }
//                }
//
//                override fun onFailure(call: Call<Info>, t: Throwable) {
//                    getErrorMessageLiveData.postValue(t.message)
//                }
//
//            })
//        }
//    }

//    override fun getAllInfo(page: Int)  {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getAllInfo(page)
//        }
//    }
* */