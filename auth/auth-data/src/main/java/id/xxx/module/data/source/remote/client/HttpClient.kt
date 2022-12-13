package id.xxx.module.data.source.remote.client

import id.xxx.module.data.source.remote.response.Header
import id.xxx.module.data.source.remote.response.Response
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.internal.headersContentLength
import java.io.ByteArrayInputStream
import java.io.InputStream

class HttpClient private constructor(private val client: OkHttpClient) {

    companion object {

        @Volatile
        private var sInstance: HttpClient? = null

        fun getInstance() = sInstance ?: synchronized(this) {
            sInstance ?: run {
                val client = OkHttpClient.Builder()
                    .build()
                HttpClient(client).also { sInstance = it }
            }
        }
    }

    fun execute(
        URL: String,
        methode: RequestMethode = RequestMethode.GET,
        requestBody: RequestBody? = null
    ): Response<InputStream> {
        val request = Request.Builder()
            .url(URL)
            .method(methode.value, requestBody)
            .build()
        val response = client.newCall(request).execute()
        return Response(
            header = Header(
                code = response.code,
                date = response.headers.getDate("date")?.time ?: 0,
                contentLength = response.headersContentLength()
            ),
            body = response.body?.byteStream() ?: ByteArrayInputStream(byteArrayOf())
        )
    }
}