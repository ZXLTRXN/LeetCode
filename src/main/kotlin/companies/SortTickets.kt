package companies

import assertEquals

/**
 * Yandex
 * Отсортировать билеты в порядке маршрута.
 * Из билетов можно построить единственный непрерывный маршрут без петель.
 *
 * Временная сложность: O(n)
 * Пространственная сложность: O(n)
 */
data class TicketY(
    val id: Int,
    val from: String,
    val to: String,
    val meta: Map<String, Any>
)

class SortTickets {
    fun sortTickets(tickets: List<TicketY>): List<TicketY> {

        val paths = HashMap<String, TicketY>()
        val ToDestinations = mutableSetOf<String>()
        val result = mutableListOf<TicketY>()

        for (ticket in tickets) {
            paths[ticket.from] = ticket
            ToDestinations.add(ticket.to)
        }

        val root = tickets.first {
            !ToDestinations.contains(it.from)
        }

        var from = root.from
        while (true) {
            paths[from]?.let { ticket ->
                result.add(ticket)
                from = ticket.to
            } ?: break

        }
        return result
    }
}

fun main() {
    val test = SortTickets()
    val t3 = TicketY(3, "a", "b", mapOf())
    val t2 = TicketY(2, "b", "c", mapOf())
    val t1 = TicketY(1, "c", "d", mapOf())
    val list = listOf(t1, t2, t3)

    assertEquals(listOf(t3, t2, t1), test.sortTickets(list))

}




