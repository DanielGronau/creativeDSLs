package creativeDSLs.chapter_12.units

import kotlin.reflect.KClass

@Repeatable
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MultiplicationResult(
    val factor1: KClass<*>,
    val factor2: KClass<*>
)