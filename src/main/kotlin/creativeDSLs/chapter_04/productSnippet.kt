package creativeDSLs.chapter_04

fun List<Double>.product() = fold(1.0, Double::times)

val p = listOf(1.0, 2.0, 3.0).product()  // p == 6.0
