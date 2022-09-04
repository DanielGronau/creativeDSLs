package creativeDSLs.chapter_04

import java.math.BigDecimal

data class Amount(val value: BigDecimal, val currency: String)

val Double.USD
    get() = Amount(this.toBigDecimal(), "USD")

// with an extension function, this would be 22.46.USD()
val money = 22.46.USD