package creativeDSLs.chapter_04

import java.math.BigDecimal

interface Euro
interface BritishPound

data class Currency<T>(val value: BigDecimal)

val Double.EUR
    get() = Currency<Euro>(this.toBigDecimal())
val Double.GBP
    get() = Currency<BritishPound>(this.toBigDecimal())

operator fun <T> Currency<T>.plus(that: Currency<T>) =
    copy(value = this.value + that.value)

val works = 3.1.EUR + 4.5.EUR // 7.6 €
val worksToo = 2.1.GBP + 4.2.GBP // 6.3 £

//this doesn't compile:
//val oops = 3.1.EUR + 4.5.GBP