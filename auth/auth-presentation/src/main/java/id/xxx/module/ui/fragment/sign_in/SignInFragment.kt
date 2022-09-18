package id.xxx.module.ui.fragment.sign_in

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import id.xxx.module.auth.presentation.databinding.FragmentSignInBinding
import id.xxx.module.ktx.fragment.invokeListener
import id.xxx.module.ui.base.BaseFragmentViewBindingAuth

class SignInFragment : BaseFragmentViewBindingAuth<FragmentSignInBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.etEmail.doOnTextChanged(::onTextEmailChange)
        viewBinding.etPassword.doOnTextChanged(::onTextPasswordChange)
        viewBinding.btnSignIn.setOnClickListener(::onClickSignIn)
        viewBinding.tvForgetPassword.setOnClickListener(::onClickForgetPassword)
        viewBinding.tvSignUp.setOnClickListener(::onClickSignUp)
        viewBinding.btnSignInWithPhone.setOnClickListener(::onClickSignInWithPhone)
    }

    private fun onClickSignInWithPhone(view: View) {
        invokeListener(SignInFragmentCallback.OnClickSignInWithPhone(viewBinding))
    }

    private fun onClickSignUp(view: View) {
        invokeListener(SignInFragmentCallback.OnClickSignUp(viewBinding))
    }

    private fun onClickForgetPassword(view: View) {
        invokeListener(SignInFragmentCallback.OnClickForgetPassword(viewBinding))
    }

    private fun onClickSignIn(view: View) {
        invokeListener(SignInFragmentCallback.OnClickSignIn(viewBinding))
    }

    private fun invokeListener(arg: SignInFragmentCallback) = invokeListener(
        SignInFragmentListener::class.java, SignInFragmentListener::onInvoke.name, arg
    )

    private fun onTextPasswordChange(text: CharSequence?, start: Int, before: Int, count: Int) {
        resetIfError(viewBinding.textInputLayoutPassword)
    }

    private fun onTextEmailChange(text: CharSequence?, start: Int, before: Int, count: Int) {
        resetIfError(viewBinding.textInputLayoutEmail)
    }

    private fun resetIfError(textInputLayout: TextInputLayout) {
        if (textInputLayout.error != null) textInputLayout.error = null
    }
}