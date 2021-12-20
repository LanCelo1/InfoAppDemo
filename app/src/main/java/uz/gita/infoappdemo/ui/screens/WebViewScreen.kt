package uz.gita.infoappdemo.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.http.Url
import timber.log.Timber
import uz.gita.infoappdemo.R
import uz.gita.infoappdemo.databinding.ScreenWebviewBinding
import uz.gita.infoappdemo.utils.Commons.BASE_URL
import uz.gita.infoappdemo.utils.MyWebViewClient

@AndroidEntryPoint
class WebViewScreen : Fragment(R.layout.screen_webview) {
    private var _binding: ScreenWebviewBinding? = null
    private val binding get() = _binding!!
    private val args: WebViewScreenArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = ScreenWebviewBinding.bind(view)
        Timber.d("${BASE_URL}${args.args}")
        val url = BASE_URL + args.args
        binding.webView.webViewClient =
            MyWebViewClient(context!!, binding.progress, binding.errorAnim)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.safeBrowsingEnabled = true

        binding.webView.loadUrl(url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}