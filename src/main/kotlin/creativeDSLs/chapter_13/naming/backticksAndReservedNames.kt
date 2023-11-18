package creativeDSLs.chapter_13.naming

object NamingTest {
    @JvmStatic
    @JvmName("checkThisOut")
    fun `check this out`() = println("backticks")

    @JvmStatic
    @JvmName("instanceOf")
    fun instanceof() = println("instanceof")
}