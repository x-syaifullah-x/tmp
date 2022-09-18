package id.xxx.module.ui.fragment.sign_in

import id.xxx.module.annotation.KeepMethodeName

interface SignInFragmentListener {

    @KeepMethodeName
    fun onInvoke(callback: SignInFragmentCallback)
}