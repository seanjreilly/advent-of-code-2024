package day25

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day25SolutionTest {
    private val sampleInput = """
        #####
        .####
        .####
        .####
        .#.#.
        .#...
        .....

        #####
        ##.##
        .#.##
        ...##
        ...#.
        ...#.
        .....

        .....
        #....
        #....
        #...#
        #.#.#
        #.###
        #####

        .....
        .....
        #.#..
        ###..
        ###.#
        ###.#
        #####

        .....
        .....
        .....
        #....
        #.#..
        #.#.#
        #####
    """.trimIndent().lines()

    private val solution = Day25Solution()
    
    @Test
    fun `parseLocks should return Lock instances for the chunks that have the top row filled`() {
        val expectedResult: List<Lock> = listOf(
            Lock(0,5,3,4,3),
            Lock(1,2,0,5,3)
        )

        val result: List<Lock> = parseLocks(sampleInput)

        assert(result == expectedResult)
    }

    @Test
    fun `parseKeys should return Key instances for the chunks that have the top row empty`() {
        val expectedResult: List<Key> = listOf(
            Key(5,0,2,1,3),
            Key(4,3,4,0,2),
            Key(3,0,2,0,1),
        )

        val result: List<Key> = parseKeys(sampleInput)

        assert(result == expectedResult)
    }

    @Nested
    inner class LockTest {
        @Test
        fun `fits should return true given a key that fits`() {
            val lock = Lock(0,5,3,4,3)
            val key = Key(3,0,2,0,1)

            assert(lock.fits(key))
        }

        @Test
        fun `fits should return false given a key that doesn't fit`() {
            val lock = Lock(0,5,3,4,3)
            val key = Key(3,1,2,0,1)

            assert(!lock.fits(key))
        }
    }

    @Test
    fun `part1 should count the number of lock and key combinations that fit`() {
        assert(solution.part1(sampleInput) == 3)
    }
}