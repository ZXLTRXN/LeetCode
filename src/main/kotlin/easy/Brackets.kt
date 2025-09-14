package easy

import java.util.*
import kotlin.text.iterator

class Brackets {

    fun isValid(s: String): Boolean {
        val stack: Deque<Char> = LinkedList()
        val brackets: Set<Char> = hashSetOf('(', ')', '{', '}', '[', ']')
        val bracketsPairs: Map<Char, Char> = hashMapOf(')' to '(' ,'}' to '{' , ']' to '[')

        for(l in s) {
            if(!brackets.contains(l)) return false
            if(bracketsPairs.containsValue(l)) {
                stack.push(l)
                continue
            }
            if (bracketsPairs.containsKey(l) && stack.peek() == bracketsPairs[l]) {
                stack.pop()
                continue
            } else {
                return false
            }
        }
        return stack.isEmpty()
    }
}