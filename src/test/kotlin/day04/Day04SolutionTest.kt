package day04

import org.junit.jupiter.api.Test
import utils.Point

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

    private val part2InputWithNonDiagonalCrosses = """
        .M....
        MAS...
        .S.S..
        ..A...
        .M.M..
    """.trimIndent().lines()

    private val part2SampleInput = """
        .M.S......
        ..A..MSMS.
        .M.S.MAA..
        ..A.ASMSM.
        .M.S.M....
        ..........
        S.S.S.S.S.
        .A.A.A.A..
        M.M.M.M.M.
        ..........
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
    fun `detectDiagonalCross should return false given a 3x3 grid of points that doesn't have diagonally crossed MASes in it`() {
        val grid = """
            M.M
            .A.
            S.M
        """.trimIndent().lines().toGrid()

        assert(!detectDiagonalCross(grid, Point(1,1)))
    }

    @Test
    fun `detectDiagonalCross should return false given a 3x3 grid of points with a diagonal and vertical MAS`() {
        val grid = """
            M.M
            MAA
            ...
        """.trimIndent().lines().toGrid()

        assert(!detectDiagonalCross(grid, Point(1,1)))
    }

    @Test
    fun `detectDiagonalCross should return false given a 3x3 grid of points with a vertical MAS cross`() {
        val grid = """
            .M.
            MAS
            .S.
        """.trimIndent().lines().toGrid()

        assert(!detectDiagonalCross(grid, Point(1,1)))
    }

    @Test
    fun `detectDiagonalCross should return true given a 3x3 grid of points that has diagonally crossed MASes in it`() {
        val grid = """
            M.M
            .A.
            S.S
        """.trimIndent().lines().toGrid()

        assert(detectDiagonalCross(grid, Point(1,1)))
    }

    @Test
    fun `detectDiagonalCross should return true given a different 3x3 grid of points that has diagonally crossed MASes in it`() {
        val grid = """
            M.S
            .A.
            M.S
        """.trimIndent().lines().toGrid()

        assert(detectDiagonalCross(grid, Point(1,1)))
    }

    @Test
    fun `part1 should count every instance of XMAS in the grid`() {
        assert(solution.part1(sampleInput) == 18)
    }

    @Test
    fun `part2 should count every diagonal MAS cross in the grid`() {
        assert(solution.part2(sampleInput) == 9)
    }

    @Test
    fun `part2 should only count diagonal MAS crosses in the grid`() {
        assert(solution.part2(part2InputWithNonDiagonalCrosses) == 1)
    }

    private fun List<String>.toGrid() = this.map { it.toCharArray().toTypedArray() }.toTypedArray()
}