package day02

import org.junit.jupiter.api.Test

class Day02SolutionTest {
    private val sampleInput = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
        """.trimIndent().lines()

    private val solution = Day02Solution()

    @Test
    fun `parseLine should return a list of numbers`() {
        val expectedResult = listOf(7, 6, 4, 2, 1)
        val result = parseLine(sampleInput.first())
        assert(result == expectedResult)
    }

    @Test
    fun `isSafe should return true given an input where all of the numbers are increasing by 1 - 3`() {
        val levels = listOf(1, 2, 4, 7)
        assert(isSafe(levels))
    }

    @Test
    fun `isSafe should return false given an input where a level increases by more than 3`() {
        val levels = listOf(1, 3, 7)
        assert(!isSafe(levels))
    }

    @Test
    fun `isSafe should return true given an input where all of the levels are decreasing by 1-3`() {
        val levels = listOf(9, 8, 6, 3)
        assert(isSafe(levels))
    }

    @Test
    fun `isSafe should return false given an input where a level decreases by more than 3`() {
        val levels = listOf(9, 8, 4)
        assert(!isSafe(levels))
    }

    @Test
    fun `isSafe should return false given an input where levels increase and decrease`() {
        val levels = listOf(1, 2, 3, 2, 1)
        assert(!isSafe(levels))
    }

    @Test
    fun `isSafe should return false given an input where the level doesn't change`() {
        val levels = listOf(8, 6, 4, 4, 1)
        assert(!isSafe(levels))
    }

    @Test
    fun `part1 should count the number of safe levels`() {
        assert(solution.part1(sampleInput) == 2)
    }
}

