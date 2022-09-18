package id.xxx.module.ui.fragment.new_password

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import id.xxx.module.auth.presentation.databinding.FragmentNewPasswordBinding
import id.xxx.module.ktx.fragment.invokeListener
import id.xxx.module.ui.base.BaseFragmentViewBindingAuth

class NewPasswordFragment : BaseFragmentViewBindingAuth<FragmentNewPasswordBinding>() {

    companion object {
        const val DATA_EXTRA_OOB_CODE = "DATA EXTRA OOB CODE"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val oobCode = arguments
            ?.getString(DATA_EXTRA_OOB_CODE)
            ?: throw Throwable("${this::class.java.simpleName} required oob code")

        viewBinding.etPassword.doOnTextChanged { _, _, _, _ ->
            resetIfError(viewBinding.textInputLayoutNewPassword)
        }

        viewBinding.etPasswordConfirm.doOnTextChanged { _, _, _, _ ->
            resetIfError(viewBinding.textInputLayoutNewPasswordConfirm)
        }
        viewBinding.btnConfirm.setOnClickListener {
            onClickConfirm(
                oobCode,
                viewBinding.textInputLayoutNewPassword,
                viewBinding.textInputLayoutNewPasswordConfirm
            )
        }
    }

    private fun resetIfError(textInputLayout: TextInputLayout) {
        if (textInputLayout.error != null) textInputLayout.error = null
    }

    private fun onClickConfirm(
        oobCode: String,
        newPassword: TextInputLayout,
        newPasswordConfirm: TextInputLayout
    ) {
        val textNewPassword = "${newPassword.editText?.text}"
        val textNewPasswordConfirm = "${newPasswordConfirm.editText?.text}"

        if (textNewPassword.isEmpty()) {
            newPassword.error = "Please input new password"
            return
        }

        if (textNewPasswordConfirm.isEmpty()) {
            newPasswordConfirm.error = "Please input confirm password"
            return
        }

        if (textNewPassword != textNewPasswordConfirm) {
            newPasswordConfirm.error = "Password confirm is not the same"
            return
        }

        invokeListener(
            NewPasswordFragmentListener::class.java,
            NewPasswordFragmentListener::onInvoke.name,
            NewPasswordFragmentCallback.OnClickConfirmNewPassword(
                viewBinding,
                oobCode = oobCode,
                newPassword = textNewPassword
            )
        )
    }
}