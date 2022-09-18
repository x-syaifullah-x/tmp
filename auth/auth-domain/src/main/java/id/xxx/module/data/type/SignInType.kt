package id.xxx.module.data.type

sealed interface SignInType {

    data class CostumeToken(
        val token: String
    ) : SignInType

    data class Password(
        val email: String,
        val password: String
    ) : SignInType

    data class Phone(
        val sessionInfo: String,
        val code: String
    ) : SignInType
}