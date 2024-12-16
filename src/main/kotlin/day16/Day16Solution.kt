package day16

import utils.Bounds
import utils.CardinalDirection.East
import utils.LongSolution
import utils.Point
import utils.PointAndDirection
import utils.TurnDirection.*
import utils.dijkstrasWithPreviousNodes
import utils.get

fun main() = Day16Solution().run()
class Day16Solution : LongSolution() {
    override fun part1(input: List<String>) = parseMaze(input).findLowestCostToEnd().toLong()
    override fun part2(input: List<String>) = parseMaze(input).countPointsOnLowestCostToEnd().toLong()
}

internal data class Maze(val start: PointAndDirection, val end: Point, val walls: Set<Point>, val bounds: Bounds) {
    fun findLowestCostToEnd(): Int {
        return findBestPathsToEnd().first
            .filter { it.key.point == end }
            .values
            .min()
    }

    fun countPointsOnLowestCostToEnd(): Int {
        val (costs, previousNodes) = findBestPathsToEnd()
        val pointsOnPath = mutableSetOf<Point>()

        //you can enter the final point from multiple directions and one might cost more
        val lowestCostEndings = costs.entries
            .filter { it.key.point == end }
            .groupBy({ entry -> entry.value }, {entry -> entry.key})
            .minBy { it.key }
            .value
            .toSet()


        var generation = lowestCostEndings
        while (generation.isNotEmpty()) {
            pointsOnPath += generation.map { it.point }
            generation = generation
                .mapNotNull { previousNodes[it] }
                .flatMap { it }
                .toSet()
        }

        return pointsOnPath.size
    }

    internal fun findBestPathsToEnd(): Pair<Map<PointAndDirection, Int>, Map<PointAndDirection, Set<PointAndDirection>>> {
        fun findNeighbours(current: PointAndDirection): Collection<Pair<PointAndDirection, Int>> {
            val result = mutableListOf(
                current.copy(direction = current.direction.turn(Left)) to 1000,
                current.copy(direction = current.direction.turn(Right)) to 1000,
            )

            val forward = current.point.move(current.direction)
            if (forward in bounds && forward !in walls) {
                result += ((forward facing current.direction) to 1)
            }

            return result
        }

        return dijkstrasWithPreviousNodes(start, neighboursMapping = { findNeighbours(it) })
    }
}

internal fun parseMaze(input: List<String>): Maze {
    val bounds = Bounds(input)
    var start: PointAndDirection? = null
    var end: Point? = null
    val walls = mutableSetOf<Point>()

    bounds.forEach {
        when(input[it]) {
            'S' -> start = it facing East
            'E' -> end = it
            '#' -> walls += it
        }
    }

    return Maze(start!!, end!!, walls, bounds)
}