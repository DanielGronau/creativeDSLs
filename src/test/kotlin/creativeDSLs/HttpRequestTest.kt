package creativeDSLs

import creativeDSLs.chapter_07.httpRequest
import org.junit.jupiter.api.Test
import java.net.URI
import java.time.temporal.ChronoUnit.*

class HttpRequestTest {

    @Test
    fun test() {
        val request = httpRequest(URI.create("http://acme.com:9876/products")) {
            method = GET
            timeout = 5 * SECONDS
            headers {
                "Content-Type" .. "application/x-www-form-urlencoded; charset=UTF-8"
                "Accept-Encoding" .. "gzip, deflate"
            }
        }
    }
}