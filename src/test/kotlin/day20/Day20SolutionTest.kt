package day20

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import utils.Bounds
import utils.Point
import utils.get

const val PART1_MINIMUM_SAVINGS_TEST = 40

class Day20SolutionTest {
    private val sampleInput = """
        ###############
        #...#...#.....#
        #.#.#.#.#.###.#
        #S#...#.#.#...#
        #######.#.#.###
        #######.#.#...#
        #######.#.###.#
        ###..E#...#...#
        ###.#######.###
        #...###...#...#
        #.#####.#.###.#
        #.#...#.#.#...#
        #.#.#.#.#.#.###
        #...#...#...###
        ###############
    """.trimIndent().lines()

    private val solution = Day20Solution(PART1_MINIMUM_SAVINGS_TEST)

    @Test
    fun `count the number of stars in the production input`() {
        val input = solution.readInput()
        val count = Bounds(input).count { input[it] == '#' }
        println(count)
        assert(count > 10000)
    }

    @Test
    fun `there is only one path from start to end that doesn't hit a wall without cheating`() {
        val racetrack = Racetrack(solution.readInput())
        var visitedPoints = mutableSetOf<Point>()
        var currentPoint = racetrack.start
        while (currentPoint != racetrack.end) {
            visitedPoints += currentPoint
            val neighbours = currentPoint.getCardinalNeighbours()
                .filter { it in racetrack.bounds }
                .filter { it !in racetrack.walls }
                .filter { it !in visitedPoints }

            assert(neighbours.size == 1) { "Each point must have 1 unvisited point from start to end" }
            currentPoint = neighbours.first()
        }
        println("Path from beginning to end is ${visitedPoints.size} points long")
    }

    @Nested
    inner class RacetrackTest {

        @Test
        fun `constructor should return the expected race track configuration given sample input`() {
            val expectedStart = Point(1, 3)
            val expectedEnd = Point(5, 7)
            val expectedWallCount = 140

            @Suppress("RedundantExplicitType") //used to create the class in the first place
            val racetrack: Racetrack = Racetrack(sampleInput)

            assert(racetrack.start == expectedStart)
            assert(racetrack.end == expectedEnd)
            assert(racetrack.walls.size == expectedWallCount)
            assert(racetrack.bounds == Bounds(sampleInput))
        }

        @Test
        fun `fastestPathWithoutCheating should find the fastest path from start to end without cheating`() {
            val racetrack = Racetrack(sampleInput)
            val bounds = Bounds(sampleInput)

            val fastestPath: List<Point> = racetrack.fastestPathWithoutCheating()

            assert(fastestPath.size == 85) //84 moves plus one destination
            assert(fastestPath.first() == racetrack.start)
            assert(fastestPath.last() == racetrack.end)
            fastestPath.forEach {
                assert(it in bounds) {"fastest path should not go out of bounds"}
                assert(it !in racetrack.walls) {"fastest path should not hit a wall"}
            }

            val moves = fastestPath.windowed(2)
            moves.forEach { (prev, next) ->
                assert(next in prev.getCardinalNeighbours()) {"path should go up, down, left or right at each turn"}
            }
        }

        @Test
        fun `countCheats should find 44 potential cheats given a minimum savings 1 picosecond`() {
            val minimumSavings = 1
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 44)
        }

        @Test
        fun `countCheats should find 44 potential cheats given a minimum savings of 2 picoseconds`() {
            val minimumSavings = 2
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 44)
        }

        @Test
        fun `countCheats should find 30 potential cheats given a minimum savings of 3 picoseconds`() {
            val minimumSavings = 3
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 30)
        }

        @Test
        fun `countCheats should find 16 potential cheats given a minimum savings of 6 picoseconds`() {
            val minimumSavings = 6
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 16)
        }

        @Test
        fun `countCheats should find 14 potential cheats given a minimum savings of 8 picoseconds`() {
            val minimumSavings = 8
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 14)
        }

        @Test
        fun `countCheats should find 10 potential cheats given a minimum savings of 10 picoseconds`() {
            val minimumSavings = 10
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 10)
        }

        @Test
        fun `countCheats should find 8 potential cheats given a minimum savings of 12 picoseconds`() {
            val minimumSavings = 12
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 8)
        }

        @Test
        fun `countCheats should find 5 potential cheats given a minimum savings of 13 picoseconds`() {
            val minimumSavings = 13
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 5)
        }

        @Test
        fun `countCheats should find 5 potential cheats given a minimum savings of 20 picoseconds`() {
            val minimumSavings = 20
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 5)
        }

        @Test
        fun `countCheats should find 4 potential cheats given a minimum savings of 36 picoseconds`() {
            val minimumSavings = 36
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 4)
        }

        @Test
        fun `countCheats should find 3 potential cheats given a minimum savings of 38 picoseconds`() {
            val minimumSavings = 38
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 3)
        }

        @Test
        fun `countCheats should find 2 potential cheats given a minimum savings of 40 picoseconds`() {
            val minimumSavings = 40
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 2)
        }

        @Test
        fun `countCheats should find 1 potential cheat given a minimum savings of 64 picoseconds`() {
            val minimumSavings = 64
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 1)
        }

        @Test
        fun `countCheats should find no potential cheats given a minimum savings of 65 picoseconds`() {
            val minimumSavings = 65
            val racetrack = Racetrack(sampleInput)

            val cheatsFound: Int = racetrack.countCheats(minimumSavings)

            assert(cheatsFound == 0)
        }
    }

    @Test
    fun `part1 should count the number of cheats that return at least the minimum savings and return the result`() {
        assert(solution.part1(sampleInput) == 2L)
    }
}