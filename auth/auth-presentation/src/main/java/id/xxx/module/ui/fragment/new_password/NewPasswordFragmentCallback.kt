package id.xxx.module.ui.fragment.new_password

import id.xxx.module.auth.presentation.databinding.FragmentNewPasswordBinding

interface NewPasswordFragmentCallback {

    data class OnClickConfirmNewPassword(
        val viewBinding: FragmentNewPasswordBinding,
        val oobCode: String,
        val newPassword: String
    ) : NewPasswordFragmentCallback
}