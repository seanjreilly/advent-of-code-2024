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

    override fun part2(input: List<String>) = 0
}

internal fun findStart(input: List<String>): PointAndDirection {
    val bounds = Bounds(input)
    return PointAndDirection(bounds.first { input[it] == '^' }, North)
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