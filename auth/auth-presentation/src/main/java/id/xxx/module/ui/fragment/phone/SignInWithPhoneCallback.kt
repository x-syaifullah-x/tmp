package id.xxx.module.ui.fragment.phone

import id.xxx.module.data.Resources
import id.xxx.module.data.model.TokenModel

interface SignInWithPhoneCallback {

    data class OnClickGetOtp(
        val phoneNumber: String,
        val token: String,
        val rc: (Resources<String>) -> Unit
    ) : SignInWithPhoneCallback

    object OnClickUseEmail : SignInWithPhoneCallback

    data class OnClickConfirm(
        val sessionInfo: String,
        val code: String,
        val isRemember: Boolean,
        val rc: (Resources<TokenModel>) -> Unit
    ) : SignInWithPhoneCallback
}