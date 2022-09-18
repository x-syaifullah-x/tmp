package id.xxx.module.ui.fragment.request_otp

import id.xxx.module.annotation.KeepMethodeName

interface RequestOtpFragmentListener {

    @KeepMethodeName
    fun onInvoke(callback: RequestOtpFragmentCallback)
}