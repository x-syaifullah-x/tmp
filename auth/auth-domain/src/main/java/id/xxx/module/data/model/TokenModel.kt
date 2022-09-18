package id.xxx.module.data.model

class TokenModel(
    val uid: String,
    val token: String,
    val refreshToken: String,
    val expiresIn: Long
) : java.io.Serializable