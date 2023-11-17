package creativeDSLs.chapter_10

import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.*

@Repeatable
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Mapping(
    val source: String,
    val target: String,
    val transformer: KClass<*> = Nothing::class
)

abstract class Mapper<S : Any, T : Any> {
    fun map(s: S): T {
        val annotations = this::class.findAnnotations(Mapping::class)

        val targetType = this::class.supertypes[0]
            .arguments[1]
            .type!!
            .classifier as KClass<*>

        val targetConstructor = targetType.primaryConstructor!!

        val targetArgs = targetConstructor.parameters.map { targetParam ->

            val ann = annotations.find { it.target == targetParam.name }

            val sourceParam = ann?.source ?: targetParam.name

            val sourceValue = s::class.memberProperties.find {
                it.name == sourceParam
            }!!.getter.call(s)

            ann?.transformer?.isSubclassOf(Function1::class).takeIf { it == true }
                ?.let {
                    val transformer = ann!!.transformer.objectInstance
                        ?: ann.transformer.primaryConstructor!!.call()
                    transformer::class.memberFunctions
                        .find { it.name == "invoke" }!!
                        .call(transformer, sourceValue)
                } ?: sourceValue
        }.toTypedArray()

        @Suppress("UNCHECKED_CAST")
        return targetConstructor.call(*targetArgs) as T
    }
}


data class User(val id: UUID, val firstName: String, val familyName: String, val birthDay: ZonedDateTime)

data class Person(val firstName: String, val lastName: String, val age: Int)

open class Transformer<in A, out B>(val lambda: (A) -> B) : (A) -> B {
    override fun invoke(a: A): B = lambda(a)
}

object AgeTransformer : Transformer<ZonedDateTime, Int>({ z ->
    ChronoUnit.YEARS.between(z, ZonedDateTime.now()).toInt()
})

@Mapping("familyName", "lastName")
@Mapping("birthDay", "age", AgeTransformer::class)
object UserToPerson : Mapper<User, Person>()

fun main() {
    val u = User(UUID.randomUUID(), "John", "Doe", ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]"))
    val p = UserToPerson.map(u)
    println(p)
}