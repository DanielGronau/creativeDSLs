package creativeDSLs

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublisher
import java.time.Duration
import java.time.temporal.TemporalUnit

fun httpRequest(uri: URI, block: HttpRequestBuilder.() -> Unit): HttpRequest =
    HttpRequestBuilder(uri).apply(block).build()

fun httpRequest(uri: String, block: HttpRequestBuilder.() -> Unit): HttpRequest =
    HttpRequestBuilder(URI.create(uri)).apply(block).build()

typealias HttpMethod = Pair<String, BodyPublisher?>

@DslMarker
annotation class HttpRequestDsl

@HttpRequestDsl
class HttpRequestBuilder(var uri: URI) {

    var method: HttpMethod? = null
    var timeout: Duration? = null
    var expectContinue: Boolean? = null
    var version: HttpClient.Version? = null
    private val headers = mutableMapOf<String, String>()

    val GET: HttpMethod = "GET" to null
    val DELETE: HttpMethod = "DELETE" to null
    fun PUT(bp: BodyPublisher): HttpMethod = "PUT" to bp
    fun POST(bp: BodyPublisher): HttpMethod = "POST" to bp

    fun headers(block: Headers.() -> Unit) {
        Headers().apply(block)
    }

    fun build(): HttpRequest =
        with(HttpRequest.newBuilder(uri)) {
            headers.forEach { (key, value) -> header(key, value) }
            timeout?.let { timeout(it) }
            expectContinue?.let { expectContinue(it) }
            version?.let { version(it) }
            method?.let {
                when (method) {
                    GET -> GET()
                    DELETE -> DELETE()
                    else -> method(method!!.first, method!!.second)
                }
            }
            this.build()
        }

    @HttpRequestDsl
    inner class Headers {
        operator fun String.rangeTo(value: String) {
            this@HttpRequestBuilder.headers[this@rangeTo] = value
        }
    }

    operator fun Long.times(unit: TemporalUnit): Duration =
        Duration.of(this, unit)

    operator fun Int.times(unit: TemporalUnit): Duration =
        Duration.of(this.toLong(), unit)
}
