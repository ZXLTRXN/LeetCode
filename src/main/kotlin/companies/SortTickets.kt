package companies

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



