package id.xxx.module.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import id.xxx.module.data.Action

abstract class AuthActionBroadcastReceiver private constructor() : BroadcastReceiver() {

    companion object {

        internal const val ACTION = "AuthActionBroadcastReceiver.ACTION_AUTH"

        internal const val EXTRA_ACTION_MODE = "EXTRA_ACTION_MODE"

        fun register(context: Context?, onReceive: (Action) -> Unit): AuthActionBroadcastReceiver {
            val broadcastReceiver = object : AuthActionBroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.hasExtra(EXTRA_ACTION_MODE)) {
                        val action = intent.extras?.get(EXTRA_ACTION_MODE) as Action
                        onReceive(action)
                    }
                }
            }
            context?.registerReceiver(broadcastReceiver, IntentFilter(ACTION))
            return broadcastReceiver
        }
    }
}