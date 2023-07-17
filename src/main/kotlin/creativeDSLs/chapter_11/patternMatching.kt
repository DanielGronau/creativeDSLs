package creativeDSLs.chapter_11

import java.io.Serializable
import kotlin.reflect.KClass

fun interface Pattern<in P> : (P) -> Boolean

data class MatchResult<T>(val value: T)

class Matcher<P, T>(private val obj: P) {

    private var result: T? = null

    infix fun Pattern<P>.then(value: () -> T) {
        if (result == null && this(obj)) {
            result = value()
        }
    }

    fun otherwise(default: () -> T) = MatchResult(result ?: default())
}

class Capture<P : Any> : Pattern<P> {

    lateinit var value: P
        private set

    override fun invoke(obj: P) = true.also { value = obj }
}

inline fun <reified P : Any> capture() = Capture<P>()

fun <P, T> match(obj: P, body: Matcher<P, T>.() -> MatchResult<T>): T =
    Matcher<P, T>(obj).run(body).value

fun <P> any() = Pattern<P> { true }

fun <P> none() = Pattern<P> { false }

fun <P> isNull() = Pattern<P> { it == null }

operator fun <P> Pattern<P>.not() = Pattern<P> { !this@not(it) }

infix fun <P> Pattern<P>.and(that: Pattern<P>) = Pattern<P> { this@and(it) && that(it) }

infix fun <P> Pattern<P>.or(that: Pattern<P>) = Pattern<P> { this@or(it) || that(it) }

fun <P> eq(value: P) = Pattern<P> { it == value }

fun <P> oneOf(vararg values: P) = Pattern<P> { it in values }

fun <P> isA(kClass: KClass<*>) = Pattern<P> { kClass.isInstance(it) }

fun <P> isSame(value: P) = Pattern<P> { it === value }

inline fun <reified C : Comparable<C>> gt(value: C) = Pattern<C> { it > value }

inline fun <reified C : Comparable<C>> ge(value: C) = Pattern<C> { it >= value }

inline fun <reified C : Comparable<C>> lt(value: C) = Pattern<C> { it < value }

inline fun <reified C : Comparable<C>> le(value: C) = Pattern<C> { it <= value }

data class Person(val firstName: String, val lastName: String, val age: Int)

fun person(
    firstNamePattern: Pattern<String>,
    lastNamePattern: Pattern<String>,
    agePattern: Pattern<Int>
) = Pattern<Person?> {
    when (it) {
        null -> false
        else -> firstNamePattern(it.firstName) &&
                lastNamePattern(it.lastName) &&
                agePattern(it.age)
    }
}

fun main() {
    //val p: Person? = null
    val p: Person = Person("Andy", "Smith", 42)

    val result = match(p) {
        person(oneOf("Andy", "Mike"), eq("Miller"), any()) then
                { "One of the Miller brothers" }

        person(any(), isA(Serializable::class), gt(50)) then
                { "An old person!" }

        val ageCap = capture<Int>()
        person(eq("Andy"), !eq("Miller"), ageCap) then
                { "Some unknown Andy of age ${ageCap.value}" }

        isNull<Person>() then { "Null-Value" }

        otherwise { "Some unknown person" }
    }

    println(result)
}