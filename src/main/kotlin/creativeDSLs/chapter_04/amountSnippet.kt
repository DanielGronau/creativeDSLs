package creativeDSLs.chapter_04

import java.math.BigDecimal

data class Amount(val value: BigDecimal, val currency: String)

val Double.GBP
    get() = Currency(this.toBigDecimal(), "GBP")

// with an extension function, this would be 22.46.GBP()
val money = 22.46.GBP