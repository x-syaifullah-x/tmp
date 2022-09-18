package id.xxx.module.data.source.remote.response

data class Header internal constructor(
    val code: Int,
    val date: Long,
    val contentLength: Long
)