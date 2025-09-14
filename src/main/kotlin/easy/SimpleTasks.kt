package easy


@Suppress("unused")
class SimpleTasks {

    fun reverseString(str: String): String {
        // свой reversed()
        val len = str.length
        val builder = StringBuilder(len) // без лишних аллокаций

        for (i in len-1 downTo 0) {
            builder.append(str[i])
        }
        return builder.toString()
    }

    fun removeDuplicates(list: List<Int>): List<Int> {
        val set = LinkedHashSet<Int>()

        for (el in list) {
            if(!set.contains(el)) {
                set.add(el)
            }
        }
        return set.toTypedArray().toList()
    }
}