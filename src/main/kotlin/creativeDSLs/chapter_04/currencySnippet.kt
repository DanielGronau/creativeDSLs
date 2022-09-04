package creativeDSLs.chapter_04

import java.math.BigDecimal

object Eur
object Usd

data class Currency<T>(val value: BigDecimal, val type: T)

val Double.EUR
    get() = Currency(this.toBigDecimal(), Eur)
val Double.USD
    get() = Currency(this.toBigDecimal(), Usd)

operator fun <T> Currency<T>.plus(that: Currency<T>) =
    copy(value = this.value + that.value)

val works = 3.1.EUR + 4.5.EUR // 7.6 €

//this doesn't compile:
//val oops = 3.1.EUR + 4.5.USD