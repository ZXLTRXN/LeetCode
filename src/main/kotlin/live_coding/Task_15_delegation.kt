package live_coding

interface Base {
    val message: String
    fun print()
}

class BaseImpl : Base {
    override val message = "BaseImpl message"
    override fun print() { println(message) }
}

class Derived(b: Base) : Base by b {
    override val message = "Derived message"
}

fun main() {
    val b = BaseImpl()
    val derived = Derived(b)
    println(derived.message) // выведет `BaseImpl message` или `Derived message`?
    derived.print() // выведет `BaseImpl message` или `Derived message`?
}