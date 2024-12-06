package day06

import utils.Bounds
import utils.CardinalDirection.North
import utils.IntSolution
import utils.Point
import utils.PointAndDirection
import utils.TurnDirection
import utils.get

fun main() = Day06Solution().run()
class Day06Solution : IntSolution() {
    override fun part1(input: List<String>): Int {
        val (exitPath, _) = findExitPath(input)
        return exitPath.map { it.point }.toSet().size
    }

    override fun part2(input: List<String>) : Int {
        val (originalPath, bounds) = findExitPath(input)

        // when looking for cycles, it's fine to start at the point in the original path
        // just before the obstruction is first reached. A cycle will either include this
        // position or happen entirely after it, and if the path terminates, it will happen after this
        fun findStartPoint(potentialObstruction: Point) : PointAndDirection {
            val pathIndexOfObstruction = originalPath.withIndex()
                .first { (_, pointAndDirection) -> pointAndDirection.point == potentialObstruction }
                .index
            return originalPath[pathIndexOfObstruction-1]
        }

        //only consider points on the original path — adding an obstruction elsewhere cannot create a loop
        return originalPath
            .map { it.point }.toSet()
            .parallelStream()
            .filter { input[it] == '.' }
            .map { potentialObstruction -> potentialObstruction to addObstruction(input, potentialObstruction) }
            .filter { (potentialObstruction, gridWithNewObstruction) -> detectLoop(gridWithNewObstruction, bounds, findStartPoint(potentialObstruction)) }
            .count()
            .toInt()
    }
}

internal fun detectLoop(grid: List<String>, bounds: Bounds, start: PointAndDirection): Boolean {
    val previousPositions = mutableSetOf(start)
    var guard = start
    do {
        guard = moveNext(guard, grid)
        if (guard in previousPositions) {
            return true
        }
        previousPositions += guard
    } while (guard.point in bounds)
    return false
}

private fun findExitPath(input: List<String>): Pair<List<PointAndDirection>, Bounds> {
    val bounds = Bounds(input)
    val start = findStart(input)

    var guard = start
    val path = mutableListOf<PointAndDirection>()
    do {
        path += guard
        guard = moveNext(guard, input)
    } while (guard.point in bounds)
    return path to bounds
}

internal fun moveNext(pointAndDirection: PointAndDirection, grid: List<String>): PointAndDirection {
    val (point, direction) = pointAndDirection
    val bounds = Bounds(grid)

    val newPoint = point.move(direction)
    if (newPoint !in bounds || grid[newPoint] != '#') {
        return PointAndDirection(newPoint, direction)
    }
    return PointAndDirection(point, direction.turn(TurnDirection.Right))
}

internal fun addObstruction(grid: List<String>, point: Point): List<String> {
    return grid.mapIndexed { index, line ->
        when (index) {
            point.y -> line.replaceRange(point.x..point.x, "#")
            else -> line
        }
    }
}

internal fun findStart(input: List<String>): PointAndDirection {
    val bounds = Bounds(input)
    return PointAndDirection(bounds.first { input[it] == '^' }, North)
}
