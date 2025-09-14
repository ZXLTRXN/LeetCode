package easy

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TwoSumsTest {
    lateinit var tc: TwoSums

    @BeforeEach
    fun init() {
        tc = TwoSums()
    }

    @Test
    fun `valid`() {
        Assertions.assertEquals(tc.twoSumOptimal(intArrayOf(2, 7, 11, 15), 9), intArrayOf(0, 1))

    }

    @Test
    fun `not valid`() {

    }
}