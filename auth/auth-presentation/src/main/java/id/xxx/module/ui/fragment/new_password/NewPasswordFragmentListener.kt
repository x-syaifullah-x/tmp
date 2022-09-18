package id.xxx.module.ui.fragment.new_password

import id.xxx.module.annotation.KeepMethodeName

interface NewPasswordFragmentListener {

    @KeepMethodeName
    fun onInvoke(callback: NewPasswordFragmentCallback)
}