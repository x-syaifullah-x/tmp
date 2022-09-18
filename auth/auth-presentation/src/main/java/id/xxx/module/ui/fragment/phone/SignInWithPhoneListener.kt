package id.xxx.module.ui.fragment.phone

import id.xxx.module.annotation.KeepMethodeName

interface SignInWithPhoneListener {

    @KeepMethodeName
    fun onInvoke(cb: SignInWithPhoneCallback)
}