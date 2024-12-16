package day16

import org.junit.jupiter.api.Test
import utils.CardinalDirection.East
import utils.Point

class Day16SolutionTest {
    private val sampleInput = """
        ###############
        #.......#....E#
        #.#.###.#.###.#
        #.....#.#...#.#
        #.###.#####.#.#
        #.#.#.......#.#
        #.#.#####.###.#
        #...........#.#
        ###.#.#####.#.#
        #...#.....#.#.#
        #.#.#.###.#.#.#
        #.....#...#.#.#
        #.###.#.#.#.#.#
        #S..#.....#...#
        ###############
    """.trimIndent().lines()

    private val secondSampleInput = """
        #################
        #...#...#...#..E#
        #.#.#.#.#.#.#.#.#
        #.#.#.#...#...#.#
        #.#.#.#.###.#.#.#
        #...#.#.#.....#.#
        #.#.#.#.#.#####.#
        #.#...#.#.#.....#
        #.#.#####.#.###.#
        #.#.#.......#...#
        #.#.###.#####.###
        #.#.#...#.....#.#
        #.#.#.#####.###.#
        #.#.#.........#.#
        #.#.#.#########.#
        #S#.............#
        #################
    """.trimIndent().lines()

    private val solution = Day16Solution()

    @Test
    fun `parseMaze should return a maze with a start, end, and walls`() {
        val expectedStart = Point(1, 13) facing East
        val expectedEnd = Point(13, 1)

        val maze: Maze = parseMaze(sampleInput)

        assert(maze.start == expectedStart)
        assert(maze.end == expectedEnd)
        assert(Point(0,0) in maze.walls)
        assert(Point(1,0) in maze.walls)
        assert(Point(0,1) in maze.walls)
        assert(Point(1,1) !in maze.walls)

        assert(maze.start.point !in maze.walls)
        assert(maze.start.point.north() !in maze.walls)
        assert(maze.start.point.east() !in maze.walls)
        assert(maze.walls.size == 121)
    }

    @Test
    fun `findLowestCostToEnd should find the lowest cost route from start to end`() {
        val maze: Maze = parseMaze(sampleInput)
        val expectedLowestCost = 7036

        val cost: Int = maze.findLowestCostToEnd()

        assert(cost == expectedLowestCost)
    }

    @Test
    fun `findLowestCostToEnd should find the lowest cost route from start to end given the second maze`() {
        val maze: Maze = parseMaze(secondSampleInput)
        val expectedLowestCost = 11048

        val cost: Int = maze.findLowestCostToEnd()

        assert(cost == expectedLowestCost)
    }

    @Test
    fun `countPointsOnLowestCostToEnd should count the number of tiles on the lowest cost route from start to end`() {
        val maze: Maze = parseMaze(sampleInput)
        val expectedNumberOfPoints = 45

        val numberOfPoints: Int = maze.countPointsOnLowestCostToEnd()

        assert(numberOfPoints == expectedNumberOfPoints)
    }

    @Test
    fun `countPointsOnLowestCostToEnd should count the number of tiles on the lowest cost route from start to end given the second example`() {
        val maze: Maze = parseMaze(secondSampleInput)
        val expectedNumberOfPoints = 64

        val numberOfPoints: Int = maze.countPointsOnLowestCostToEnd()

        assert(numberOfPoints == expectedNumberOfPoints)
    }

    @Test
    fun `part1 should return the minimum cost through the maze`() {
        assert(solution.part1(sampleInput) == 7036)
        assert(solution.part1(secondSampleInput) == 11048)
    }

    @Test
    fun `part2 should count the number of nodes on the best path(s) through the maze`() {
        assert(solution.part2(sampleInput) == 45)
        assert(solution.part2(secondSampleInput) == 64)
    }
}

