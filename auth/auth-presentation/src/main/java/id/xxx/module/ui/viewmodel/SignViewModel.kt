package id.xxx.module.ui.viewmodel

import androidx.lifecycle.ViewModel
import id.xxx.module.data.interactor.SignInteractor
import id.xxx.module.data.model.SignUpFormModel
import id.xxx.module.data.type.OobType
import id.xxx.module.data.type.SignInType

class SignViewModel(
    private val repo: SignInteractor
) : ViewModel() {

    fun signUp(form: SignUpFormModel) =
        repo.signUp(form)

    fun signIn(
        type: SignInType
    ) = repo.signIn(type)

    fun sendOobCode(
        email: String,
    ) = repo.sendOobCode(OobType.PasswordReset(email))

    fun resetPassword(
        code: String,
        newPassword: String,
    ) = repo.resetPassword(code, newPassword)

    fun sendVerificationCode(
        phoneNumber: String, recaptchaToken: String
    ) = repo.sendVerificationCode(phoneNumber, recaptchaToken)
}