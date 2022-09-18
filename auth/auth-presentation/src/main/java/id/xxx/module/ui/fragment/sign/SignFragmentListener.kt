package id.xxx.module.ui.fragment.sign

import id.xxx.module.annotation.KeepMethodeName
import id.xxx.module.data.model.TokenModel

interface SignFragmentListener {

    @KeepMethodeName
    fun onSignInSuccess(result: TokenModel, isRemember: Boolean)

    @KeepMethodeName
    fun onSignUpSuccess(result: TokenModel, isRemember: Boolean)
}