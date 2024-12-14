package day14

import utils.Bounds
import utils.LongSolution
import utils.Point
import kotlin.math.sign

internal val PROD_BOUNDS = Bounds(0 ..101, 0..103)

fun main() = Day14Solution().run()
class Day14Solution(val bounds: Bounds = PROD_BOUNDS) : LongSolution() {
    override fun part1(input: List<String>): Long {
        val robots = parseRobots(input)
        repeat(100) { robots.forEach { robot -> robot.move(bounds)} }
        return robots.safetyFactor(bounds).toLong()
    }

    override fun part2(input: List<String>) = 0L
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