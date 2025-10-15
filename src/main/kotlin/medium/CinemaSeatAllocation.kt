package medium


class CinemaSeatAllocation {
    fun maxNumberOfFamilies(n: Int, reservedSeats: Array<IntArray>): Int {
        val mask1 = intArrayOf(1, 2, 3, 4)
        val mask2 = intArrayOf(5, 6, 7, 8)
        val mask3 = intArrayOf(3, 4, 5, 6)

        val occupiedSeats = HashMap<Int, MutableList<Int>>()
        
        for (occupiedSeat in reservedSeats) {
            occupiedSeats.computeIfAbsent(occupiedSeat[0] - 1) { mutableListOf() }.add(occupiedSeat[1] - 1)
        }

        var result = 0

        for (rowIndex in occupiedSeats.keys) {
            val mask1Collision = occupiedSeats[rowIndex]!!.any { mask1.contains(it) }
            val mask2Collision = occupiedSeats[rowIndex]!!.any { mask2.contains(it) }

            if(mask1Collision || mask2Collision) {
                if (mask1Collision && mask2Collision) {
                    val mask3Collision = occupiedSeats[rowIndex]!!.any { mask3.contains(it) }
                    if (!mask3Collision) result++
                } else {
                    result++
                }
            } else {
                result += 2
            }
        }

        result += 2 * (n - occupiedSeats.keys.size)
        return result
    }

}

fun main() {
    val test = CinemaSeatAllocation()


}

