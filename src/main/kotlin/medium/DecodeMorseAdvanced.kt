package medium

// https://www.codewars.com/kata/54b72c16cd7f5154e9000457
class DecodeMorseAdvanced {
    fun decodeBits(bits: String): String {
        val trimmedBits = bits.trim { it == '0' }
        if (trimmedBits.isEmpty()) return ""

        val rate = calculateTransmissionRate(trimmedBits)

        val result = StringBuilder()
        var currentChar = trimmedBits[0]
        var counter = 0

        // Простой способ обработать группы — это пройти по строке
        // и сбрасывать символ каждый раз, когда он меняется
        for (bit in trimmedBits) {
            if (bit == currentChar) {
                counter++
            } else {
                result.append(defineSymbol(currentChar, counter, rate))
                currentChar = bit
                counter = 1
            }
        }
        // Не забываем обработать самый последний хвост после цикла
        result.append(defineSymbol(currentChar, counter, rate))

        return result.toString()
    }

    fun defineSymbol(bit: Char, length: Int, rate: Int): String {
        val units = length / rate
        return when (bit) {
            '1' -> if (units == 1) "." else "-"
            '0' -> when (units) {
                1 -> ""
                3 -> " "
                7 -> "   "
                else -> ""
            }
            else -> ""
        }
    }

    fun calculateTransmissionRate(bits: String): Int {
        var minLength = Int.MAX_VALUE
        var currentLength = 0
        var lastChar = bits[0]

        for (char in bits) {
            if (char == lastChar) {
                currentLength++
            } else {
                if (currentLength < minLength) minLength = currentLength
                lastChar = char
                currentLength = 1
            }
        }
        // Проверяем последний блок
        if (currentLength < minLength) minLength = currentLength

        return minLength
    }

    fun decodeMorse(code: String): String {
        val words = code.split("   ")
        var result = StringBuilder()
        words.forEachIndexed { idx, word ->
            val chars = word.split(" ")
            chars.forEach { result.append(
//                MORSE_CODE[it]
                null
                    ?: "") }
            if (idx != word.lastIndex) result.append(" ")
        }
        return result.toString()
    }
}