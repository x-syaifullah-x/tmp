package id.xxx.module.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import id.xxx.module.data.Action
import id.xxx.module.receiver.AuthActionBroadcastReceiver

class ActionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val intent = Intent(AuthActionBroadcastReceiver.ACTION)
        val action = Action.fromUri(this.intent.data)
        intent.putExtra(AuthActionBroadcastReceiver.EXTRA_ACTION_MODE, action)
        sendBroadcast(intent)

        finish()

        super.onCreate(savedInstanceState)
    }
}