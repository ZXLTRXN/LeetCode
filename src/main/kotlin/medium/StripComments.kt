package medium

// https://www.codewars.com/kata/51c8e37cee245da6b40000bd
class StripComments {
    fun solution(input: String, markers: CharArray): String {
        val markersSet = markers.toHashSet()

        return input.lineSequence().map { line ->
            var markersIdx = -1 // line.indexOfFirst { markersSet.contains(it) }
            for (chIdx in line.indices) {
                if (markersSet.contains(line[chIdx])) {
                    markersIdx = chIdx
                    break
                }
            }

            val end =  if (markersIdx != -1) markersIdx else line.lastIndex + 1
            line.subSequence(0..<end).trimEnd()
        }.joinToString("\n")
    }

}

/*
apples, pears # and bananas
grapes
bananas !apples
 */