package easy.linked_list

data class ListNode(var value: Int) {

    constructor(value: Int, next: ListNode?): this(value) {
        this.next = next
    }

    var next: ListNode? = null
}