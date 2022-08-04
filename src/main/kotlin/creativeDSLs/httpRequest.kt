package creativeDSLs

import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublisher
import java.time.Duration

fun main() {
    val r1 = HttpRequest.newBuilder(URI.create("https://acme.com:9876/products"))
        .GET()
        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        .timeout(Duration.ofSeconds(5L))
        .build()

    val r2 = httpRequest(URI.create("https://acme.com:9876/products")) {
        method = GET
        headers {
            "Content-Type" .. "application/x-www-form-urlencoded; charset=UTF-8"
            "Accept-Encoding" .. "gzip, deflate"
        }
        timeout = Duration.ofSeconds(5L)
    }
}

fun httpRequest(uri: URI, block: HttpRequestBuilder.() -> Unit) : HttpRequest =
    HttpRequestBuilder(uri).apply(block).build()

typealias HttpMethod = Pair<String, BodyPublisher?>

class HttpRequestBuilder(uri: URI) {

    private val peer = HttpRequest.newBuilder(uri)

    val GET: Pair<String, BodyPublisher?> = "GET" to null
    val DELETE: Pair<String, BodyPublisher?> = "DELETE" to null

    var method: HttpMethod? = null
      set(value) {
          when(value) {
              GET -> peer.GET()
              DELETE -> peer.DELETE()
              else -> peer.method(value!!.first, value.second)
          }
          field = value
      }

    var timeout: Duration? = null
      set(value) {
          peer.timeout(value)
          field = value
      }

    fun headers(block: Headers.() -> Unit) {
       Headers().apply(block)
    }

    fun build() = peer.build()

    inner class Headers {
        operator fun String.rangeTo(value: String) {
            peer.header(this@rangeTo, value)
        }
    }
}

