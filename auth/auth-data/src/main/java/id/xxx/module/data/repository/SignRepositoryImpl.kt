package id.xxx.module.data.repository

import id.xxx.module.data.Resources
import id.xxx.module.data.model.SignUpFormModel
import id.xxx.module.data.model.TokenModel
import id.xxx.module.data.source.remote.auth.email.AuthEmailDataSourceRemote
import id.xxx.module.data.source.remote.response.Header
import id.xxx.module.data.source.remote.response.Response
import id.xxx.module.data.throwable.AuthThrowable
import id.xxx.module.data.type.OobType
import id.xxx.module.data.type.SignInType
import id.xxx.module.data.type.UpdateType
import id.xxx.module.ktx.getString
import id.xxx.module.ktx.read
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.concurrent.atomic.AtomicLong

class SignRepositoryImpl private constructor(
    private val remoteDataSource: AuthEmailDataSourceRemote,
) : SignRepository {

    companion object {
        @Volatile
        private var sInstance: SignRepository? = null

        fun getInstance(remote: AuthEmailDataSourceRemote) =
            sInstance ?: synchronized(this) {
                sInstance ?: SignRepositoryImpl(remote)
                    .also { sInstance = it }
            }
    }

    override fun signUp(form: SignUpFormModel) = asResources(
        request = { remoteDataSource.signUp(form.email, form.password) },
        result = { header, response ->
            val j = JSONObject(response)
            TokenModel(
                uid = j.getString("localId"),
                token = j.getString("idToken"),
                refreshToken = j.getString("refreshToken"),
                expiresIn = (j.getLong("expiresIn") * 1000) + header.date
            )
        }
    )

    override fun signIn(type: SignInType) = asResources(
        request = { remoteDataSource.signIn(type) },
        result = { header, response ->
            val j = JSONObject(response)
            TokenModel(
                uid = j.getString("localId"),
                token = j.getString("idToken"),
                refreshToken = j.getString("refreshToken"),
                expiresIn = (j.getLong("expiresIn") * 1000) + header.date
            )
        }
    )

    override fun sendOobCode(OOBType: OobType) = asResources(
        request = { remoteDataSource.sendOobCode(OOBType) },
        result = { _, response -> response }
    )

    override fun resetPassword(oobCode: String, newPassword: String) = asResources(
        request = { remoteDataSource.resetPassword(oobCode, newPassword) },
        result = { _, response -> response }
    )

    override fun update(type: UpdateType) = asResources(
        request = { remoteDataSource.update(type) },
        result = { _, response -> response }
    )

    override fun sendVerificationCode(phoneNumber: String, recaptchaToken: String) = asResources(
        request = { remoteDataSource.sendVerificationCode(phoneNumber, recaptchaToken) },
        result = { _, response -> JSONObject(response).getString("sessionInfo") }
    )

    private fun <T> asResources(
        request: () -> Response<InputStream>,
        result: (header: Header, response: String) -> T
    ) = flow {
        val countAtomic = AtomicLong(0)
        val lengthAtomic = AtomicLong(0)
        val progress = Resources.Loading.Progress(countAtomic, lengthAtomic)
        val loading = Resources.Loading(progress)
        emit(loading)
        val response = request.invoke()
        val header = response.header
        lengthAtomic.set(header.contentLength)
        val data = response.body
        val out = ByteArrayOutputStream()
        data.read(
            onRead = { bytes ->
                val size = bytes.size
                countAtomic.set(countAtomic.get() + size)
                out.write(bytes, 0, size)
                emit(loading)
            },
            onReadComplete = {
                val outString = out.toString()
                if (header.code in 200..299) {
                    emit(Resources.Success(result.invoke(header, outString)))
                } else {
                    val error = JSONObject(outString).getJSONObject("error")
                    val message = error.getString("message", "Error")
                    val code = error.getInt("code")
                    throw AuthThrowable(message, code)
                }
            },
            onError = { e -> throw e }
        )
    }.flowOn(Dispatchers.IO)
        .catch { emit(Resources.Failure(it)) }
}