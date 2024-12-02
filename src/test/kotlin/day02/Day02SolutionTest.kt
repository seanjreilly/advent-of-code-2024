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
    fun `isSafe should return false given an input where all the levels are the same`() {
        val levels = listOf(1, 1, 1, 1, 1)
        assert(!isSafe(levels))
    }

    @Test
    fun `isSafeWithDampening should return true if removing a single level from the input would make it safe`() {
        assert(isSafeWithDampening(parseLine(sampleInput[0])))
        assert(isSafeWithDampening(parseLine(sampleInput[3])))
        assert(isSafeWithDampening(parseLine(sampleInput[4])))
        assert(isSafeWithDampening(parseLine(sampleInput[5])))
    }

    @Test
    fun `isSafeWithDampening should return false if removing any single level from the input still won't make it safe`() {
        assert(!isSafeWithDampening(parseLine(sampleInput[1])))
        assert(!isSafeWithDampening(parseLine(sampleInput[2])))
    }

    @Test
    fun `isSafeWithDampening should return true for otherwise valid levels where the first two numbers match`() {
        assert(isSafeWithDampening(listOf(75, 75, 76, 77, 79, 81)))
    }

    @Test
    fun `part1 should count the number of safe levels`() {
        assert(solution.part1(sampleInput) == 2)
    }

    @Test
    fun `part2 should count the number of safe levels with dampening`() {
        assert(solution.part2(sampleInput) == 4)
    }
}

