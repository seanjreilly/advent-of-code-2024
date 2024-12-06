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
        return findPointsVisitedBeforeExiting(input).size
    }

    override fun part2(input: List<String>) : Int {
        //only calculate these once
        val bounds = Bounds(input)
        var start = findStart(input)

        //adding an obstruction to a point the guard doesn't visit cannot create a loop
        return findPointsVisitedBeforeExiting(input)
            .parallelStream()
            .filter { input[it] == '.' }
            .map { potentialObstruction -> addObstruction(input, potentialObstruction) }
            .filter { gridWithNewObstruction -> detectLoop(gridWithNewObstruction, bounds, start) }
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

private fun findPointsVisitedBeforeExiting(input: List<String>): MutableSet<Point> {
    val bounds = Bounds(input)
    var guard = findStart(input)
    val pointsVisited = mutableSetOf<Point>()
    do {
        pointsVisited += guard.point
        guard = moveNext(guard, input)
    } while (guard.point in bounds)
    return pointsVisited
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
