package day03

import org.junit.jupiter.api.Test

class Day03SolutionTest {
    private val sampleInput = """
        xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
    """.trim()

    private val sampleInputOnMultipleLines = """
        xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)
        +mul(32,64]then(mul(11,8)mul(8,5))
    """.trimIndent().lines()

    private val sampleInputPart2 = """
        xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
    """.trim()

    private val sampleInputPart2OnMultipleLines = """
        xmul(2,4)&mul[3,7]!^don't()_mul(5,5)
        +mul(32,64](mul(11,8)undo()?mul(8,5))
    """.trimIndent().lines()

    private val solution = Day03Solution()

    @Test
    fun `findMulInstructions should find all of the valid mul instructions given a string`() {
        val expectedInstructions = listOf(
            2 to 4,
            5 to 5,
            11 to 8,
            8 to 5
        )

        val instructions: List<Pair<Int,Int>> = findMulInstructions(sampleInput)
        assert(instructions == expectedInstructions)
    }

    @Test
    fun `removeDisabledMulInstructions should find instructions before the initial don't() instruction`() {
        val expectedResult = "mul(1,2)mul(2,3)asdf"
        val input = "${expectedResult}don't()mul(3,4)ailhr"

        val result = removeDisabledMulInstructions(input)

        assert(result == expectedResult)
    }

    @Test
    fun `removeDisabledMulInstructions should remove instructions after a don't() instruction but before a do instruction`() {
        val input = "abcddon't()efghijdo()klmnopqdon't()rstudo()vwxyz"
        val expectedResult = "abcdklmnopqvwxyz"

        val result = removeDisabledMulInstructions(input)

        assert(result == expectedResult)
    }

    @Test
    fun `part1 should return the sum of all valid mul instructions in the input`() {
        assert(solution.part1(listOf(sampleInput)) == 161)
    }

    @Test
    fun `part1 should work with input split over multiple lines`() {
        assert(solution.part1(sampleInputOnMultipleLines) == 161)
    }

    @Test
    fun `part2 should return the sum of all valid and enabled mul instructions in the input`() {
        assert(solution.part2(listOf(sampleInputPart2)) == 48)
    }

    @Test
    fun `part2 should work with input split over multiple lines`() {
        assert(solution.part2(sampleInputPart2OnMultipleLines) == 48)
    }
}