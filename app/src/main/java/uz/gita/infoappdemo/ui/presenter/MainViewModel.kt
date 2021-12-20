package uz.gita.infoappdemo.ui.presenter

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import uz.gita.infoappdemo.data.model.Data

interface MainViewModel  {

   var getAllInfo : Flow<PagingData<Data>>
   var getAllInfoFromDb : Flow<PagingData<Data>>
    val getAllInfoLiveData : SharedFlow<List<Data>>
    val getErrorMessageLiveData : SharedFlow<String>
    val getAllInfoFromDatabaseLiveData : SharedFlow<List<Data>>

    fun getAllInfo(page: Int, pageSize : Int)

    fun insertDataToDatabase(data: Data)

    fun getAllInfoFromDatabase(): Flow<PagingData<Data>>

}