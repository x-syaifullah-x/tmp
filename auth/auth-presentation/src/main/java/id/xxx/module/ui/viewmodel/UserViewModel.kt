package id.xxx.module.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import id.xxx.module.data.Resources
import id.xxx.module.data.model.TokenModel
import org.json.JSONObject

class UserViewModel : ViewModel() {

    companion object {
        private const val PREF_NAME_TOKEN = "token"
        private const val KEY_TOKEN = "token_state"

        private const val NAME_LOCAL_ID = "localId"
        private const val NAME_ID_TOKEN = "idToken"
        private const val NAME_REFRESH_TOKEN = "refreshToken"
        private const val NAME_EXPIRES_IN = "expiresIn"

        private fun from(json: String?): TokenModel? {
            val j = JSONObject(json ?: return null)
            return TokenModel(
                uid = j.getString(NAME_LOCAL_ID),
                token = j.getString(NAME_ID_TOKEN),
                refreshToken = j.getString(NAME_REFRESH_TOKEN),
                expiresIn = (j.getLong(NAME_EXPIRES_IN))
            )
        }

        private fun TokenModel?.toJson(): String? {
            if (this == null) return null
            val j = JSONObject()
            j.put(NAME_LOCAL_ID, uid)
            j.put(NAME_ID_TOKEN, token)
            j.put(NAME_REFRESH_TOKEN, refreshToken)
            j.put(NAME_EXPIRES_IN, expiresIn)
            return j.toString()
        }
    }

    private val _token = MutableLiveData<Resources<TokenModel>>(Resources.Loading())

    val token: LiveData<ViewModelSingleEvent<Resources<TokenModel>>> =
        _token.map { ViewModelSingleEvent(it) }

    fun setUser(context: Context) {
        val state = getUserPref(context).getString(KEY_TOKEN, null)
        val token = from(state)
        setUser(context, token, token != null)
    }

    fun setUser(context: Context, token: TokenModel?, isRemember: Boolean) {
        if (token != null) {
            _token.value = Resources.Success(token)
        } else {
            _token.value = Resources.Failure(Throwable("NOT STATE USER"))
        }

        val edit: SharedPreferences.Editor = getUserPref(context).edit()
        if (isRemember) {
            edit.putString(KEY_TOKEN, token.toJson())
        } else {
            edit.putString(KEY_TOKEN, null)
        }
        edit.apply()
    }

    private fun getUserPref(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME_TOKEN, Context.MODE_PRIVATE)
}