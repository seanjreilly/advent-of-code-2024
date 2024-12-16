package day16

import utils.*
import utils.CardinalDirection.East
import utils.DijkstrasAlgorithm.Result
import utils.TurnDirection.Left
import utils.TurnDirection.Right

fun main() = Day16Solution().run()
class Day16Solution : IntSolution() {
    override fun part1(input: List<String>) = parseMaze(input).findLowestCostToEnd()
    override fun part2(input: List<String>) = parseMaze(input).countPointsOnLowestCostToEnd()
}

internal data class Maze(val start: PointAndDirection, val end: Point, val walls: Set<Point>) {
    fun findLowestCostToEnd(): Int {
        return findBestPathsToEnd().costs
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

    internal fun findBestPathsToEnd(): Result<PointAndDirection> {
        fun findNeighbours(current: PointAndDirection): Collection<Pair<PointAndDirection, Int>> {
            val result = mutableListOf(
                current.copy(direction = current.direction.turn(Left)) to 1000,
                current.copy(direction = current.direction.turn(Right)) to 1000,
            )

            val forward = current.move()
            if (forward.point !in walls) { //no need for a bounds check, there are walls all around
                result += (forward to 1)
            }

            return result
        }

        val dijkstras = DijkstrasAlgorithm<PointAndDirection> { findNeighbours(it) }
        return dijkstras.search(start)
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

    return Maze(start!!, end!!, walls)
}