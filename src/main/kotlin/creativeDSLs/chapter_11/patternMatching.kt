package creativeDSLs.chapter_11

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.cast
import kotlin.reflect.full.memberFunctions

fun interface Pattern : (Any?) -> Boolean

data class MatchResult<T>(val value: T)

class Matcher<T>(private val obj: Any?) {
    private val captures = mutableMapOf<String, Any?>()
    private var result: T? = null

    fun otherwise(default: () -> T) = MatchResult(result ?: default())

    infix fun Pattern.then(value: () -> T) {
        if (result == null && this(obj)) {
            result = value()
        }
    }

}

class Capture<T : Any>(val kclass: KClass<T>) : Pattern {

    lateinit var value: T
        private set

    override fun invoke(obj: Any?) = when {
        kclass.isInstance(obj) -> true.also { value = kclass.cast(obj) }
        else -> false
    }
}

inline fun <reified T : Any> capture() = Capture(T::class)

fun <T> match(obj: Any, body: Matcher<T>.() -> MatchResult<T>): T =
    Matcher<T>(obj).run(body).value

val any = Pattern { true }

val none = Pattern { false }

val isNull = Pattern { it == null }

operator fun Pattern.not() = Pattern { !this@not(it) }

infix fun Pattern.and(that: Pattern) = Pattern { this@and(it) && that(it) }

infix fun Pattern.or(that: Pattern) = Pattern { this@or(it) || that(it) }

fun eq(value: Any?) = Pattern { it == value }

fun oneOf(vararg values: Any?) = Pattern { values.contains(it) }

fun isA(kClass: KClass<*>) = Pattern { kClass.isInstance(it) }

fun isSame(value: Any) = Pattern { it === value }

inline fun <reified C : Comparable<C>> gt(value: C) = Pattern {
    when (it) {
        is C -> it > value
        else -> false
    }
}

inline fun <reified C : Comparable<C>> ge(value: C) = Pattern {
    when (it) {
        is C -> it >= value
        else -> false
    }
}

inline fun <reified C : Comparable<C>> lt(value: C) = Pattern {
    when (it) {
        is C -> it < value
        else -> false
    }
}

inline fun <reified C : Comparable<C>> le(value: C) = Pattern {
    when (it) {
        is C -> it <= value
        else -> false
    }
}

operator fun KClass<*>.invoke(vararg patterns: Any?) = Pattern {
    fun asTest(p: Any?) = when (p) {
        is Pattern -> p
        else -> eq(p)
    }
    when {
        it == null -> false
        !this@invoke.isInstance(it) -> false
        patterns.size != maxComponent(it) -> false
        it is Iterable<*> -> it.zip(patterns) { elem, p ->
            if (asTest(p)(elem)) 1 else 0
        }.sum() == patterns.size

        else -> patterns.foldIndexed(true) { i, b, p ->
            b && asTest(p).testComponentN(it, i + 1)
        }
    }
}

private fun Pattern.testComponentN(obj: Any?, index: Int) =
    if (index < 0 || obj == null) false
    else obj::class.memberFunctions.find { f ->
        f.name == "component$index" &&
                f.parameters.size == 1 &&
                f.parameters[0].kind == KParameter.Kind.INSTANCE
    }
        ?.call(obj)
        ?.let { this@testComponentN(it) }
        ?: false

private fun maxComponent(obj: Any?) = obj?.let {
    generateSequence(1) { index ->
        if (obj::class.memberFunctions.any { f ->
                f.name == "component$index" &&
                        f.parameters.size == 1 &&
                        f.parameters[0].kind == KParameter.Kind.INSTANCE
            }) index + 1 else null
    }.last() - 1
} ?: 0

data class Person(val firstName: String, val lastName: String, val age: Int)

fun main() {
    /*val result: Int = match(Person("a", "b", 4)) {
        Person::class("a", "b") then { 42 }
        Person::class("a", oneOf("c", "d") or isNull, 4) then { 11 }
        Person::class("a", !eq("c"), capture("age")) then { get("age")!! }
        none then { 12 }
        isA(String::class) and !eq("b") then { 7 }
        eq("str") then { 5 }
        any then { 14 }
        otherwise { 23 }
    }*/

    val p = Person("Andy", "Smith", 42)

    val result = match(p) {
        Person::class("Andy", "Miller", any) then
                { "Andy Miller has called!" }
        val ageCap = capture<Int>()
        Person::class("Andy", !eq("Miller"), ageCap) then
                { "Some other Andy of age ${ageCap.value} has called" }
        otherwise { "Some unknown caller" }
    }

    println(result)
}