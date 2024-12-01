package day01

import org.junit.jupiter.api.Test

class Day01SolutionTest {
    private val sampleInput = """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent().lines()

    private val solution = Day01Solution()

    @Test
    fun `parseLine should return a pair of integers given a string of numbers separated by whitespace`() {
        assert(parseLine(sampleInput[0]) == Pair(3,4))
        assert(parseLine(sampleInput[1]) == Pair(4,3))
        assert(parseLine(sampleInput[2]) == Pair(2,5))
        assert(parseLine(sampleInput[3]) == Pair(1,3))
        assert(parseLine(sampleInput[4]) == Pair(3,9))
        assert(parseLine(sampleInput[5]) == Pair(3,3))
    }

    @Test
    fun `absoluteDifference should return 0 given two equal integers`() {
        val result = absoluteDifference(6, 6)
        assert(result == 0)
    }

    @Test
    fun `absoluteDifference should return the first number minus the second given two integers with the first larger than the second`() {
        val result = absoluteDifference(5, 3)
        assert(result == 2)
    }

    @Test
    fun `absoluteDifference should return the second number minus the first given two integers with the second larger than the first`() {
        val result = absoluteDifference(2, 9)
        assert(result == 7)
    }

    @Test
    fun `part1 should parse two lists of numbers, sort them by size, compute the absolute difference betwwen the nth position in each list, and return the sum`() {
        val result = solution.part1(sampleInput)
        assert(result == 11)
    }
}