package medium

/**
 *  * Yandex
 * Условия задачи:
 *
 * Дан родительский контейнер с фиксированной шириной parentWidth и список спецификаций ширин дочерних View (childSpecs).
 * Каждый элемент childSpecs[i] описывает, как рассчитывать ширину дочернего View:
 *
 *     Если childSpecs[i] > 0, то ширина дочернего View фиксированная и равна childSpecs[i].
 *     Если childSpecs[i] <= 0, то это доля, которую этот дочерний View займет от оставшегося пространства в родительском контейнере (после учета всех фиксированных ширин).
 *
 * Требуется: Определить итоговые размеры всех дочерних View.
 *
 * Пример: Входные данные:
 *
 *     parentWidth = 100
 *     childSpecs = listOf(50, -3, -2)
 *
 * Результат:
 *
 *     Итоговые размеры: listOf(50, 30, 20)
 */
class ViewSizes {

    fun solve(parentWidth: Int, childSpecs: List<Int>) {
        var fixedWidth = 0
        var finalWidths = mutableListOf<Int>()

        // неверно, надо порядок не потерять, но суть ясна, просто 2 раза пройтись надо
        val (exact, partial) = childSpecs.partition { it > 0 }

        for (childWidth in exact) {
            if (childWidth > 0) {
                val supportedChildWidth = parentWidth - fixedWidth
                val finalChildWidth = if (childWidth <= supportedChildWidth) {
                    childWidth
                } else {
                    supportedChildWidth
                }
                finalWidths.add(finalChildWidth)
                fixedWidth -= finalChildWidth
            }
        }

        val totalParts = partial.reduce { acc, v ->
            acc - v
        }

        val supportedChildWidth = parentWidth - fixedWidth
        val partWidth = supportedChildWidth / totalParts

        for (childWidthPart in partial) {
            val finalChildWidth = partWidth * childWidthPart
            finalWidths.add(finalChildWidth)
            fixedWidth -= finalChildWidth
        }



    }

}