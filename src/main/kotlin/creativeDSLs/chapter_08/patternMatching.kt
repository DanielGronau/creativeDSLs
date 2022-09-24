package creativeDSLs.chapter_08

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberFunctions

fun interface Test : (Any?) -> Boolean

data class MatchResult<T>(val value: T)

class Matcher<T>(private val obj: Any?) {
    private val captures = mutableMapOf<String, Any?>()
    private var result: T? = null

    fun otherwise(default: () -> T) = MatchResult(result ?: default())

    infix fun Test.then(value: () -> T) {
        if (result == null && this(obj)) {
            result = value()
        }
    }

    fun capture(key: String) = Test {
        captures[key] = it
        true
    }

    @Suppress("UNCHECKED_CAST")
    fun <R> get(key: String) = captures[key] as? R
}

fun <T> match(obj: Any, body: Matcher<T>.() -> MatchResult<T>): T =
    Matcher<T>(obj).run(body).value

val any = Test { true }

val none = Test { false }

val isNull = Test { it == null }

operator fun Test.not() = Test { !this@not(it) }

infix fun Test.and(that: Test) = Test { this@and(it) && that(it) }

infix fun Test.or(that: Test) = Test { this@or(it) || that(it) }

fun eq(value: Any?) = Test { it == value }

fun oneOf(vararg values: Any?) = Test { values.contains(it) }

fun isA(kClass: KClass<*>) = Test { kClass.isInstance(it) }

fun isSame(value: Any) = Test { it === value }

inline fun <reified C : Comparable<C>> gt(value: C) = Test {
    when (it) {
        is C -> it > value
        else -> false
    }
}

inline fun <reified C : Comparable<C>> ge(value: C) = Test {
    when (it) {
        is C -> it >= value
        else -> false
    }
}

inline fun <reified C : Comparable<C>> lt(value: C) = Test {
    when (it) {
        is C -> it < value
        else -> false
    }
}

inline fun <reified C : Comparable<C>> le(value: C) = Test {
    when (it) {
        is C -> it <= value
        else -> false
    }
}

operator fun KClass<*>.invoke(vararg patterns: Any?) = Test {
    fun asTest(p: Any?) = when (p) {
        is Test -> p
        else -> eq(p)
    }
    when {
        it == null -> false
        !this@invoke.isInstance(it) -> false
        patterns.size != maxComponent(it) -> false
        it is Iterable<*> -> it.zip(patterns) { elem, p -> if (asTest(p)(elem)) 1 else 0 }
            .sum() == patterns.size

        else -> patterns.foldIndexed(true) { i, b, p -> b && asTest(p).testComponentN(it, i + 1) }
    }
}

private fun Test.testComponentN(obj: Any?, index: Int) =
    if (index < 0 || obj == null) false
    else obj::class.memberFunctions.find { f ->
        f.name == "component$index" && f.parameters.size == 1 && f.parameters[0].kind == KParameter.Kind.INSTANCE
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
        Person::class("Andy", !eq("Miller"), capture("age")) then
                { "Some other Andy of age ${get<Int>("age")!!} has called" }
        otherwise { "Some unknown caller" }
    }

    println(result)
}