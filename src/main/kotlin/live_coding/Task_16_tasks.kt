package live_coding

// Компилируется ли код? Если нет, то поправить проблемные места.
// Какой будет вывод после выполнения данного кода?
// Как вывести все числа пропустив 3?

private class Task(var id: Long, val name: String)
//
//val tasks = HashSet<Task>()
//
//val list = listOf(1, 3, 5)
//
//fun main() {
//    val task1 = Task(1, "Задача")
//    val task2 = Task(1, "Задача")
//
//    tasks.add(task1)
//    tasks.add(task2)
//
//    list.add(7)
//    list.forEvery { it ->
//        if (it == 3) {
//            return
//        }
//        println("$it")
//    }
//
//    println("tasks contains ${tasks.size} elements")
//    println("Done!")
//}
//
//synchronized fun  <reified T> List<T>.forEvery(itemAction: (T) -> Unit) {
//    list.reversed.forEach { itemAction(it) }
//}


private val tasks = HashSet<Task>()

val list: MutableList<Int> = mutableListOf(1, 3, 5)

fun main() {
    val task1 = Task(1, "Задача")
    val task2 = Task(1, "Задача")

    tasks.add(task1)
    tasks.add(task2)

    list.add(7)
    list.forEveryReversed {
        if (it != 3) {
            println("$it")
        }
    }

    println("tasks contains ${tasks.size} elements")
    println("Done!")
}

inline fun <T> List<T>.forEveryReversed(itemAction: (T) -> Unit) {
    this.reversed().forEach { itemAction(it) }
}