package creativeDSLs.chapter_07.autoDsl

import com.faendir.kotlin.autodsl.AutoDsl
import com.faendir.kotlin.autodsl.AutoDslSingular
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.time.Duration

typealias HttpMethod = Pair<String, HttpRequest.BodyPublisher?>

val GET: HttpMethod = "GET" to null
val DELETE: HttpMethod = "DELETE" to null
fun PUT(bp: HttpRequest.BodyPublisher): HttpMethod = "PUT" to bp
fun POST(bp: HttpRequest.BodyPublisher): HttpMethod = "POST" to bp

@AutoDsl
data class Header(val key: String, val value: String)

@AutoDsl
data class HttpRequestBuilder(
    val uri: URI,
    val method: HttpMethod = GET,
    val timeout: Duration? = null,
    val expectContinue: Boolean? = null,
    val version: HttpClient.Version? = null,
    @AutoDslSingular("header")
    val headers: List<Header> = listOf()
) {
    fun build(): HttpRequest =
        with(HttpRequest.newBuilder(uri)) {
            headers.forEach { (key, value) -> header(key, value) }
            timeout?.let { timeout(it) }
            expectContinue?.let { expectContinue(it) }
            version?.let { version(it) }
            method.let {
                when (method) {
                    GET -> GET()
                    DELETE -> DELETE()
                    else -> method(method.first, method.second)
                }
            }
            this.build()
        }
}

fun main() {
    val request = httpRequestBuilder {
        uri = URI.create("https://acme.com:9876/products")
        method = GET
        header {
            key = "Content-Type"
            value = "application/x-www-form-urlencoded; charset=UTF-8"
        }
        header {
            key = "Accept-Encoding"
            value = "gzip, deflate"
        }
        timeout = Duration.ofSeconds(5)
    }.build()
}
