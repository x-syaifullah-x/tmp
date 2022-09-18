package id.xxx.module.ui.fragment.sign_in

import id.xxx.module.auth.presentation.databinding.FragmentSignInBinding

interface SignInFragmentCallback {

    class OnClickForgetPassword(
        val viewBiding: FragmentSignInBinding,
    ) : SignInFragmentCallback

    class OnClickSignUp(
        val viewBiding: FragmentSignInBinding,
    ) : SignInFragmentCallback

    class OnClickSignIn(
        val viewBiding: FragmentSignInBinding,
    ) : SignInFragmentCallback

    class OnClickSignInWithPhone(
        val viewBiding: FragmentSignInBinding,
    ) : SignInFragmentCallback
}