package day06

import org.junit.jupiter.api.Test
import utils.Bounds
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
    fun `addObstruction should return a modified map with an additional obstruction`() {
        val grid = """
            .^.
            ...
            ...
        """.trimIndent().lines()

        val expectedResult = """
            .^.
            ...
            #..
        """.trimIndent().lines()

        val result: List<String> = addObstruction(grid, Point(0, 2))

        assert(result == expectedResult)
    }

    @Test
    fun `detectLoop should return false if the guard leaves the map`() {
        val result: Boolean = detectLoop(sampleInput, Bounds(sampleInput), findStart(sampleInput))

        assert(!result)
    }

    @Test
    fun `detectLoop should return true if the guard enters an infinite loop`() {
        val obstruction = Point(3, 6)
        val grid = addObstruction(sampleInput, obstruction)

        val result: Boolean = detectLoop(grid, Bounds(grid), findStart(grid))

        assert(result)
    }

    @Test
    fun `part1 should move until the guard leaves the map and return the number of unique points visited, ignoring direction`() {
        assert(solution.part1(sampleInput) == 41)
    }

    @Test
    fun `part2 should attempt to add an obstruction in every location and count the number of positions that cause the guard to enter an infinite loop`() {
        assert(solution.part2(sampleInput) == 6)
    }
}