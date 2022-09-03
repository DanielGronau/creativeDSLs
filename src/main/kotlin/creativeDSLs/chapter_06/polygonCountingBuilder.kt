package creativeDSLs.chapter_06

import java.awt.geom.Point2D

data class Polygon(val points: List<Point2D>)

sealed class PB
object PolygonBuilder : PB() {
    fun add(point: Point2D) = PointPB<PolygonBuilder>(listOf(point))
}
class PointPB<T : PB>(val points:List<Point2D>) : PB() {
    fun add(point: Point2D) = PointPB<PointPB<T>>(points + point)
}

fun <T: PB> PointPB<PointPB<PointPB<T>>>.build() = Polygon(points)

fun main() {
    val polygon = PolygonBuilder
        .add(Point2D.Double(1.0, 2.3))
        .add(Point2D.Double(2.1, 4.5))
        .add(Point2D.Double(2.4, 5.0))
        .build()

    println(polygon)
}