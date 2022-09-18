package id.xxx.module.ui.fragment.recaptcha

import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebView

interface RecaptchaFragmentCallback {

    fun onSuccess(token: String)

    fun onProgressChanged(webView: WebView?, progress: Int) {}

    fun onError() {}

    @JavascriptInterface
    fun onSubmit(token: String) {
        onSuccess(token)
    }
}