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
    fun `parseLine should return a list of integers given a string of numbers separated by whitespace`() {
        assert(parseLine(sampleInput[0]) == listOf(3, 4))
        assert(parseLine(sampleInput[1]) == listOf(4, 3))
        assert(parseLine(sampleInput[2]) == listOf(2, 5))
        assert(parseLine(sampleInput[3]) == listOf(1, 3))
        assert(parseLine(sampleInput[4]) == listOf(3, 9))
        assert(parseLine(sampleInput[5]) == listOf(3, 3))
    }

    @Test
    fun `parseLines should return two lists of integers given lines of integers separated by whitespace`() {
        val firstExpectedList = listOf(3, 4, 2, 1, 3, 3)
        val secondExpectedList = listOf(4, 3, 5, 3, 9, 3)
        val result = parseLines(sampleInput)

        assert(result == firstExpectedList to secondExpectedList)
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
    fun `countOccurrences should map each entry to the number of times it occurs in the list`() {
        val theList = listOf(4, 3, 5, 3, 9, 3, 3)
        val result = countOccurrences(theList)
        assert(
            result == mapOf(
                4 to 1,
                5 to 1,
                9 to 1,
                3 to 4
            )
        )
    }

    @Test
    fun `part1 should parse two lists of numbers, sort them by size, compute the absolute difference between the nth position in each list, and return the sum`() {
        val result = solution.part1(sampleInput)
        assert(result == 11)
    }

    @Test
    fun `part2 should parse two lists of numbers, multiply each number in the first list by how many times it appears in the second list, and return the sum`() {
        val result = solution.part2(sampleInput)
        assert(result == 31)
    }
}