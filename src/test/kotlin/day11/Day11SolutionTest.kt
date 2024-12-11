package day11

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day11SolutionTest {
    private val sampleInput = """
        0 1 10 99 999
        """.trimIndent().lines()

    private val smallSampleInput = "125 17"

    private val solution = Day11Solution()

    @Test
    fun `String dot toStones() should return a list of Stone instances`() {
        val expectedResult = listOf(
            Stone(0),
            Stone(1),
            Stone(10),
            Stone(99),
            Stone(999)
        )

        assert(sampleInput.first().toStones() == expectedResult)
    }

    @Test
    fun `String dot toParallelStones() should return a ParallelStone instance`() {
        val expectedResult = ParallelStones(mapOf(
            Stone(0) to 1,
            Stone(1) to 1,
            Stone(10) to 1,
            Stone(99) to 1,
            Stone(999) to 1
        ))

        assert(sampleInput.first().toParallelStones() == expectedResult)
    }

    @Test
    fun `String dot toStones() should work for production input`() {
        assert(solution.readInput().first().toStones().isNotEmpty())
    }

    @Test
    fun `List of Stones dot blink() should blink every Stone in the list, preserving order`() {
        val expectedResult = "1 2024 1 0 9 9 2021976".toStones()
        val stones = sampleInput.first().toStones()

        assert(stones.blink() == expectedResult)
    }

    @Nested
    inner class StoneTest {
        @Test
        fun `blink should return a single Stone(1) given Stone(0)`() {
            val expectedResult = listOf(Stone(1))

            assert(Stone(0).blink() == expectedResult)
        }

        @Test
        fun `blink should return two Stones with half of the digits each given a Stone with an even number of digits`() {
            assert(Stone(12).blink() == listOf(Stone(1), Stone(2)))
            assert(Stone(1234).blink() == listOf(Stone(12), Stone(34)))
            assert(Stone(123456).blink() == listOf(Stone(123), Stone(456)))
            assert(Stone(12345678).blink() == listOf(Stone(1234), Stone(5678)))
        }

        @Test
        fun `blink should remove leading zeroes when splitting a Stone with an even number of digits into two Stones`() {
            assert(Stone(1000).blink() == listOf(Stone(10), Stone(0)))
        }

        @Test
        fun `blink should return a Stone with the value times 2024 given a Stone with an odd number of digits that isn't 0`() {
            assert(Stone(1).blink() == listOf(Stone(2024)))
        }
    }

    @Nested
    inner class ParallelStoneTest {
        @Test
        fun `blink() should blink every Stone, updating counts`() {
            val expectedResult = ParallelStones(mapOf(
                Stone(1) to 2L,
                Stone(2024) to 1L,
                Stone(0) to 1L,
                Stone(9) to 2L,
                Stone(2021976) to 1L
            ))
            val stones = sampleInput.first().toParallelStones()
            val result = stones.blink()

            assert(result == expectedResult)
        }

        @Test
        fun `blinking 25 times should return an expected result`() {
            var stones = smallSampleInput.toParallelStones()
            repeat(25) { stones = stones.blink() }
            assert(stones.totalCount() == 55312L)
        }

        @Test
        fun `totalCount should return the total count of stones`() {
            val stones = sampleInput.first().toParallelStones()

            assert(stones.totalCount() == 5L)
            assert(stones.blink().totalCount() == 7L)
        }
    }

    @Test
    fun `blinking six times should return the correct number of stones`() {
        var stones = smallSampleInput.toStones()
        val expectedResult = 22
        repeat(6) { stones = stones.blink() }

        assert(stones.size == expectedResult)
    }
    
    @Test
    fun `part1 should blink the stones 25 times and return the number of stones`() {
        assert(solution.part1(listOf(smallSampleInput)) == 55312L)
    }

    @Test
    fun `part2 should blink the stones 75 times and return the number of stones`() {
        // calculated this using the solution itself, but it yields the correct answer
        val expectedResult = 65601038650482L
        assert(solution.part2(listOf(smallSampleInput)) == expectedResult)
    }
}