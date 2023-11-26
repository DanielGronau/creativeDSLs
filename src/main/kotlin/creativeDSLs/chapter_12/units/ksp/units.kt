package creativeDSLs.chapter_12.units.ksp

import creativeDSLs.chapter_12.units.Conversion
import creativeDSLs.chapter_12.units.MultiplicationResult
import creativeDSLs.chapter_12.units.QuantityOperations

@QuantityOperations
sealed interface Quantity {
    val amount: Double
}

// base units

@Conversion("s", 1.0)
@Conversion("min", 60.0)
@Conversion("hrs", 3600.0)
@Conversion("yr", 31_556_925.216)
data class Second(override val amount: Double) : Quantity

@Conversion("mm", 0.001)
@Conversion("cm", 0.01)
@Conversion("in", 0.0254)
@Conversion("ft", 0.3048)
@Conversion("yd", 0.9144)
@Conversion("m", 1.0)
@Conversion("km", 1000.0)
@Conversion("mi", 1609.344)
@MultiplicationResult(MeterPerSecond::class, Second::class)
data class Meter(override val amount: Double) : Quantity

@Conversion("mg", 0.000_001)
@Conversion("g", 0.001)
@Conversion("kg", 1.0)
@Conversion("tons", 1000.0)
data class Kilogram(override val amount: Double) : Quantity

// derived units

@Conversion("mm2", 0.000_001)
@Conversion("m2", 1.0)
@Conversion("km2", 1_000_000.0)
@MultiplicationResult(Meter::class, Meter::class)
data class SquareMeter(override val amount: Double) : Quantity

@Conversion("mm3", 0.000_000_001)
@Conversion("l", 0.001)
@Conversion("m3", 1.0)
@Conversion("km3", 1_000_000_000.0)
@MultiplicationResult(Meter::class, SquareMeter::class)
data class CubicMeter(override val amount: Double) : Quantity

@Conversion("m_s", 1.0)
@Conversion("km_h", 0.2777777777777778)
@MultiplicationResult(MeterPerSecondSquared::class, Second::class)
data class MeterPerSecond(override val amount: Double) : Quantity

@Conversion("m_s2", 1.0)
data class MeterPerSecondSquared(override val amount: Double) : Quantity

@Conversion("N", 1.0)
@Conversion("kN", 1000.0)
@MultiplicationResult(MeterPerSecondSquared::class, Kilogram::class)
@MultiplicationResult(Pascal::class, SquareMeter::class)
data class Newton(override val amount: Double) : Quantity

@Conversion("mJ",  0.0001)
@Conversion("J",  1.0)
@Conversion("kJ",  1000.0)
@Conversion("MegaJ",  1_000_000.0)
@MultiplicationResult(Newton::class, Meter::class)
@MultiplicationResult(Watt::class, Second::class)
data class Joule(override val amount: Double) : Quantity

@Conversion("mW",  0.001)
@Conversion("W",  1.0)
@Conversion("kW",  1000.0)
@Conversion("MegaW", 1_000_000.0)
data class Watt(override val amount: Double) : Quantity

@Conversion("mP", 0.001)
@Conversion("P", 1.0)
@Conversion("hP",  100.0)
@Conversion("kP",  1000.0)
@Conversion("MegaP",  1_000_000.0)
data class Pascal(override val amount: Double) : Quantity

