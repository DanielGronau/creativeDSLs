package creativeDSLs.chapter_04

import java.time.LocalDateTime
import java.util.logging.Logger

class TemperatureSensor {
    var celsius: Double = 0.0
        set(value) {
            require(value >= -273.15) { "Temperature is under absolute zero." }
            field = value
        }
}

class Data {
    val lazyCalc: Int by lazy {
        3 * 4
    }
}

class SensitiveData {
    val logger = Logger.getLogger(this::class.java.name)

    var secretValue: Int = 42
        get() {
            logger.info("Access to secret value $field at ${LocalDateTime.now()}")
            return field
        }
}

fun main() {
    TemperatureSensor().celsius = 500.0
    println(SensitiveData().secretValue)
}