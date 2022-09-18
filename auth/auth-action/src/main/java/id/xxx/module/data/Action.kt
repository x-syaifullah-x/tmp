package id.xxx.module.data

import android.net.Uri
import java.io.Serializable

interface Action : Serializable {

    companion object {
        @JvmStatic
        fun fromUri(uri: Uri?) = when (uri?.getQueryParameter("mode")) {
            "resetPassword" -> Mode.ResetPassword(uri.oobCode())
            "verifyEmail" -> Mode.VerifyEmail(uri.oobCode())
            else -> throw IllegalArgumentException()
        }

        private fun Uri.oobCode() =
            getQueryParameter("oobCode")
                ?: throw NullPointerException("required oob code")
    }

    interface Mode {

        data class ResetPassword(
            val oobCode: String
        ) : Action

        data class VerifyEmail(
            val oobCode: String
        ) : Action
    }
}