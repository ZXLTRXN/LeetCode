class ReverseLinkedList {
    fun reverseList(head: ListNode?): ListNode? {
        return recursiveReverseList(head, null)
    }

    fun IterativeReverseList(head: ListNode?): ListNode? {
        var cur = head
        var newNext: ListNode? = null
        while (cur != null) {
            val oldNext = cur.next
            cur.next = newNext
            newNext = cur
            cur = oldNext
        }
        return newNext
    }

    fun recursiveReverseList(head: ListNode?, newNext: ListNode?): ListNode? {
        if(head == null) return newNext

        val oldNext = head.next
        head.next = newNext

        return recursiveReverseList(oldNext, head)
    }
}


data class ListNode(var value: Int) {
    var next: ListNode? = null
}