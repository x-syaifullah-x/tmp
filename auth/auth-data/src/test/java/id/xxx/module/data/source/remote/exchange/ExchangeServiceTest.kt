package id.xxx.module.data.source.remote.exchange

import id.xxx.module.data.source.remote.auth.exchange.AuthExchangeService
import id.xxx.module.ktx.read
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.ByteArrayOutputStream

class ExchangeServiceTest {

    private val service = AuthExchangeService()

    @Test
    fun test(): Unit = runBlocking {
        val mockToken =
            "AOEOulbfDXjpyux9RxYSIUFqQ6AoH4_l4ntOYmRg7tYnUPnl1G5dwIcHxxo53gdcKV0YUgZv54-8m52S33iMS8hxUN6mMaqa3xWmK-5s1SmnRfQXCfs5ZJUXqotyWGO9gwXC8HOHvhJk3X1N81N4ffz5znVFrcn9QkabtAdtdu9PpXcYr9bZu6XL8MCGBaUqivTuTOhYoCT_gCYT3typu6mq8JWLz-aLFQ"
        val response = service.token(mockToken)

        val out = ByteArrayOutputStream()

        println(response.header)
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
    }
}