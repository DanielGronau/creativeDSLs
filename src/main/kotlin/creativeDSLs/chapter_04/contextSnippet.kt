package creativeDSLs.chapter_04


interface EnvironmentContext {
    fun getProperty(name: String): String
}

context(EnvironmentContext)
fun methodWithContext() {
    val userName = getProperty("userName")
    println(userName)
}

fun main() {
    val environmentContext = object : EnvironmentContext {
        override fun getProperty(name: String): String = "prop for $name"
    }
    with(environmentContext) {
        methodWithContext()
    }
}