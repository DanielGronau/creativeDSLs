package creativeDSLs.chapter_06

import creativeDSLs.chapter_06.Validation.Success.plus
import kotlin.reflect.KFunction1
import kotlin.reflect.KProperty1

sealed interface Validation {
    data object Success : Validation
    data class Failure(val reasons: List<String>) : Validation

    operator fun Validation.plus(that: Validation): Validation = when {
        this is Failure && that is Failure -> this.copy(reasons = this.reasons + that.reasons)
        this is Success -> that
        else -> this
    }
}

@DslMarker
annotation class ValidationDsl

@ValidationDsl
fun interface Validator<T> {
    fun validate(t: T): Validation
}

fun <T> T.validate(
    vararg validators: Validator<T>
): Validation = validators.fold<_, Validation>(Validation.Success) { result, validator ->
    result + validator.validate(this)
}

private infix fun Boolean.then(reason: String) = when {
    this -> Validation.Failure(listOf(reason))
    else -> Validation.Success
}

fun notBlank() = Validator<String> {
    it.isBlank() then "String can't be blank"
}

fun minLength(min: Int) = Validator<String> {
    (it.length < min) then "String '$it' must have at least $min characters"
}

fun maxLength(max: Int) = Validator<String> {
    (it.length > max) then "String '$it' must have at most $max characters"
}

fun <T> forAll(vararg validators: Validator<T>) = Validator<List<T>> {
    it.fold<_, Validation>(Validation.Success) { result, element ->
        result + element.validate(*validators)
    }
}

fun <T, S> KProperty1<T, S>.validate(
    vararg validators: Validator<S>
) = Validator<T> {
    validators.fold<_, Validation>(Validation.Success) { result, validator ->
        result + validator.validate(this.call(it))
    }
}

fun <T, S> KFunction1<T, S>.validate(
    vararg validators: Validator<S>
) = Validator<T> {
    validators.fold<_, Validation>(Validation.Success) { result, validator ->
        result + validator.validate(this.call(it))
    }
}

data class Child(val name: String, val friends: List<String>)

fun main() {
    val child = Child("Charlie", listOf("Snoopy", "Lucy", "Linus"))

    val result = child.validate(
        Child::name.validate(
            minLength(2),
            maxLength(3)
        ),
        Child::friends.validate(
            forAll(
                notBlank(),
                maxLength(5)
            )
        )
    )
    println(result)
}
