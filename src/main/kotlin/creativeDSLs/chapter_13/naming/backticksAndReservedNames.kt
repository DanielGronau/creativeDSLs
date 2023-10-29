package creativeDSLs.chapter_13.naming

object NamingTest {
    @JvmStatic
    @JvmName("checkThisOut")
    fun `check this out`() = println("backticks")

    @JvmStatic
    @JvmName("instanceofFun")
    fun instanceof() = println("instanceof")
}