package id.xxx.module.ui.fragment.request_otp

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.core.widget.doOnTextChanged
import id.xxx.module.auth.presentation.databinding.FragmentRequestOtpBinding
import id.xxx.module.ktx.fragment.invokeListener
import id.xxx.module.ui.base.BaseFragmentViewBindingAuth

class RequestOtpFragment : BaseFragmentViewBindingAuth<FragmentRequestOtpBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction()
        viewBinding.etEmail.doOnTextChanged { _, _, _, _ ->
            if (viewBinding.textInputLayoutEmail.error != null)
                viewBinding.textInputLayoutEmail.error = null
        }
        viewBinding.btnGetOtp.setOnClickListener(::onClickBtnGetOtp)
    }

    private fun onClickBtnGetOtp(@Suppress("UNUSED_PARAMETER") view: View) {
        val email = "${viewBinding.textInputLayoutEmail.editText?.text}"
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            viewBinding.textInputLayoutEmail.error = "Please enter a valid email address"
        } else {
            invokeListener(
                listenerClass = RequestOtpFragmentListener::class.java,
                methodeName = RequestOtpFragmentListener::onInvoke.name,
                RequestOtpFragmentCallback.OnClickBtnGetOtp(viewBinding, email)
            )
        }
    }
}