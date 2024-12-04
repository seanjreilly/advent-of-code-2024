package day04

import org.junit.jupiter.api.Test

class Day04SolutionTest {
    private val sampleInput = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent().lines()

    private val simpleSampleInput = """
        ..X...
        .SAMX.
        .A..A.
        XMAS.S
        .X....
    """.trimIndent().lines()

    private val solution = Day04Solution()

    @Test
    fun `countXmasInstances should count every instance of XMAS in the grid given simple input`() {
        assert(countXmasInstances(simpleSampleInput) == 4)
    }

    @Test
    fun `countXmasInstances should count every instance of XMAS in the grid`() {
        assert(countXmasInstances(sampleInput) == 18)
    }

    @Test
    fun `part1 should count every instance of XMAS in the grid`() {
        assert(solution.part1(sampleInput) == 18L)
    }
}
