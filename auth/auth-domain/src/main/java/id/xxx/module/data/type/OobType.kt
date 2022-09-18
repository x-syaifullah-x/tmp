package id.xxx.module.data.type

sealed interface OobType {

    val requestType: String

    data class PasswordReset(val email: String) : OobType {
        override val requestType = "PASSWORD_RESET"
    }

    data class VerifyEmail(val idToken: String) : OobType {
        override val requestType = "VERIFY_EMAIL"
    }
}