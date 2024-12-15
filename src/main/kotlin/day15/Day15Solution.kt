package day15

import utils.CardinalDirection
import utils.CardinalDirection.East
import utils.CardinalDirection.North
import utils.CardinalDirection.South
import utils.CardinalDirection.West
import utils.IntSolution
import utils.Point
import kotlin.collections.minus
import kotlin.collections.plus
import kotlin.collections.plusAssign

fun main() = Day15Solution().run()
class Day15Solution : IntSolution() {
    override fun part1(input: List<String>): Int {
        val originalWarehouse = Warehouse(input)
        return parseMoves(input)
            .fold(originalWarehouse) { warehouse, direction -> warehouse.moveRobot(direction) }
            .totalScore()
    }

    override fun part2(input: List<String>) : Int {
        val originalWarehouse = Warehouse2(input.transformWarehouseLayout())
        return parseMoves(input)
            .fold(originalWarehouse) { warehouse, direction -> warehouse.moveRobot(direction) }
            .totalScore()
    }
}

internal data class Warehouse(val robotLocation: Point, val boxLocations: Set<Point>, val wallLocations: Set<Point>) {
    fun moveRobot(direction: CardinalDirection) : Warehouse {
        val potentialNewRobotLocation = robotLocation.move(direction)
        var newBoxLocations = boxLocations
        if (potentialNewRobotLocation in wallLocations) {
            return this // can't move
        }
        if (potentialNewRobotLocation in boxLocations) {
            val oldBoxLocation = potentialNewRobotLocation
            var newBoxLocation = potentialNewRobotLocation
            while (newBoxLocation in boxLocations) {
                newBoxLocation = newBoxLocation.move(direction)
            }
            if (newBoxLocation in wallLocations) {
                return this // can't move
            }
            newBoxLocations -= oldBoxLocation
            newBoxLocations += newBoxLocation
        }

        return this.copy(robotLocation = potentialNewRobotLocation, boxLocations = newBoxLocations)
    }

    fun totalScore(): Int {
        return boxLocations.sumOf { it.score() }
    }

    companion object {
        internal operator fun invoke(input:List<String>) : Warehouse {
            val rawWarehouseLayout = input.takeWhile { it.isNotEmpty() }

            var robotLocation : Point? = null
            val boxLocations = mutableSetOf<Point>()
            val wallLocations = mutableSetOf<Point>()

            rawWarehouseLayout.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    val point = Point(x, y)
                    when (char) {
                        '@' -> robotLocation = point
                        'O' -> boxLocations += point
                        '#' -> wallLocations += point
                    }
                }
            }

            return Warehouse(robotLocation!!, boxLocations, wallLocations)
        }
    }
}

internal data class Warehouse2(val robotLocation: Point, val boxLeftSideLocations: Set<Point>, val wallLocations: Set<Point>) {
    val boxRightSideLocations = boxLeftSideLocations.map { it.copy(it.x + 1) }.toSet()

    fun moveRobot(direction: CardinalDirection) : Warehouse2 {
        val potentialNewRobotLocation = robotLocation.move(direction)
        var nextRoundLeftSideLocations = boxLeftSideLocations
        if (potentialNewRobotLocation in wallLocations) {
            return this // can't move
        }

        if (direction == East) {
                val visitedLeftSides = mutableSetOf<Point>()
                var location = potentialNewRobotLocation
                while (location in boxLeftSideLocations) {
                    visitedLeftSides += location
                    location = location.copy(x = location.x + 2) //x+1 is a box right side
                    if (location in wallLocations) {
                        return this // can't move
                    }
                }

                val leftSidesToRemove = visitedLeftSides
                val leftSidesToAdd = visitedLeftSides.map { it.east() }
                nextRoundLeftSideLocations = (boxLeftSideLocations - leftSidesToRemove) + leftSidesToAdd
        }

        if (direction == West) {
                val visitedRightSides = mutableSetOf<Point>()
                var location = potentialNewRobotLocation
                while (location in boxRightSideLocations) {
                    visitedRightSides += location
                    location = location.copy(x = location.x - 2) //x-1 a box left side
                    if (location in wallLocations) {
                        return this // can't move
                    }
                }

                val leftSidesToRemove = visitedRightSides.map { it.west() }
                val leftSidesToAdd = visitedRightSides.map { it.copy(x= it.x -2) }
                nextRoundLeftSideLocations = (boxLeftSideLocations - leftSidesToRemove) + leftSidesToAdd
        }

        if (direction == North || direction == South) {
            nextRoundLeftSideLocations = pushBoxesVertically(potentialNewRobotLocation, direction) ?: return this //can't move
        }

        return this.copy(
            robotLocation = potentialNewRobotLocation,
            boxLeftSideLocations = nextRoundLeftSideLocations
        )
    }

    fun totalScore(): Int {
        return boxLeftSideLocations.sumOf { it.score() }
    }

    private fun pushBoxesVertically(newRobotLocation: Point, direction: CardinalDirection) : Set<Point>? {
        if (newRobotLocation in boxLeftSideLocations || newRobotLocation in boxRightSideLocations) {
            val visitedPoints = mutableSetOf(newRobotLocation)
            visitedPoints += if (newRobotLocation in boxRightSideLocations) {
                newRobotLocation.west()
            } else {
                newRobotLocation.east()
            }

            var generation: Set<Point> = visitedPoints
            while (generation.isNotEmpty()) {
                visitedPoints += generation
                generation = generation
                    .mapNotNull { findNeighbours(it, direction) }
                    .flatMap { pair -> pair.toList() }
                    .toSet()
            }

            val leftSidesToRemove = visitedPoints.filter { it in boxLeftSideLocations }
            val leftSidesToAdd = leftSidesToRemove.map { it.move(direction) }
            val rightSidesToAdd = visitedPoints.filter { it in boxRightSideLocations }.map { it.move(direction) }
            val newPoints = leftSidesToAdd + rightSidesToAdd
            if (newPoints.any { it in wallLocations }) {
                return null // can't move
            }

            return (boxLeftSideLocations - leftSidesToRemove) + leftSidesToAdd
        }
        return boxLeftSideLocations //didn't run into a box
    }

    private fun findNeighbours(point: Point, direction: CardinalDirection) : Pair<Point, Point>? {
        val neighbour = point.move(direction)
        return when (neighbour) {
            in boxLeftSideLocations -> neighbour to neighbour.east()
            in boxRightSideLocations -> neighbour to neighbour.west()
            else -> null
        }
    }

    companion object {
        internal operator fun invoke(input:List<String>) : Warehouse2 {
            val rawWarehouseLayout = input

            var robotLocation : Point? = null
            val boxLeftSideLocations = mutableSetOf<Point>()
            val wallLocations = mutableSetOf<Point>()

            rawWarehouseLayout.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    val point = Point(x, y)
                    when (char) {
                        '@' -> robotLocation = point
                        '[' -> boxLeftSideLocations += point
                        '#' -> wallLocations += point
                    }
                }
            }

            return Warehouse2(robotLocation!!, boxLeftSideLocations, wallLocations)
        }
    }
}

internal fun Point.score() = (100 * y) + x

internal fun parseMoves(input: List<String>): List<CardinalDirection> {
    return input
        .dropWhile { it.isNotEmpty() } //warehouse layout
        .drop(1) //blank line
        .flatMap { line -> line.map { parseMove(it) } }
}

internal fun parseMove(ch: Char): CardinalDirection {
    return when(ch) {
        '^' -> North
        '>' -> East
        'v' -> South
        '<' -> West
        else -> throw IllegalArgumentException("Unexpected move symbol '$ch'")
    }
}

internal fun List<String>.transformWarehouseLayout() : List<String> {
    return this
        .takeWhile { it.isNotEmpty() }
        .map { line ->
            line.map { ch ->
                when (ch) {
                    '#' -> "##"
                    '@' -> "@."
                    'O' -> "[]"
                    '.' -> ".."
                    else -> throw IllegalArgumentException("unexpected character '$ch'")
                }
            }
                .joinToString("")
        }
}