package id.xxx.module.ui.fragment.request_otp

import id.xxx.module.auth.presentation.databinding.FragmentRequestOtpBinding

interface RequestOtpFragmentCallback {

    data class OnClickBtnGetOtp(
        val viewBinding: FragmentRequestOtpBinding,
        val email: String
    ) : RequestOtpFragmentCallback
}