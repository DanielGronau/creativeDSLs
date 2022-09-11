package creativeDSLs.chapter_11

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import java.nio.file.Path
import kotlin.reflect.KClass

sealed interface Quantity {
    val amount: Double
}

// base units
data class Second(override val amount: Double) : Quantity
data class Meter(override val amount: Double) : Quantity
data class Kilogram(override val amount: Double) : Quantity

// derived units
data class SquareMeter(override val amount: Double) : Quantity
data class CubicMeter(override val amount: Double) : Quantity
data class MeterPerSecond(override val amount: Double) : Quantity
data class MeterPerSecondSquared(override val amount: Double) : Quantity
data class Newton(override val amount: Double) : Quantity
data class Joule(override val amount: Double) : Quantity
data class Watt(override val amount: Double) : Quantity
data class Pascal(override val amount: Double) : Quantity

private val fromToDouble = listOf(
    Triple("s", Second::class, 1.0),
    Triple("min", Second::class, 60.0),
    Triple("hrs", Second::class, 3600.0),
    Triple("yr", Second::class, 31_556_925.216),

    Triple("mm", Meter::class, 0.001),
    Triple("cm", Meter::class, 0.01),
    Triple("in", Meter::class, 0.0254),
    Triple("ft", Meter::class, 0.3048),
    Triple("yd", Meter::class, 0.9144),
    Triple("m", Meter::class, 1.0),
    Triple("km", Meter::class, 1000.0),
    Triple("mi", Meter::class, 1609.344),

    Triple("mg", Kilogram::class, 0.000_001),
    Triple("g", Kilogram::class, 0.001),
    Triple("kg", Kilogram::class, 1.0),
    Triple("tons", Kilogram::class, 1000.0),

    Triple("mm2", SquareMeter::class, 0.000_001),
    Triple("m2", SquareMeter::class, 1.0),
    Triple("km2", SquareMeter::class, 1_000_000.0),

    Triple("mm3", CubicMeter::class, 0.000_000_001),
    Triple("l", CubicMeter::class, 0.001),
    Triple("m3", CubicMeter::class, 1.0),
    Triple("km3", CubicMeter::class, 1_000_000_000.0),

    Triple("m_s", MeterPerSecond::class, 1.0),
    Triple("km_h", MeterPerSecond::class, 0.2777777777777778),

    Triple("m_s2", MeterPerSecondSquared::class, 1.0),

    Triple("N", Newton::class, 1.0),
    Triple("kN", Newton::class, 1000.0),

    Triple("mJ", Joule::class, 0.0001),
    Triple("J", Joule::class, 1.0),
    Triple("kJ", Joule::class, 1000.0),
    Triple("MegaJ", Joule::class, 1_000_000.0),

    Triple("mW", Watt::class, 0.001),
    Triple("W", Watt::class, 1.0),
    Triple("kW", Watt::class, 1000.0),
    Triple("MegaW", Watt::class, 1_000_000.0),

    Triple("mP", Pascal::class, 0.001),
    Triple("P", Pascal::class, 1.0),
    Triple("hP", Pascal::class, 100.0),
    Triple("kP", Pascal::class, 1000.0),
    Triple("MegaP", Pascal::class, 1_000_000.0),
)

private fun makeDoubleToQuantity(unit: String, kClass: KClass<out Quantity>, factor: Double) =
    PropertySpec.builder(unit, kClass)
        .receiver(Double::class)
        .getter(
            FunSpec.getterBuilder()
                .addStatement("return %T(this * %L)", kClass, factor)
                .build()
        )
        .build()

private fun makeQuantityToDouble(unit: String, kClass: KClass<out Quantity>, factor: Double) =
    PropertySpec.builder(unit, Double::class)
        .receiver(kClass)
        .getter(
            FunSpec.getterBuilder()
                .addStatement("return this.amount / %L", factor)
                .build()
        )
        .build()

private fun makeAddition(kClass: KClass<out Quantity>) =
    FunSpec.builder("plus")
        .addModifiers(KModifier.OPERATOR)
        .receiver(kClass)
        .addParameter("that", kClass)
        .addStatement("return copy(amount = this.amount + that.amount)")
        .build()

private fun makeSubtraction(kClass: KClass<out Quantity>) =
    FunSpec.builder("minus")
        .addModifiers(KModifier.OPERATOR)
        .receiver(kClass)
        .addParameter("that", kClass)
        .addStatement("return copy(amount = this.amount - that.amount)")
        .build()

private fun makeNegation(kClass: KClass<out Quantity>) =
    FunSpec.builder("unaryMinus")
        .addModifiers(KModifier.OPERATOR)
        .receiver(kClass)
        .addStatement("return copy(amount = -this.amount)")
        .build()

private fun makeScalarMultiplication(kClass: KClass<out Quantity>) =
    FunSpec.builder("times")
        .addModifiers(KModifier.OPERATOR)
        .receiver(Double::class)
        .addParameter("that", kClass)
        .addStatement("return that.copy(amount = this * that.amount)")
        .build()

private fun makeQuantityToAmounts() =
    fromToDouble.map { (u, k, f) -> makeDoubleToQuantity(u, k, f) }

private fun makeAmountToQuantities() =
    fromToDouble.map { (u, k, f) -> makeQuantityToDouble(u, k, f) }

private fun makeAdditions() =
    Quantity::class.sealedSubclasses.map { makeAddition(it) }

private fun makeSubtractions() =
    Quantity::class.sealedSubclasses.map { makeSubtraction(it) }

private fun makeNegations() =
    Quantity::class.sealedSubclasses.map { makeNegation(it) }

private fun makeScalarMultiplications() =
    Quantity::class.sealedSubclasses.map { makeScalarMultiplication(it) }

fun main() {
    val builder = FileSpec.builder("creativeDSLs.chapter_11", "generated")

    makeQuantityToAmounts().forEach { builder.addProperty(it) }

    makeAmountToQuantities().forEach { builder.addProperty(it) }

    makeAdditions().forEach { builder.addFunction(it) }

    makeSubtractions().forEach { builder.addFunction(it) }

    makeNegations().forEach { builder.addFunction(it)}

    makeScalarMultiplications().forEach { builder.addFunction(it)}

    builder.build()
        .writeTo(Path.of("./src/main/kotlin/"))
}

