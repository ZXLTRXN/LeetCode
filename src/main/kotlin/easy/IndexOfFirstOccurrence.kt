package easy

/**
 * https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/description
 * найди в строке подстроку и верни индекс либо -1
 */
class IndexOfFirstOccurrence {

    // o(n*m) n — длина haystack m — длина needle, ram o(1)
    fun find(haystack: String, needle: String): Int {
        if (haystack.isEmpty() || needle.isEmpty()) return -1
        for (i in haystack.indices) {
            if (haystack[i] == needle[0]) {
                for (j in needle.indices) {
                    if (i + j > haystack.lastIndex || haystack[i + j] != needle[j]) break
                    if (j == needle.lastIndex) return i
                }
            }
        }
        return -1
    }

    // KMP
    // Потому что таблица LPS описывает саму структуру шаблона —
    // она говорит, какие куски шаблона повторяются внутри самого шаблона.
    // lps[i] = хранит, насколько далеко можно “откатиться” в шаблоне,
    // чтобы не терять уже найденное совпадение, а именно длину совпадающих частей(len)
    // = индекс где мы можем начать наше новое сравнение
    // не откатываясь к началу, если совпадение с подстрокой оборвалось.
    // В целом это работает так: если iый lenый совпадают, то мы длину увеличиваем на один и записываем это в табличку,
    // В противном случае, если длина не ноль, то мы длине присваиваем предыдущее значение длины
    // в противном же случае сбрасываю все в ноль, что говорит нам о том что нету такого места куда мы можем вернуться,
    // чтобы не начинать сравнение сначала
    fun createLPSTable(pattern: String): IntArray{
        val lps = IntArray(pattern.length) { 0 }
        var len = 0
        var i = 1
        while (i < pattern.length) {
            if(pattern[i] == pattern[len]) {
                len++
                lps[i] = len
                i++
            } else {
                if(len != 0) {
                    len = lps[len-1]
                } else {
                    lps[i] = 0
                    i++
                }
            }
        }
        return lps
    }

    fun findKMP(haystack: String, needle: String): Int {
        if (haystack.isEmpty() || needle.isEmpty()) return -1
        var i = 0
        var j = 0
        val lps = createLPSTable(needle)
        while(i < haystack.length) {
            if(haystack[i] == needle[j]) {
                i++
                j++
                if (j == needle.length) {
                    return i - j
                }
            } else {
                if (j != 0) {
                    j = lps[j-1] // откатываем шаблон
                } else {
                    i++ // несовпадение на самом начале — просто идём дальше
                }

            }
        }
        return -1
    }


}

fun main() {
    val test = IndexOfFirstOccurrence()

    require(test.findKMP("sadbutsad", "sad") == 0)
    require(test.findKMP("leetcode", "leeto") == -1)
    require(test.findKMP("leetcode", "") == -1)
    require(test.findKMP("", "") == -1)
    require(test.findKMP("", "fwf") == -1)
}