package id.xxx.module.data.throwable

data class AuthThrowable(
    override val message: String,
    val code: Int,
) : Throwable(message)