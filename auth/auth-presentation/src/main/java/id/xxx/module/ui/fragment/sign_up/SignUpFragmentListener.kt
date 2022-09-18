package id.xxx.module.ui.fragment.sign_up

import id.xxx.module.annotation.KeepMethodeName

interface SignUpFragmentListener {

    @KeepMethodeName
    fun onInvoke(rc: SignUpFragmentCallback)
}