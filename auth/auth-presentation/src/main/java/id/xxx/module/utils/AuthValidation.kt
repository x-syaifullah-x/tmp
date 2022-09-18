package id.xxx.module.utils

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

object AuthValidation {

    fun email(etEmail: EditText): Boolean {
        val textInputLayout = etEmail.parent.parent as? TextInputLayout
        if (etEmail.text.isEmpty()) {
            val message = "Pleas input your email"
            if (textInputLayout != null) {
                textInputLayout.error = message
                textInputLayout.requestFocus()
            } else {
                etEmail.error = message
                etEmail.requestFocus()
            }
            return false
        }
        return true
    }

    fun password(etPassword: EditText): Boolean {
        val textInputLayout = etPassword.parent.parent as? TextInputLayout
        if (etPassword.text.isEmpty()) {
            val message = "Pleas input your password"
            if (textInputLayout != null) {
                textInputLayout.error = message
                textInputLayout.requestFocus()
            } else {
                etPassword.error = message
                etPassword.requestFocus()
            }
            return false
        }
        return true
    }
}