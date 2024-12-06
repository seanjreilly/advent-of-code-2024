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
        val bounds = Bounds(input)
        var guard = findStart(input)
        val pointsVisited = mutableSetOf<Point>()
        do {
            pointsVisited += guard.point
            guard = moveNext(guard, input)
        } while (guard.point in bounds)
        return pointsVisited.size
    }

    override fun part2(input: List<String>) : Int {
        val bounds = Bounds(input)
        return bounds
            .filter { input[it] == '.' }
            .map { potentialObstruction -> addObstruction(input, potentialObstruction) }
            .count { gridWithNewObstruction -> detectLoop(gridWithNewObstruction) }
    }
}

internal fun detectLoop(grid: List<String>): Boolean {
    val bounds = Bounds(grid)
    var guard = findStart(grid)
    val previousPositions = mutableSetOf(guard)
    do {
        guard = moveNext(guard, grid)
        if (guard in previousPositions) {
            return true
        }
        previousPositions += guard
    } while (guard.point in bounds)
    return false
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
