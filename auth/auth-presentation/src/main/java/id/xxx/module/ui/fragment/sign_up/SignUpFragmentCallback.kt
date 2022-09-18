package id.xxx.module.ui.fragment.sign_up

import id.xxx.module.auth.presentation.databinding.FragmentSignUpBinding

interface SignUpFragmentCallback {

    data class OnClickButtonSignUp(
        val binding: FragmentSignUpBinding,
    ) : SignUpFragmentCallback
}