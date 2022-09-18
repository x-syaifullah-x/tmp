package id.xxx.module.data.repository

import kotlinx.coroutines.CoroutineTestRule
import id.xxx.module.data.type.OobType
import id.xxx.module.data.type.SignInType
import id.xxx.module.data.source.remote.auth.email.AuthEmailDataSourceRemote
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.Rule
import org.junit.Test

class SignRepositoryImplTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val repo = SignRepositoryImpl.getInstance(
        AuthEmailDataSourceRemote.getInstance()
    )

    @Test
    fun signIn() = runBlocking {
        repo.signIn(SignInType.Password("roottingandroid@gmail.com", "123456"))
            .collect {
                println(it)
            }
    }

    @Test
    fun sendOobCodePasswordReset() = runBlocking {
        repo.sendOobCode(OobType.PasswordReset("roottingandroid@gmail.com")).collect {
            println(it)
        }
    }

    @Test
    fun resetPassword() = runBlocking {
        val httpUrl =
            "https://auth-a22e6.firebaseapp.com/__/auth/action?mode=resetPassword&oobCode=GsoplVevgBKJFOjK7-U3YoaKVVPjQk3w4tbsjlCRD2gAAAGEG25BVA&apiKey=AIzaSyAMGBhmVsmDHfBgM3bdfp-K72zdpPd9kHs&lang=en"
                .toHttpUrl()
        val oobCode = httpUrl.queryParameter("oobCode") ?: ""
        repo.resetPassword(oobCode, "123456").collect {
            println(it)
        }
    }
}