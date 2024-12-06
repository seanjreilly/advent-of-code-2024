package day06

import org.junit.jupiter.api.Test
import utils.CardinalDirection.*
import utils.Point
import utils.PointAndDirection

class Day06SolutionTest {
    private val sampleInput = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
        """.trimIndent().lines()

    private val solution = Day06Solution()
    
    @Test
    fun `findStart should return the location of the ^ symbol, pointing North`() {
        val expectedResult = PointAndDirection(Point(4, 6), North)

        val result: PointAndDirection = findStart(sampleInput)

        assert(result == expectedResult)
    }

    @Test
    fun `moveNext should move forward one point if there is no obstacle in that direction, when facing North`() {
        val originalDirection = North
        val start = PointAndDirection(Point(4, 6), originalDirection)

        val result : PointAndDirection = moveNext(start, sampleInput)

        assert(result.point == Point(4, 5))
        assert(result.direction == originalDirection)
    }

    @Test
    fun `moveNext should move forward one point if there is no obstacle in that direction, when facing East`() {
        val originalDirection = East
        val start = PointAndDirection(Point(4, 6), originalDirection)

        val result : PointAndDirection = moveNext(start, sampleInput)

        assert(result.point == Point(5, 6))
        assert(result.direction == originalDirection)
    }

    @Test
    fun `moveNext should move forward one point if there is no obstacle in that direction, when facing South`() {
        val originalDirection = South
        val start = PointAndDirection(Point(4, 6), originalDirection)

        val result : PointAndDirection = moveNext(start, sampleInput)

        assert(result.point == Point(4, 7))
        assert(result.direction == originalDirection)
    }

    @Test
    fun `moveNext should move forward one point if there is no obstacle in that direction, when facing West`() {
        val originalDirection = West
        val start = PointAndDirection(Point(4, 6), originalDirection)

        val result : PointAndDirection = moveNext(start, sampleInput)

        assert(result.point == Point(3, 6))
        assert(result.direction == originalDirection)
    }

    @Test
    fun `moveNext should turn right if there is an obstacle in the current direction`() {
        val originalPosition = Point(4, 1)
        val start = PointAndDirection(originalPosition, North)

        val result : PointAndDirection = moveNext(start, sampleInput)

        assert(result.point == originalPosition)
        assert(result.direction == East)
    }

    @Test
    fun `moveNext should turn right until there is a free path`() {
        val grid = """
            .#.
            .^#
            .#.
        """.trimIndent().lines()

        val start = findStart(grid)
        assert(start.direction == North)

        //should turn right
        var currentPointAndDirection = moveNext(start, grid)
        assert(currentPointAndDirection.point == start.point)
        assert(currentPointAndDirection.direction == East)

        //should turn right again
        currentPointAndDirection = moveNext(currentPointAndDirection, grid)
        assert(currentPointAndDirection.point == start.point)
        assert(currentPointAndDirection.direction == South)

        //should turn right again
        currentPointAndDirection = moveNext(currentPointAndDirection, grid)
        assert(currentPointAndDirection.point == start.point)
        assert(currentPointAndDirection.direction == West)

        //should finally move forward
        currentPointAndDirection = moveNext(currentPointAndDirection, grid)
        assert(currentPointAndDirection.point == Point(0,1))
        assert(currentPointAndDirection.direction == West)
    }

    @Test
    fun `moveNext should not throw an exception when moving out of the grid`() {
        val grid = """
            .^.
            ...
            ...
        """.trimIndent().lines()

        val start = findStart(grid)
        assert(start.point == Point(1, 0)) { "precondition check on starting point" }

        val exit = moveNext(start, grid)
        assert(exit.point == Point(1, -1))
        assert(exit.direction == North)
    }

    @Test
    fun `part1 should move until the guard leaves the map and return the number of unique points visited, ignoring direction`() {
        assert(solution.part1(sampleInput) == 41)
    }
}