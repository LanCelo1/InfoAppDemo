package uz.gita.infoappdemo.ui.screens

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAssignedNumbers
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import uz.gita.infoappdemo.App
import uz.gita.infoappdemo.R
import uz.gita.infoappdemo.databinding.ScreenMainBinding
import uz.gita.infoappdemo.ui.adapter.InfoAdapter
import uz.gita.infoappdemo.ui.presenter.MainViewModel
import uz.gita.infoappdemo.ui.presenter.MainViewModelImpl
import android.util.DisplayMetrics
import androidx.paging.cachedIn
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import uz.gita.infoappdemo.ui.adapter.loader.LoaderStateAdapter


@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main) {
    private var _binding: ScreenMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels<MainViewModelImpl>()
    private lateinit var infoAdapter: InfoAdapter
    private lateinit var loaderStateAdapter: LoaderStateAdapter
    private lateinit var rvDoggoLoader: RecyclerView
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = ScreenMainBinding.bind(view)
        setUpRecyclerview()
        setUpObservers()
//        viewModel.getAllInfo(5,4)
        getSizeOfScreen()

        if (isNetworkAvailable(context)) {
            getAllInfo()
        } else {
            getAllInfoFromDatabase()
        }
    }

    private fun getSizeOfScreen(): DisplayMetrics {
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        return metrics
    }

    @SuppressLint("CheckResult")
    fun getAllInfo() {
        lifecycleScope.launch {
            viewModel.getAllInfo.distinctUntilChanged().collectLatest {
                infoAdapter.submitData(it)
            }
        }
    }

    fun getAllInfoFromDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getAllInfoFromDatabase().distinctUntilChanged().collectLatest {
                infoAdapter.submitData(it)
                Timber.d("it's database source")
            }
        }
    }

    private fun setUpObservers() {
        /* viewModel.getAllInfoLiveData.observe(viewLifecycleOwner) {
             infoAdapter.submitList(it)
             Timber.d("list = $it")
         }*/


        /* viewModel.getAllInfoLiveData.onEach {
             infoAdapter.submitData(it)
             it.forEach {
                 viewModel.insertDataToDatabase(it)
             }
         }.launchIn(lifecycleScope)

          viewModel.getAllInfoFromDatabaseLiveData.onEach {
              infoAdapter.submitData(it)
          }.launchIn(lifecycleScope)*/

        viewModel.getErrorMessageLiveData.onEach {
            Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
        }.launchIn(lifecycleScope)
    }

    private fun setUpRecyclerview() {
        infoAdapter = InfoAdapter(getSizeOfScreen())
        binding.recyclerview.apply {
            adapter = infoAdapter
            loaderStateAdapter = LoaderStateAdapter { infoAdapter.retry() }
            infoAdapter.onClickItemListener {
                navController.navigate(MainScreenDirections.actionMainScreenToWebViewScreen(it.url))
            }
            infoAdapter.haveElementListener {
                binding.imgEmptyList.visibility = View.GONE
            }
            binding.recyclerview.adapter = infoAdapter.withLoadStateFooter(loaderStateAdapter)
            val decoration = DividerItemDecoration(App.instance, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            layoutManager = LinearLayoutManager(this@MainScreen.context)
        }
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}