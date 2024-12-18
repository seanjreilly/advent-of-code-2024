package day18

import org.junit.jupiter.api.Test
import utils.Bounds
import utils.DijkstrasAlgorithm
import utils.Point

class Day18SolutionTest {
    private val sampleInput = """
        5,4
        4,2
        4,5
        3,0
        2,1
        6,3
        2,4
        1,5
        0,6
        3,3
        2,6
        5,1
        1,2
        5,5
        2,5
        6,5
        1,4
        0,4
        6,4
        1,1
        6,1
        1,0
        0,5
        1,6
        2,0
    """.trimIndent().lines()

    private val TEST_BOUNDS = Bounds(0..6, 0..6)
    private val TEST_PART1_FALLEN_BYTES = 12

    private val solution = Day18Solution(TEST_BOUNDS, TEST_PART1_FALLEN_BYTES)

    @Test
    fun `parseFallingByte should return the correct point given the first line of sample input`() {
        val line = sampleInput.first()
        val fallingByte : Point = parseFallingByte(line)
        assert(fallingByte == Point(5,4))
    }

    @Test
    fun `parseFallingByte should return the correct point given the second line of sample input`() {
        val line = sampleInput[1]
        val fallingByte : Point = parseFallingByte(line)
        assert(fallingByte == Point(4,2))
    }

    @Test
    fun `parseFallingBytes should return a list of Points`() {
        val fallingBytes : List<Point> = parseFallingBytes(sampleInput)

        assert(fallingBytes.size == 25)
        assert(fallingBytes[0] == Point(5,4))
        assert(fallingBytes[1] == Point(4,2))
        assert(fallingBytes[2] == Point(4,5))
        assert(fallingBytes[3] == Point(3,0))
        assert(fallingBytes[4] == Point(2,1))
        assert(fallingBytes.last() == Point(2,0))
    }

    @Test
    fun `getExitPoint should return the bottom right corner of the bounds given test bounds`() {
        val exitPoint: Point = getExitPoint(TEST_BOUNDS)
        assert(exitPoint == Point(6, 6))
    }

    @Test
    fun `getExitPoint should return the bottom right corner of the bounds given prod bounds`() {
        val exitPoint: Point = getExitPoint(PROD_BOUNDS)
        assert(exitPoint == Point(70, 70))
    }

    @Test
    fun `findShortestPathToExit should use Dijkstra's to find the shortest path to the exit given a bounds, a list of falling bytes, and how many bytes have fallen, and should return the length of the shortest path`() {
        val result: Int = findShortestPathToExit(TEST_BOUNDS, parseFallingBytes(sampleInput), 12)
        assert(result == 22)
    }

    @Test
    fun `part1 should return the number of nodes in the shortest path to the exit given the conditions`() {
        assert(solution.part1(sampleInput) == 22L)
    }

}