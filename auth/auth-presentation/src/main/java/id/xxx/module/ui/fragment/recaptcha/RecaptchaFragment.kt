package id.xxx.module.ui.fragment.recaptcha

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.*
import id.xxx.module.auth.presentation.databinding.FragmentRecaptchaBinding
import id.xxx.module.ui.base.BaseFragmentViewBindingAuth
import java.util.*

class RecaptchaFragment : BaseFragmentViewBindingAuth<FragmentRecaptchaBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val web = viewBinding.webView

        val callback =
            if (activity is RecaptchaFragmentCallback) {
                activity as? RecaptchaFragmentCallback
            } else if (parentFragment is RecaptchaFragmentCallback) {
                parentFragment as? RecaptchaFragmentCallback
            } else {
                null
            }

        web.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                callback?.onProgressChanged(view, newProgress)
            }
        }
        web.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                callback?.onError()
            }
        }
        val webSettings = web.settings
        @SuppressLint("SetJavaScriptEnabled")
        webSettings.javaScriptEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        if (callback != null)
            web.addJavascriptInterface(callback, "dataCallback")
        val language = Locale.getDefault().language
        web.loadUrl("https://x-x-x-test.web.app/recaptcha/index.html?language=$language")
    }
}