package live_coding

/**
 * Требуется:
 *
 * Объяснить, как работают первичный и вторичный конструкторы.
 * Указать, как вызываются эти конструкторы, и в каком порядке выполняются блоки init.
 * Указать, какие данные передаются в свойства класса при создании объектов.
 */
class Student(var name: String) {

    constructor(sectionName: String, id: Int) : this(sectionName) { // выполнится после всех init
        this.id = id // выше или ниже поле id нам без разницы для конструктора
        println("constructor 2")
    }

    // init и поля исполняются по порядку
    init {
        println("1 init $name")
    }

    var id: Int? = null

    val surname = "Shevtsov"
    init {
        println("2 init surname $surname id $id") // если init будет выше поля surname, то не сработает
    }
}


fun main() {
    Student("Ilia", 1)
}
