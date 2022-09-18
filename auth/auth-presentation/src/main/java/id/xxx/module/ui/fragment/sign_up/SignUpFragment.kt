package id.xxx.module.ui.fragment.sign_up

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.core.widget.doOnTextChanged
import id.xxx.module.auth.presentation.databinding.FragmentSignUpBinding
import id.xxx.module.ktx.fragment.invokeListener
import id.xxx.module.ui.base.BaseFragmentViewBindingAuth

class SignUpFragment : BaseFragmentViewBindingAuth<FragmentSignUpBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutInputEmail = viewBinding.textInputLayoutEmail
        layoutInputEmail.editText?.doOnTextChanged { text, start, before, count ->
            if (layoutInputEmail.error != null) {
                layoutInputEmail.error = null
            }
        }

        val layoutInputPassword = viewBinding.textInputLayoutPassword
        layoutInputPassword.editText?.doOnTextChanged { text, start, before, count ->
            if (layoutInputPassword.error != null) {
                layoutInputPassword.error = null
            }
        }

        val layoutInputPasswordConfirm = viewBinding.textInputLayoutPasswordConfirm
        layoutInputPasswordConfirm.editText?.doOnTextChanged { text, start, before, count ->
            if (layoutInputPasswordConfirm.error != null) {
                layoutInputPasswordConfirm.error = null
            }
        }
        viewBinding.btnSignUp.setOnClickListener { onClickButtonSignUp() }
        viewBinding.tvSignIn.setOnClickListener { requireActivity().onBackPressed() }
    }

    private fun onClickButtonSignUp() {
        val layoutInputEmail = viewBinding.textInputLayoutEmail
        val email = "${layoutInputEmail.editText?.text}"
        if (email.isEmpty()) {
            layoutInputEmail.error = "Enter email address."
            layoutInputEmail.requestFocus()
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layoutInputEmail.error = "Enter a valid email address."
            layoutInputEmail.requestFocus()
            return
        }

        val layoutInputPassword = viewBinding.textInputLayoutPassword
        val password = "${layoutInputPassword.editText?.text}"
        if (password.isEmpty()) {
            layoutInputPassword.error = "Enter password."
            layoutInputPassword.requestFocus()
            return
        } else if (password.length < 6) {
            layoutInputPassword.error = "Password must be 6 characters or more."
            layoutInputPassword.requestFocus()
            return
        }

        val layoutInputPasswordConfirm = viewBinding.textInputLayoutPasswordConfirm
        val passwordConfirm = "${layoutInputPasswordConfirm.editText?.text}"
        if (passwordConfirm.isEmpty()) {
            layoutInputPasswordConfirm.error = "Confirm your password."
            layoutInputPasswordConfirm.requestFocus()
            return
        } else if (password != passwordConfirm) {
            layoutInputPasswordConfirm.error = "The passwords don't match. Try again."
            layoutInputPasswordConfirm.requestFocus()
            return
        }

        invokeListener(
            listenerClass = SignUpFragmentListener::class.java,
            methodeName = SignUpFragmentListener::onInvoke.name,
            args = arrayOf(SignUpFragmentCallback.OnClickButtonSignUp(viewBinding))
        )
    }
}