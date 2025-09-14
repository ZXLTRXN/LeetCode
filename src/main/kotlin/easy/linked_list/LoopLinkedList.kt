package easy.linked_list

@Suppress("unused")
class LoopLinkedList {
    // сохраняем в сет, а потом если такое уже встречалось то цикл есть
    fun findLoop(head: ListNode?): Boolean {
        if (head == null) return false

        val set = HashSet<Int>()
        var cur: ListNode? = head
        while (cur != null) {
            if (set.contains(cur.value)) return true
            set.add(cur.value)
            cur = cur.next
        }
        return false
    }

    // алгоритм «черепаха и заяц» (Floyd’s cycle detection)
    // первый делает по шагу, а второй по 2,
    // если есть цикл то они пересекутся
    fun findLoopOptimal(head: ListNode?): Boolean {
        if (head == null) return false

        var tort: ListNode = head.next ?: return false
        var rab: ListNode = head.next?.next ?: return false
        while (true) {
            val rabNext = rab.next?.next
            if (tort.next == null || rabNext == null) return false
            if (tort.value == rab.value ) return true // head
            tort = tort.next!!
            rab = rabNext
        }
    }

    fun simpleTest() {
        // 3 -> 2 -> 0 -> -4 -> 2
        val justEl = ListNode(0)
        val doubleUse = ListNode(2, justEl)
        val end = ListNode(-4, doubleUse)
        justEl.next = end
        val head = ListNode(3, doubleUse)
        println(findLoop(head))

        // 1
        val single = ListNode(0)
        println(findLoop(single))

        // 1 -> 1
        val singleCycle = ListNode(1)
        singleCycle.next = singleCycle
        println(findLoop(singleCycle))
    }
}