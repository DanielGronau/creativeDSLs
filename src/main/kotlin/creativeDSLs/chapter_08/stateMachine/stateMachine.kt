package creativeDSLs.chapter_08.stateMachine

import creativeDSLs.chapter_08.stateMachine.TrafficLight.Companion.off
import creativeDSLs.chapter_08.stateMachine.TrafficLight.Companion.on
import creativeDSLs.chapter_08.stateMachine.TrafficLight.Companion.toGreen
import creativeDSLs.chapter_08.stateMachine.TrafficLight.Companion.toRed
import creativeDSLs.chapter_08.stateMachine.TrafficLight.Companion.toYellow

sealed interface State
interface Idle : State
interface Selected : State
interface Dispend : State
interface ReturnChange : State

data class Item(val name: String, val price: Double)

data class VendingMachine<State> private constructor(val item: Item?, val paid: Double = 0.0) {

    companion object {
        operator fun invoke() = TrafficLight<Off>("off")
        fun TrafficLight<Off>.on() = TrafficLight<Red>("red")
        fun TrafficLight<Red>.toGreen() = TrafficLight<Green>("green")
        fun TrafficLight<Green>.toYellow() = TrafficLight<Yellow>("yellow")
        fun TrafficLight<Yellow>.toRed() = TrafficLight<Red>("red")
        fun <T : On>TrafficLight<T>.off() = TrafficLight<Red>("off")
    }
}

fun main() {
    TrafficLight().on()
        .toGreen()
        .toYellow()
        .toRed()
        .toGreen()
        .off()
}


