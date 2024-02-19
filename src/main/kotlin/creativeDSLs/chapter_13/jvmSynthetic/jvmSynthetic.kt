package creativeDSLs.chapter_13.jvmSynthetic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.EmptyCoroutineContext

@JvmSynthetic
suspend fun getStuff(): String {
    TODO()
}

private val scope = CoroutineScope(EmptyCoroutineContext)

fun getStuffForJava(): CompletableFuture<String> =
    scope.future { getStuff() }