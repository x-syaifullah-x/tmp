package id.xxx.module.data.source.remote.response

data class Response<T> internal constructor(
    val header: Header,
    val body: T
)