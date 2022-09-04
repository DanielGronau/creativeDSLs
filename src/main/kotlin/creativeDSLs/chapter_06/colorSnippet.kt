package creativeDSLs.chapter_06

data class Color(val red: Int, val green: Int, val blue: Int, val alpha: Int = 255)

val a = Color(red = 10, green = 30, blue = 40)