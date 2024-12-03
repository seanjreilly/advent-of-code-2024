package day03

import org.junit.jupiter.api.Test

class Day03SolutionTest {
    private val sampleInput = """
        xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)
        +mul(32,64]then(mul(11,8)mul(8,5))
    """.trim().lines()

    private val solution = Day03Solution()

    @Test
    fun `findMulInstructions should find all of the valid mul instructions given a string`() {
        val expectedInstructions = listOf(
            2 to 4,
            5 to 5,
            11 to 8,
            8 to 5
        )

        val instructions: List<Pair<Int,Int>> = findMulInstructions(sampleInput).toList()
        assert(instructions == expectedInstructions)
    }

    @Test
    fun `part1 should return the sum of all valid mul instructions in the input`() {
        assert(solution.part1(sampleInput) == 161)
    }
}