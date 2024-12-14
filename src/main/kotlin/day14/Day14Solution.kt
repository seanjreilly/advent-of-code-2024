package day14

import utils.Bounds
import utils.IntSolution
import utils.Point
import kotlin.math.sign

internal val PROD_BOUNDS = Bounds(0 ..101, 0..103)

fun main() = Day14Solution().run()
class Day14Solution(val bounds: Bounds = PROD_BOUNDS) : IntSolution() {
    override fun part1(input: List<String>): Int {
        val robots = parseRobots(input)
        repeat(100) { robots.forEach { robot -> robot.move(bounds)} }
        return robots.safetyFactor(bounds)
    }

    override fun part2(input: List<String>) : Int {
        val robots = parseRobots(input)

        fun findPositions() = robots.map { it.position }.toSet()
        fun findScore(positions: Set<Point>) = findAdjacentRobots(positions).maxOf { it.size }

        var bestSeconds = 0
        var bestPositions = findPositions()
        var bestScore = findScore(bestPositions)

        repeat(7500) { i ->
            robots.forEach { robot -> robot.move(bounds)}
            val positions = findPositions()
            val score = findScore(positions)

            if (score > bestScore) {
                bestSeconds = i + 1
                bestPositions = positions
                bestScore = score
            }
        }

        println("best candidate is after ${bestSeconds} seconds, with ${bestScore} adjacent positions")
        println()
        println(printGrid(bestPositions, bounds))

        println()

        return bestSeconds
    }
}

internal fun List<Robot>.safetyFactor(bounds: Bounds): Int {
    return mapNotNull { it.quadrant(bounds) }
        .groupBy { it }
        .values
        .map { it.size }
        .reduce(Int::times)
}

internal data class Robot(var position: Point, val velocity: Velocity) {
    fun move(bounds: Bounds) {
        //mod behaves differently than %, and has the semantics we want
        var newX = (position.x + velocity.dX).mod(bounds.lastX)
        var newY = (position.y + velocity.dY).mod(bounds.lastY)
        position = Point(newX, newY)
    }

    fun quadrant(bounds: Bounds): Quadrant? {
        val xBoundary = bounds.lastX / 2
        val yBoundary = bounds.lastY / 2

        val x = position.x.compareTo(xBoundary).sign
        val y = position.y.compareTo(yBoundary).sign

        return when (x to y) {
            -1 to -1 -> Quadrant.TOP_LEFT
            1 to -1 -> Quadrant.TOP_RIGHT
            -1 to 1 -> Quadrant.BOTTOM_LEFT
            1 to 1 -> Quadrant.BOTTOM_RIGHT
            else -> null
        }
    }

    companion object {
        private val ROBOT_REGEX = """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".toRegex()
        operator fun invoke(line: String) : Robot {
            val (x, y, dX, dY) = ROBOT_REGEX.matchEntire(line)!!.destructured.toList().map { it.toInt() }
            return Robot(Point(x,y), Velocity(dX, dY))
        }
    }
}

enum class Quadrant {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT
}

internal data class Velocity(val dX: Int, val dY: Int)

internal fun parseRobots(input: List<String>) = input.map(Robot::invoke)

internal data class AdjacentRobots(val size: Int)

internal fun findAdjacentRobots(robotPositions: Collection<Point>): Collection<AdjacentRobots> {
    val pointSet = robotPositions.toSet()
    val visitedPoints = mutableSetOf<Point>()

    fun exploreAdjacentRobots(firstPoint: Point) : AdjacentRobots {
        val pointsInRegion = mutableSetOf<Point>()
        var searchGeneration = setOf(firstPoint)

        while (searchGeneration.isNotEmpty()) {
            pointsInRegion += searchGeneration
            visitedPoints += searchGeneration
            searchGeneration = searchGeneration
                .flatMap { it.getCardinalNeighbours() }
                .filter { it !in visitedPoints } //already includes points in the region
                .filter { it in pointSet }
                .toSet()
        }
        return AdjacentRobots(pointsInRegion.size)
    }

    return robotPositions.mapNotNull {
        when (it in visitedPoints) {
            true -> null //we've already processed this plot
            false -> exploreAdjacentRobots(it) //new region
        }
    }
}

internal fun printGrid(points: Set<Point>, bounds: Bounds) : String {
    return bounds
        .validYCoordinates
        .joinToString("\n") { y ->
            bounds.validXCoordinates
                .map { x ->
                    when (Point(x, y)) {
                        in points -> '#'
                        else -> '.'
                    }
                }
                .toCharArray()
                .joinToString("")
        }
}