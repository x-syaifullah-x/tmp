package id.xxx.module.data.repository

import id.xxx.module.data.Resources
import id.xxx.module.data.model.SignUpFormModel
import id.xxx.module.data.model.TokenModel
import id.xxx.module.data.type.OobType
import id.xxx.module.data.type.SignInType
import id.xxx.module.data.type.UpdateType
import kotlinx.coroutines.flow.Flow

interface SignRepository {

    fun signUp(
        form: SignUpFormModel
    ): Flow<Resources<TokenModel>>

    fun signIn(
        type: SignInType
    ): Flow<Resources<TokenModel>>

    fun sendOobCode(
        OOBType: OobType
    ): Flow<Resources<String>>

    fun resetPassword(
        oobCode: String,
        newPassword: String,
    ): Flow<Resources<String>>

    fun update(
        type: UpdateType
    ): Flow<Resources<String>>

    fun sendVerificationCode(
        phoneNumber: String, recaptchaToken: String
    ): Flow<Resources<String>>
}