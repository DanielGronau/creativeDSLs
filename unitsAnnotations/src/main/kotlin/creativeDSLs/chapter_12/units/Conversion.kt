package creativeDSLs.chapter_12.units

@Repeatable
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Conversion(
    val derivedUnit: String,
    val factor: Double
)