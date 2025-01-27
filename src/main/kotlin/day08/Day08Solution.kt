package day08

import utils.Bounds
import utils.IntSolution
import utils.Point
import utils.get
import utils.twoElementCombinations

fun main() = Day08Solution().run()
class Day08Solution : IntSolution() {
    override fun part1(input: List<String>) = solve(input, ::findAntinodesBetween)
    override fun part2(input: List<String>) = solve(input, ::findAntinodesBetweenWithResonantHarmonics)
}

private fun solve(input: List<String>, operation: FindAntinodesOperation): Int {
    val bounds = Bounds(input)
    return parseAntennaLocations(input).values
        .flatMap { antennaLocations -> findAntinodes(antennaLocations, bounds, operation) }
        .distinct()
        .size
}

internal typealias AntennaMap = Map<Char, Set<Point>>
internal typealias FindAntinodesOperation = (Point, Point, Bounds) -> Set<Point>

internal fun findAntinodes(
    points: Set<Point>,
    bounds: Bounds,
    operation: FindAntinodesOperation = ::findAntinodesBetween
): Set<Point> {
    return points
        .twoElementCombinations()
        .flatMap { (a, b) -> operation(a, b, bounds) }
        .toSet()
}

internal fun findAntinodesBetween(a: Point, b: Point, bounds: Bounds): Set<Point> {
    val xDifference = a.x - b.x
    val yDifference = a.y - b.y

    return listOf(
        Point(a.x + xDifference, a.y + yDifference),
        Point(b.x - xDifference, b.y - yDifference)
    )
        .filter { it in bounds }
        .toSet()
}

internal fun findAntinodesBetweenWithResonantHarmonics(a: Point, b: Point, bounds: Bounds): Set<Point> {
    val xDifference = a.x - b.x
    val yDifference = a.y - b.y

    fun move(direction: Int) : Sequence<Point> {
        val xIncrement = xDifference * direction
        val yIncrement = yDifference * direction

        return sequence {
            var point = a
            do {
                yield(point)
                point = Point(point.x + xIncrement, point.y + yIncrement)
            } while (point in bounds)
        }
    }

    return (move(1) + move(-1)).toSet()
}

internal fun parseAntennaLocations(input: List<String>): AntennaMap {
    val bounds = Bounds(input)
    return bounds
        .map { point -> point to input[point] }
        .filter { (_, char) -> char.isLetterOrDigit() }
        .groupBy({ (_, char) -> char }, { (point, _) -> point })
        .mapValues { entry -> entry.value.toSet() }
}