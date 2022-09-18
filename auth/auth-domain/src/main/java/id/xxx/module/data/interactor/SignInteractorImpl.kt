package id.xxx.module.data.interactor

import id.xxx.module.data.model.SignUpFormModel
import id.xxx.module.data.repository.SignRepository
import id.xxx.module.data.type.OobType
import id.xxx.module.data.type.SignInType
import id.xxx.module.data.type.UpdateType

class SignInteractorImpl(
    private val repo: SignRepository
) : SignInteractor {

    companion object {

        @Volatile
        private var sInstance: SignInteractor? = null

        fun getInstance(repo: SignRepository) =
            sInstance ?: synchronized(this) {
                sInstance ?: SignInteractorImpl(repo)
                    .also { sInstance = it }
            }
    }

    override fun signUp(
        form: SignUpFormModel
    ) = repo.signUp(form)

    override fun signIn(
        type: SignInType
    ) = repo.signIn(type)

    override fun sendOobCode(
        type: OobType
    ) = repo.sendOobCode(type)

    override fun resetPassword(
        oobCode: String,
        newPassword: String,
    ) = repo.resetPassword(oobCode, newPassword)

    override fun update(
        type: UpdateType
    ) = repo.update(type)

    override fun sendVerificationCode(
        phoneNumber: String,
        recaptchaToken: String
    ) = repo.sendVerificationCode(phoneNumber, recaptchaToken)
}