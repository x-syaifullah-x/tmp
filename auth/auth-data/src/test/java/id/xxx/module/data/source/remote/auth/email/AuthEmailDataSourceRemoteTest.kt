package id.xxx.module.data.source.remote.auth.email

import id.xxx.module.data.source.remote.response.Response
import id.xxx.module.data.type.OobType
import id.xxx.module.data.type.SignInType
import id.xxx.module.data.type.UpdateType
import id.xxx.module.ktx.read
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.json.JSONObject
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.InputStream

internal class AuthEmailDataSourceRemoteTest {

    private val dataSource = AuthEmailDataSourceRemote.getInstance()

    @Test
    fun sendVerificationCode() = runBlocking {
        val response = dataSource
            .sendVerificationCode(
                "+111111111111",
                "03AIIukzgXw24WzmsL2a32wSzZtIpifD2GLo2Q7NqNyCNNuotuZUSkx6_XTC45_M-hDuabJz9j2RBRV0IPIVIYDwelT8lk7XW8xHBeb2nwo7sFVp6j8wBh5boZMipDYfycLoNFZr8ek8hNEy-a_uIz1yn3qqERR4ZQ8bTORSne2cM0gaLa2u1QSKfzy76DsjwluA41LGlmvlnVgkQvQ7FIycW5fuGCy0japLIMGL6hezbbClhv9CLLmRa1GvTCf_FRTYqeIvGm_xiIB21ndKLprz_rfiaUE8vg773WK131CrBct056P9uYdvmREEpdpCkY3nsq97XsN_2gQnoiOMYE2lZ0o5VyX9IA2BeSBwe3fGCleNeSWBtI4HuhETfuqyOpuBgPqg8-pbAllnlDtz9RrZ6p1SZPFFJZDUrQRa4oT3IBMrtbUDJgy7xwH-hY-uNCtD4f-e6gHUnEfPFuzJRG17fWIEu5G__WP1SXUWqtZ_Jchn0FuBU1dK6_mLwN9DE2lRblJjprtXDbNfjObipQHAxJ1f3lrl31H4gYgtqobvx5EFeR74TQZU4"
            )
        val read = read(response)
        val j = JSONObject(read)
        val sessionInfo = j.getString("sessionInfo")
        signInWithPhoneNumber(sessionInfo, "123456")
    }

    @Suppress("SameParameterValue")
    private fun signInWithPhoneNumber(
        sessionInfo: String,
        code: String
    ) = runBlocking<Unit> {
        val type = SignInType.Phone(sessionInfo, code)
        val response = dataSource.signIn(type)
        read(response)
    }

    @Test
    fun verifyEmail() = runBlocking<Unit> {
        val oobCode = "cdq-ZYcE5SmjdUQE8MgUNkAI1HVgpEdlvIRjd_lAGY0AAAGELudZUg"
        val type = UpdateType.ConfirmEmailVerification(oobCode)
        val response = dataSource.update(type)
        read(response)
    }

    @Test
    fun lookup() = runBlocking<Unit> {
        val response = dataSource.lookup(
            "eyJhbGciOiJSUzI1NiIsImtpZCI6IjNmNjcyNDYxOTk4YjJiMzMyYWQ4MTY0ZTFiM2JlN2VkYTY4NDZiMzciLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vYXV0aC1hMjJlNiIsImF1ZCI6ImF1dGgtYTIyZTYiLCJhdXRoX3RpbWUiOjE2NjcxMDU2MDIsInVzZXJfaWQiOiIxIiwic3ViIjoiMSIsImlhdCI6MTY2NzEwNTYwMiwiZXhwIjoxNjY3MTA5MjAyLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7fSwic2lnbl9pbl9wcm92aWRlciI6ImN1c3RvbSJ9fQ.Z5QUBfD78fvQ94Upm8D6kiHVWZ8v_RjClGFiRm-AJ3Eq-ANXFRpNjizXtM3FHm1x2uE14z6sWCVSDUx5mSxsHMcKvMSjr59EpKn0MEkJ_f4-vgW7maw1mEYzuGmkUodg4hOra2FVzkLyooE0h21XLGzpcZI0v3QpXPqU2rX1fMoQnNXIhRs3drsGxOB0Jf1fsEq1or2j3Va1JkD_WFaf3YhU3QrxFs1NuAUv0crmzF1-kJZ3I-l7qj_Xcq9RLF8Tzdj7m-fiAiVd3MzFvcvHZDTiGTD1wCq7Bo1Gn3g3kix9ScD5kyRjL2eaXF_CvrOpsBbI4iV-9og73MpFzP2uEA"
        )
        read(response)
    }

    @Test
    fun confirmResetPassword() = runBlocking<Unit> {
        val uri =
            "https://auth-a22e6.firebaseapp.com/__/auth/action?mode=resetPassword&oobCode=82Kd3y-QjE02vJbjWfT7E_blNMVpuXDv9VRZD1SSOhkAAAGEG0bkwg&apiKey=AIzaSyAMGBhmVsmDHfBgM3bdfp-K72zdpPd9kHs&lang=en"
                .toHttpUrl()
        val oobCode = uri.queryParameter("oobCode") ?: throw Throwable("not oob code")
        val response = dataSource.resetPassword(oobCode, "123456")
        read(response)
    }

    @Test
    fun sendOobCodeVerifyEmail() = runBlocking<Unit> {
        val token =
            "eyJhbGciOiJSUzI1NiIsImtpZCI6ImRjMzdkNTkzNjVjNjIyOGI4Y2NkYWNhNTM2MGFjMjRkMDQxNWMxZWEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vYXV0aC1hMjJlNiIsImF1ZCI6ImF1dGgtYTIyZTYiLCJhdXRoX3RpbWUiOjE2NjcyMzI0ODQsInVzZXJfaWQiOiI4bUVya3FXZm5SVjNSNXF3Zlk4RFlURFRQNWEyIiwic3ViIjoiOG1FcmtxV2ZuUlYzUjVxd2ZZOERZVERUUDVhMiIsImlhdCI6MTY2NzIzMjQ4NCwiZXhwIjoxNjY3MjM2MDg0LCJlbWFpbCI6InJvb3R0aW5nYW5kcm9pZEBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJyb290dGluZ2FuZHJvaWRAZ21haWwuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.vZLqTR4Cb0GHIPy1tx6voQs7ZCAdGIIhpAs-jQIeZenxTdXVEcWomkvtNOzdE5nzxBaC0IO5zg07G_H9sxjk9qHnHD5LYQo75CvnY69D3JVQUVX9m_lJIPrnSY3s3ckfrJo8XGXQ4Uq98NV61PYPqb_g6NDZMSgCcqBs5EJVNbt76koTpGgXJob_ZFYWri1kCHeipjnPCRoCLpXVFK0htFCm06YrwyWvqV7oE_JrIAPYWIDYfrJblZSwJbb_Jn7OtGI4XJApu72MCHW6WHmxQpXplRNkGUxbCXoBcwjMIGCk80liGy2sRwHxNMjHXz_-Ki-t4vYtwuXGKn77tGDAFA"
        val response = dataSource
            .sendOobCode(OobType.VerifyEmail(token))
        read(response)
    }

    @Test
    fun sendOobCodePasswordReset() = runBlocking<Unit> {
        val response = dataSource
            .sendOobCode(OobType.PasswordReset("roottingandroid@gmail.com"))
        read(response)
    }

    @Test
    fun signUp() = runBlocking<Unit> {
        val response = dataSource
            .signUp("a@gmail.com", "123456")
        read(response)
    }

    @Test
    fun signInWithCostumeToken() = runBlocking<Unit> {
        val token =
            "eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL2lkZW50aXR5dG9vbGtpdC5nb29nbGVhcGlzLmNvbS9nb29nbGUuaWRlbnRpdHkuaWRlbnRpdHl0b29sa2l0LnYxLklkZW50aXR5VG9vbGtpdCIsImV4cCI6MTY2NzE0NzMwMywiaWF0IjoxNjY3MTQzNzAzLCJpc3MiOiJmaXJlYmFzZS1hZG1pbnNkay01MXdvYUBhdXRoLWEyMmU2LmlhbS5nc2VydmljZWFjY291bnQuY29tIiwic3ViIjoiZmlyZWJhc2UtYWRtaW5zZGstNTF3b2FAYXV0aC1hMjJlNi5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsInVpZCI6IjEifQ.OXe4beecyOSovN-E9JqLEHOVxPw9haiiMcd9A4udHbR1H6PvFJ_ujAh6gmaIG7SYNZR_SIJxCQeCXiskGVWNcItilS_e6tjw5a9dXdBuPZLRriqEpgnwKIT_kBXm-O8goyLyPWCUYUCEZRrxbtc7wYbkgja3oGuSXQaBkEpIthOuiqBdnjGnrxDm1CFcU_OeUCpKIYIk6hvg2uzli3Cqe69zzsuVfuNr2iq2iX8n2WHh8S6ewlG_l3sAj0t8NucP95IX8gTCQUMxJ26830AxxkZ-hX1sKqBQ7A4Y76GU19Wwu-HTVeobWAcdyKtum6zuUV7yTIJYnSGk5Vdr_Td7Wg"
        val response = dataSource.signIn(SignInType.CostumeToken(token))
        read(response)
    }

    @Test
    fun signInWithPassword() = runBlocking<Unit> {
        val response = dataSource.signIn(
            SignInType.Password("roottingandroid@gmail.com", "123456")
        )
        read(response)
    }

    private suspend fun read(response: Response<InputStream>): String {
        println(response.header)

        val out = ByteArrayOutputStream()

        response.body.read(
            bufferSize = 10,
            onRead = { bytes ->
                out.write(bytes, 0, bytes.size)
            },
            onReadComplete = {
                println("onComplete: $out")
            },
            onError = { err ->
                err.printStackTrace()
            }
        )
        return out.toString()
    }
}