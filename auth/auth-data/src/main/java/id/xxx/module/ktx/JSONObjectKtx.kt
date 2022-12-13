package id.xxx.module.ktx

import okhttp3.MediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

fun JSONObject.toRequestBody(contentType: MediaType? = null) =
    toString().toRequestBody(contentType)

fun JSONObject.getString(name: String, defaultValue: String): String =
    try {
        getString(name)
    } catch (err: Throwable) {
        defaultValue
    }

fun JSONObject.getBoolean(name: String, defaultValue: Boolean): Boolean =
    try {
        getBoolean(name)
    } catch (err: Throwable) {
        defaultValue
    }