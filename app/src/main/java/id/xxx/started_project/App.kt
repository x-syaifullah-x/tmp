package id.xxx.started_project

import android.app.Application
import android.content.Intent
import android.util.JsonReader
import id.xxx.module.koin.Auth
import id.xxx.started_project.ui.started_activity.Activity
import org.json.JSONObject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

open class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                *Auth.modules.toTypedArray()
            )
        }

        Activity.registerReceiverAuthAction(this) { actionMode ->
            val intent = Intent(this, Activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Activity.EXTRA_ACTION_MODE, actionMode)
            startActivity(intent)
        }

//        jsonReaderTest()
    }

    private fun jsonReaderTest() {
        val key = JSONObject()
        for (i in 1..10) {
            val value = JSONObject()
            value.put("name", "xxx_$i")
            value.put("age", i * 10)
            key.put("$i", value)
        }
        val data = ByteArrayInputStream(
            key.toString().toByteArray()
        )

        val inputStreamReader = InputStreamReader(data, "UTF-8")
        val reader = JsonReader(inputStreamReader)
        reader.beginObject()

        while (reader.hasNext()) {
            try {
                println("key: ${reader.nextName()}")
                reader.beginObject()
                while (reader.hasNext()) {
                    println("${reader.nextName()}: ${reader.nextString()}")
                }
                reader.endObject()
            } catch (err: Throwable) {
                err.printStackTrace()
            }
        }
        reader.endObject()
    }
}