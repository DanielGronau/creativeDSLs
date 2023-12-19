package creativeDSLs.chapter_12.patterns

import java.io.Serializable
import kotlin.reflect.KClass

typealias Pattern<P> = (P) -> Boolean

interface MatchResult<T : Any> {
    val value: T
}

class Matcher<P, T : Any>(private val obj: P) {

    private var result: T? = null

    infix fun Pattern<P>.then(value: () -> T) {
        if (result == null && this(obj)) {
            result = value()
        }
    }

    fun otherwise(default: () -> T) = object : MatchResult<T> {
        override val value = result ?: default()
    }

    operator fun Any.unaryPlus() = eq(this)
}

fun <P, T : Any> match(obj: P, body: Matcher<P, T>.() -> MatchResult<T>): T =
    Matcher<P, T>(obj).run(body).value

class Capture<P : Any> : Pattern<P> {

    lateinit var value: P
        private set

    override fun invoke(obj: P) = true.also { value = obj }
}

inline fun <reified P : Any> capture() = Capture<P>()

fun <P> any(): Pattern<P> = { true }

fun <P> none(): Pattern<P> = { false }

fun <P> isNull(): Pattern<P> = { it == null }

operator fun <P> Pattern<P>.not(): Pattern<P> = { !this@not(it) }

infix fun <P> Pattern<P>.and(that: Pattern<P>): Pattern<P> = { this@and(it) && that(it) }

infix fun <P> Pattern<P>.or(that: Pattern<P>): Pattern<P> = { this@or(it) || that(it) }

fun <P> eq(value: P): Pattern<P> = { it == value }

fun <P> oneOf(vararg values: P): Pattern<P> = { it in values }

fun <P> isA(kClass: KClass<*>): Pattern<P> = { kClass.isInstance(it) }

fun <P> isSame(value: P): Pattern<P> = { it === value }

inline fun <reified C : Comparable<C>> gt(value: C): Pattern<C> = { it > value }

inline fun <reified C : Comparable<C>> ge(value: C): Pattern<C> = { it >= value }

inline fun <reified C : Comparable<C>> lt(value: C): Pattern<C> = { it < value }

inline fun <reified C : Comparable<C>> le(value: C): Pattern<C> = { it <= value }

fun <P> all(p: Pattern<P>) : Pattern<Iterable<P>> = { it.all(p) }
fun <P> any(p: Pattern<P>) : Pattern<Iterable<P>> = { it.any(p) }
fun <P> none(p: Pattern<P>) : Pattern<Iterable<P>> = { it.none(p) }

data class Person(val firstName: String, val lastName: String, val age: Int)

fun person(
    firstNamePattern: Pattern<String>,
    lastNamePattern: Pattern<String>,
    agePattern: Pattern<Int>
): Pattern<Person?> = {
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

    val r = match(listOf(1,2,4, 42)) {
        all(lt(10) or eq(42)) then { "Only small elements" }
        otherwise { "no match" }
    }

    println(r)
}