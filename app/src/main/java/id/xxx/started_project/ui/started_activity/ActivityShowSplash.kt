@file:JvmName(Activity.CLASS_NAME)

package id.xxx.started_project.ui.started_activity

import android.content.Context
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Activity.showSplash(
    delayMillis: Long = 0,
    onShowing: suspend () -> Unit = {},
    onFinished: suspend () -> Unit = {}
) {
    lifecycleScope.launch {
        onShowing.invoke()
        val sharedPreferencesName = "${packageName}.splash.state"
        val sharedPreferencesKey = "splash.state"
        val sharedPreferences =
            getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        val oldValue = sharedPreferences.getString(sharedPreferencesKey, null)
        val newValue = (baseContext.applicationContext).toString()
        if ((oldValue != newValue))
            delay(delayMillis)
        else
            delay(300)
        sharedPreferences.edit()
            .putString(sharedPreferencesKey, newValue)
            .apply()
        onFinished.invoke()
    }
}