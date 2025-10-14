

@Suppress("unused")
fun main(args: Array<String>) {}

fun assertEquals(expected: Any?, actual: Any?, message: String = "") {
    val yellow = "\u001B[33m"
    val green = "\u001B[32m"
    val red = "\u001B[31m"
    val reset = "\u001B[0m"

    if (expected == actual) {
        println("${green}✅ OK$reset ${if (message.isNotEmpty()) "- $message" else ""}")
    } else {
        println("${red}❌ FAIL$reset ${if (message.isNotEmpty()) "- $message" else ""}")
        println("${yellow}Expected:$reset $expected")
        println("${yellow}Actual:  $reset $actual")
    }
}