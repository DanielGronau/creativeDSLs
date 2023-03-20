package creativeDSLs.chapter_13.valueClassMangling

@JvmInline
value class Kilometers(val value: Double)
@JvmInline
value class Miles(val value: Double)

@JvmName("displayKm")
fun display(x: Kilometers) { println("${x.value} km") }
@JvmName("displayMiles")
fun display(x: Miles) { println("${x.value} miles") }