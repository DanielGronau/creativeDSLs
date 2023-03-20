package creativeDSLs.chapter_08.turtle

import java.awt.Color
import java.awt.Color.BLACK

sealed interface TurtleOp

interface Motion : TurtleOp
data class F(val distance: Int): Motion // forward
data class B(val distance: Int): Motion // backward
data class R(val degrees: Int): Motion // right
data class L(val degrees: Int): Motion // left
data class Goto(val x: Int, val y: Int): Motion // set position
data class Head(val degrees: Int): Motion // set heading

interface Drawing : TurtleOp
object Down : Drawing // pen down
object Up : Drawing // pen up
data class Col(val color: Color): Drawing // set color

data class TurtleGraphic(val operations: List<TurtleOp>)

operator fun TurtleOp.unaryMinus() = TurtleGraphic(listOf(this))

operator fun TurtleGraphic.minus(turtleOp: TurtleOp) =
    this.copy(operations = operations + turtleOp)

fun main() {
    val graph = -Col(BLACK)-
            Down-
            F(100)-L(120)-
            F(100)-L(120)-
            F(100)-L(120)-
            Up

    println(graph)
}