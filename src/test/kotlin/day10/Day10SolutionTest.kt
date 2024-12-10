package day10

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import utils.Bounds
import utils.Point

class Day10SolutionTest {
    private val smallSampleInput = """
        0123
        1234
        8765
        9876
    """.trimIndent().lines()


    private val sampleInput = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent().lines()

    private val solution = Day10Solution()

    @Nested
    inner class TopographicMapTest {
        @Test
        fun `constructor should return a topographic map given input`() {
            val expectedBounds = Bounds(0..3, 0..3)
            val expectedZero = Point(0,0)
            val expectedOnes = setOf(Point(1,0), Point(0,1))
            val expectedTwos = setOf(Point(2,0), Point(1,1))
            val expectedThrees = setOf(Point(3,0), Point(2,1))
            val expectedFour = Point(3,1)
            val expectedFive = Point(3,2)
            val expectedSixes = setOf(Point(2,2), Point(3,3))
            val expectedSevens = setOf(Point(1,2), Point(2,3))
            val expectedEights = setOf(Point(0,2), Point(1,3))
            val expectedNine = Point(0,3)

            val map = TopographicMap(smallSampleInput)

            assert(map.bounds == expectedBounds)
            assert(map.count() == 16)
            assert(map[expectedZero] == 0)
            expectedOnes.forEach { assert(map[it] == 1) }
            expectedTwos.forEach { assert(map[it] == 2) }
            expectedThrees.forEach { assert(map[it] == 3) }
            assert(map[expectedFour] == 4)
            assert(map[expectedFive] == 5)
            expectedSixes.forEach { assert(map[it] == 6) }
            expectedSevens.forEach { assert(map[it] == 7) }
            expectedEights.forEach { assert(map[it] == 8) }
            assert(map[expectedNine] == 9)
        }

        @Test
        fun `countReachableNines should use Djikstra's algorithm to find the number of unique points of height nine reachable from this point`() {
            val map = TopographicMap(sampleInput)

            assert(map.countReachableNines(Point(2,0)) == 5)
            assert(map.countReachableNines(Point(4,0)) == 6)
        }
        
        @Test
        fun `findTrailheads should return the position of every zero`() {
            val map = TopographicMap(sampleInput)

            val expectedResult = setOf(
                Point(2,0),
                Point(4,0),
                Point(4,2),
                Point(6,4),
                Point(2,5),
                Point(5,5),
                Point(0,6),
                Point(6,6),
                Point(1,7),
            )

            val result : Collection<Point> = map.findTrailheads()

            assert(result.size == 9)
            assert(result.toSet() == expectedResult)
        }

        @Test
        fun `getNeighbours should return neighbouring points with a gentle slope up`() {
            val expectedOnes = setOf(Point(1,0), Point(0,1))
            val expectedTwos = setOf(Point(2,0), Point(1,1))
            val expectedThrees = setOf(Point(3,0), Point(2,1))
            val expectedFour = setOf(Point(3,1))
            val expectedFive = setOf(Point(3,2))
            val expectedSixes = setOf(Point(2,2), Point(3,3))
            val expectedSevens = setOf(Point(1,2), Point(2,3))
            val expectedEights = setOf(Point(0,2), Point(1,3))
            val expectedNine = setOf(Point(0,3))

            val map = TopographicMap(smallSampleInput)

            assert(map.getNeighbours(Point(0,0)).toSet() == expectedOnes)
            assert(map.getNeighbours(Point(1,0)).toSet() == expectedTwos)
            assert(map.getNeighbours(Point(2,0)).toSet() == expectedThrees)
            assert(map.getNeighbours(Point(3,0)).toSet() == expectedFour)
            assert(map.getNeighbours(Point(3,1)).toSet() == expectedFive)
            assert(map.getNeighbours(Point(3,2)).toSet() == expectedSixes)
            assert(map.getNeighbours(Point(2,2)).toSet() == expectedSevens)
            assert(map.getNeighbours(Point(1,2)).toSet() == expectedEights)
            assert(map.getNeighbours(Point(0,2)).toSet() == expectedNine)
        }
    }
    
    @Test
    fun `part1 should find the score of every trailhead and return the sum`() {
        assert(solution.part1(sampleInput) == 36L)
    }
}

