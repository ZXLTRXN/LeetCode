package medium

class HumanReadableDuration {
    fun solution(seconds: Int): String {
        if (seconds == 0) return "now"
        val timeAssociations = linkedMapOf<TimePeriod, Int>()

        var remainder = seconds

        if (remainder >= TimePeriod.YEAR.seconds) {
            val years = remainder / TimePeriod.YEAR.seconds
            if (years > 0) {
                timeAssociations[TimePeriod.YEAR] = years
                remainder %= TimePeriod.YEAR.seconds
            }
        }

        if (remainder >= TimePeriod.DAY.seconds) {
            val days = remainder / TimePeriod.DAY.seconds
            if (days > 0) {
                remainder %= TimePeriod.DAY.seconds
                timeAssociations[TimePeriod.DAY] = days
            }
        }
        if (remainder >= TimePeriod.HOUR.seconds) {
            val hours = remainder / TimePeriod.HOUR.seconds
            if (hours > 0) {
                remainder %= TimePeriod.HOUR.seconds
                timeAssociations[TimePeriod.HOUR] = hours
            }
        }
        if (remainder >= TimePeriod.MINUTE.seconds) {
            val minutes = remainder / TimePeriod.MINUTE.seconds
            if (minutes > 0) {
                remainder %= TimePeriod.MINUTE.seconds
                timeAssociations[TimePeriod.MINUTE] = minutes
            }
        }
        if (remainder > 0) {
            timeAssociations[TimePeriod.SECOND] = remainder
        }

        return timeAssociations.entries.mapIndexed { idx, entry ->
            val ending = if (entry.value > 1) "s" else ""
            var s = "${entry.value} ${entry.key.name}$ending"
            if (idx < timeAssociations.entries.size - 1) {
                s += if (idx == timeAssociations.entries.size - 2) {
                    " and "
                } else {
                    ", "
                }
            }
            s
        }.joinToString("")
    }
}

enum class TimePeriod(val seconds: Int) {
    YEAR(365 * 24 * 60 * 60),
    DAY(24 * 60 * 60),
    HOUR(60 * 60),
    MINUTE(60),
    SECOND(1)
}