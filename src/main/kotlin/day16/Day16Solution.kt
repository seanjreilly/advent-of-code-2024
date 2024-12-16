package day16

import utils.Bounds
import utils.CardinalDirection.East
import utils.LongSolution
import utils.Point
import utils.PointAndDirection
import utils.TurnDirection.*
import utils.dijkstras
import utils.get

fun main() = Day16Solution().run()
class Day16Solution : LongSolution() {
    override fun part1(input: List<String>) = parseMaze(input).findLowestCostToEnd().toLong()
    override fun part2(input: List<String>) = 0L
}

internal data class Maze(val start: PointAndDirection, val end: Point, val walls: Set<Point>, val bounds: Bounds) {
    fun findLowestCostToEnd(): Int {

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

        return dijkstras(start, neighboursMapping = { findNeighbours(it) })
            .filter { entry -> entry.key.point == end }
            .values
            .min()
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