package creativeDSLs.chapter_11

import kotlin.Double

public val Double.s: Second
  get() = Second(this * 1.0)

public val Double.min: Second
  get() = Second(this * 60.0)

public val Double.hrs: Second
  get() = Second(this * 3600.0)

public val Double.yr: Second
  get() = Second(this * 3.1556925216E7)

public val Double.mm: Meter
  get() = Meter(this * 0.001)

public val Double.cm: Meter
  get() = Meter(this * 0.01)

public val Double.`in`: Meter
  get() = Meter(this * 0.0254)

public val Double.ft: Meter
  get() = Meter(this * 0.3048)

public val Double.yd: Meter
  get() = Meter(this * 0.9144)

public val Double.m: Meter
  get() = Meter(this * 1.0)

public val Double.km: Meter
  get() = Meter(this * 1000.0)

public val Double.mi: Meter
  get() = Meter(this * 1609.344)

public val Double.mg: Kilogram
  get() = Kilogram(this * 1.0E-6)

public val Double.g: Kilogram
  get() = Kilogram(this * 0.001)

public val Double.kg: Kilogram
  get() = Kilogram(this * 1.0)

public val Double.tons: Kilogram
  get() = Kilogram(this * 1000.0)

public val Double.mm2: SquareMeter
  get() = SquareMeter(this * 1.0E-6)

public val Double.m2: SquareMeter
  get() = SquareMeter(this * 1.0)

public val Double.km2: SquareMeter
  get() = SquareMeter(this * 1000000.0)

public val Double.mm3: CubicMeter
  get() = CubicMeter(this * 1.0E-9)

public val Double.l: CubicMeter
  get() = CubicMeter(this * 0.001)

public val Double.m3: CubicMeter
  get() = CubicMeter(this * 1.0)

public val Double.km3: CubicMeter
  get() = CubicMeter(this * 1.0E9)

public val Double.m_s: MeterPerSecond
  get() = MeterPerSecond(this * 1.0)

public val Double.km_h: MeterPerSecond
  get() = MeterPerSecond(this * 0.2777777777777778)

public val Double.m_s2: MeterPerSecondSquared
  get() = MeterPerSecondSquared(this * 1.0)

public val Double.N: Newton
  get() = Newton(this * 1.0)

public val Double.kN: Newton
  get() = Newton(this * 1000.0)

public val Double.mJ: Joule
  get() = Joule(this * 1.0E-4)

public val Double.J: Joule
  get() = Joule(this * 1.0)

public val Double.kJ: Joule
  get() = Joule(this * 1000.0)

public val Double.MegaJ: Joule
  get() = Joule(this * 1000000.0)

public val Double.mW: Watt
  get() = Watt(this * 0.001)

public val Double.W: Watt
  get() = Watt(this * 1.0)

public val Double.kW: Watt
  get() = Watt(this * 1000.0)

public val Double.MegaW: Watt
  get() = Watt(this * 1000000.0)

public val Double.mP: Pascal
  get() = Pascal(this * 0.001)

public val Double.P: Pascal
  get() = Pascal(this * 1.0)

public val Double.hP: Pascal
  get() = Pascal(this * 100.0)

public val Double.kP: Pascal
  get() = Pascal(this * 1000.0)

public val Double.MegaP: Pascal
  get() = Pascal(this * 1000000.0)

public val Second.s: Double
  get() = this.amount / 1.0

public val Second.min: Double
  get() = this.amount / 60.0

public val Second.hrs: Double
  get() = this.amount / 3600.0

public val Second.yr: Double
  get() = this.amount / 3.1556925216E7

public val Meter.mm: Double
  get() = this.amount / 0.001

public val Meter.cm: Double
  get() = this.amount / 0.01

public val Meter.`in`: Double
  get() = this.amount / 0.0254

public val Meter.ft: Double
  get() = this.amount / 0.3048

public val Meter.yd: Double
  get() = this.amount / 0.9144

public val Meter.m: Double
  get() = this.amount / 1.0

public val Meter.km: Double
  get() = this.amount / 1000.0

public val Meter.mi: Double
  get() = this.amount / 1609.344

public val Kilogram.mg: Double
  get() = this.amount / 1.0E-6

public val Kilogram.g: Double
  get() = this.amount / 0.001

public val Kilogram.kg: Double
  get() = this.amount / 1.0

public val Kilogram.tons: Double
  get() = this.amount / 1000.0

public val SquareMeter.mm2: Double
  get() = this.amount / 1.0E-6

public val SquareMeter.m2: Double
  get() = this.amount / 1.0

public val SquareMeter.km2: Double
  get() = this.amount / 1000000.0

public val CubicMeter.mm3: Double
  get() = this.amount / 1.0E-9

public val CubicMeter.l: Double
  get() = this.amount / 0.001

public val CubicMeter.m3: Double
  get() = this.amount / 1.0

public val CubicMeter.km3: Double
  get() = this.amount / 1.0E9

public val MeterPerSecond.m_s: Double
  get() = this.amount / 1.0

public val MeterPerSecond.km_h: Double
  get() = this.amount / 0.2777777777777778

public val MeterPerSecondSquared.m_s2: Double
  get() = this.amount / 1.0

public val Newton.N: Double
  get() = this.amount / 1.0

public val Newton.kN: Double
  get() = this.amount / 1000.0

public val Joule.mJ: Double
  get() = this.amount / 1.0E-4

public val Joule.J: Double
  get() = this.amount / 1.0

public val Joule.kJ: Double
  get() = this.amount / 1000.0

public val Joule.MegaJ: Double
  get() = this.amount / 1000000.0

public val Watt.mW: Double
  get() = this.amount / 0.001

public val Watt.W: Double
  get() = this.amount / 1.0

public val Watt.kW: Double
  get() = this.amount / 1000.0

public val Watt.MegaW: Double
  get() = this.amount / 1000000.0

public val Pascal.mP: Double
  get() = this.amount / 0.001

public val Pascal.P: Double
  get() = this.amount / 1.0

public val Pascal.hP: Double
  get() = this.amount / 100.0

public val Pascal.kP: Double
  get() = this.amount / 1000.0

public val Pascal.MegaP: Double
  get() = this.amount / 1000000.0

public operator fun CubicMeter.plus(that: CubicMeter) = copy(amount = this.amount + that.amount)

public operator fun Joule.plus(that: Joule) = copy(amount = this.amount + that.amount)

public operator fun Kilogram.plus(that: Kilogram) = copy(amount = this.amount + that.amount)

public operator fun Meter.plus(that: Meter) = copy(amount = this.amount + that.amount)

public operator fun MeterPerSecond.plus(that: MeterPerSecond) = copy(amount = this.amount +
    that.amount)

public operator fun MeterPerSecondSquared.plus(that: MeterPerSecondSquared) = copy(amount =
    this.amount + that.amount)

public operator fun Newton.plus(that: Newton) = copy(amount = this.amount + that.amount)

public operator fun Pascal.plus(that: Pascal) = copy(amount = this.amount + that.amount)

public operator fun Second.plus(that: Second) = copy(amount = this.amount + that.amount)

public operator fun SquareMeter.plus(that: SquareMeter) = copy(amount = this.amount + that.amount)

public operator fun Watt.plus(that: Watt) = copy(amount = this.amount + that.amount)

public operator fun CubicMeter.minus(that: CubicMeter) = copy(amount = this.amount - that.amount)

public operator fun Joule.minus(that: Joule) = copy(amount = this.amount - that.amount)

public operator fun Kilogram.minus(that: Kilogram) = copy(amount = this.amount - that.amount)

public operator fun Meter.minus(that: Meter) = copy(amount = this.amount - that.amount)

public operator fun MeterPerSecond.minus(that: MeterPerSecond) = copy(amount = this.amount -
    that.amount)

public operator fun MeterPerSecondSquared.minus(that: MeterPerSecondSquared) = copy(amount =
    this.amount - that.amount)

public operator fun Newton.minus(that: Newton) = copy(amount = this.amount - that.amount)

public operator fun Pascal.minus(that: Pascal) = copy(amount = this.amount - that.amount)

public operator fun Second.minus(that: Second) = copy(amount = this.amount - that.amount)

public operator fun SquareMeter.minus(that: SquareMeter) = copy(amount = this.amount - that.amount)

public operator fun Watt.minus(that: Watt) = copy(amount = this.amount - that.amount)

public operator fun CubicMeter.unaryMinus() = copy(amount = -this.amount)

public operator fun Joule.unaryMinus() = copy(amount = -this.amount)

public operator fun Kilogram.unaryMinus() = copy(amount = -this.amount)

public operator fun Meter.unaryMinus() = copy(amount = -this.amount)

public operator fun MeterPerSecond.unaryMinus() = copy(amount = -this.amount)

public operator fun MeterPerSecondSquared.unaryMinus() = copy(amount = -this.amount)

public operator fun Newton.unaryMinus() = copy(amount = -this.amount)

public operator fun Pascal.unaryMinus() = copy(amount = -this.amount)

public operator fun Second.unaryMinus() = copy(amount = -this.amount)

public operator fun SquareMeter.unaryMinus() = copy(amount = -this.amount)

public operator fun Watt.unaryMinus() = copy(amount = -this.amount)

public operator fun Double.times(that: CubicMeter) = that.copy(amount = this * that.amount)

public operator fun Double.times(that: Joule) = that.copy(amount = this * that.amount)

public operator fun Double.times(that: Kilogram) = that.copy(amount = this * that.amount)

public operator fun Double.times(that: Meter) = that.copy(amount = this * that.amount)

public operator fun Double.times(that: MeterPerSecond) = that.copy(amount = this * that.amount)

public operator fun Double.times(that: MeterPerSecondSquared) = that.copy(amount = this *
    that.amount)

public operator fun Double.times(that: Newton) = that.copy(amount = this * that.amount)

public operator fun Double.times(that: Pascal) = that.copy(amount = this * that.amount)

public operator fun Double.times(that: Second) = that.copy(amount = this * that.amount)

public operator fun Double.times(that: SquareMeter) = that.copy(amount = this * that.amount)

public operator fun Double.times(that: Watt) = that.copy(amount = this * that.amount)

public operator fun Meter.times(that: Meter) = SquareMeter(this.amount * that.amount)

public operator fun Meter.times(that: SquareMeter) = CubicMeter(this.amount * that.amount)

public operator fun SquareMeter.times(that: Meter) = CubicMeter(this.amount * that.amount)

public operator fun MeterPerSecond.times(that: Second) = Meter(this.amount * that.amount)

public operator fun Second.times(that: MeterPerSecond) = Meter(this.amount * that.amount)

public operator fun MeterPerSecondSquared.times(that: Second) = MeterPerSecond(this.amount *
    that.amount)

public operator fun Second.times(that: MeterPerSecondSquared) = MeterPerSecond(this.amount *
    that.amount)

public operator fun MeterPerSecondSquared.times(that: Kilogram) = Newton(this.amount * that.amount)

public operator fun Kilogram.times(that: MeterPerSecondSquared) = Newton(this.amount * that.amount)

public operator fun Pascal.times(that: SquareMeter) = Newton(this.amount * that.amount)

public operator fun SquareMeter.times(that: Pascal) = Newton(this.amount * that.amount)

public operator fun Newton.times(that: Meter) = Joule(this.amount * that.amount)

public operator fun Meter.times(that: Newton) = Joule(this.amount * that.amount)

public operator fun Watt.times(that: Second) = Joule(this.amount * that.amount)

public operator fun Second.times(that: Watt) = Joule(this.amount * that.amount)

public operator fun SquareMeter.div(that: Meter) = Meter(this.amount / that.amount)

public operator fun CubicMeter.div(that: Meter) = SquareMeter(this.amount / that.amount)

public operator fun CubicMeter.div(that: SquareMeter) = Meter(this.amount / that.amount)

public operator fun Meter.div(that: MeterPerSecond) = Second(this.amount / that.amount)

public operator fun Meter.div(that: Second) = MeterPerSecond(this.amount / that.amount)

public operator fun MeterPerSecond.div(that: MeterPerSecondSquared) = Second(this.amount /
    that.amount)

public operator fun MeterPerSecond.div(that: Second) = MeterPerSecondSquared(this.amount /
    that.amount)

public operator fun Newton.div(that: MeterPerSecondSquared) = Kilogram(this.amount / that.amount)

public operator fun Newton.div(that: Kilogram) = MeterPerSecondSquared(this.amount / that.amount)

public operator fun Newton.div(that: Pascal) = SquareMeter(this.amount / that.amount)

public operator fun Newton.div(that: SquareMeter) = Pascal(this.amount / that.amount)

public operator fun Joule.div(that: Newton) = Meter(this.amount / that.amount)

public operator fun Joule.div(that: Meter) = Newton(this.amount / that.amount)

public operator fun Joule.div(that: Watt) = Second(this.amount / that.amount)

public operator fun Joule.div(that: Second) = Watt(this.amount / that.amount)
