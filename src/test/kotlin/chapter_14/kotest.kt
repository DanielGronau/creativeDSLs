package chapter_14

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class LogicTest : StringSpec({
    val xorTable = listOf(
        Triple(true, true, false),
        Triple(true, false, true),
        Triple(false, true, true),
        Triple(false, false, false)
    )

    for ((x, y, z) in xorTable) {
        "'$x' xor '$y' should be '$z'" {
            x xor y shouldBe z
        }
    }
})

