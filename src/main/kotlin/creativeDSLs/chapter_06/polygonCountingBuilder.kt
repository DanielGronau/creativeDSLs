package creativeDSLs.chapter_06

import java.awt.geom.Point2D

sealed interface Nat
interface Z : Nat
interface S<N : Nat> : Nat

data class Polygon(val points: List<Point2D>)

class PolygonBuilder<N : Nat> private constructor() {
    companion object {
        operator fun invoke() = PolygonBuilder<Z>()
    }
    val points: MutableList<Point2D> = mutableListOf()

    @Suppress("UNCHECKED_CAST")
    fun add(point: Point2D) = (this as PolygonBuilder<S<N>>)
        .also { points += point }
}

fun <N : Nat> PolygonBuilder<S<S<S<N>>>>.build() = Polygon(points)

fun main() {
    val polygon = PolygonBuilder()
        .add(Point2D.Double(1.0, 2.3))
        .add(Point2D.Double(2.1, 4.5))
        .add(Point2D.Double(2.4, 5.0))
        .build()
    println(polygon)
}

