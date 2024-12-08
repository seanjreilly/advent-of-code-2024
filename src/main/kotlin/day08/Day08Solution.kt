package day08

import utils.Bounds
import utils.LongSolution
import utils.Point
import utils.get

fun main() = Day08Solution().run()
class Day08Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        val bounds = Bounds(input)
        return parseAntennaLocations(input).values
            .flatMap { antennaLocations -> findAntinodes(antennaLocations, bounds) }
            .toSet().size.toLong()
    }

    override fun part2(input: List<String>) = 0L
}

internal fun findAntinodes(points: Set<Point>, bounds: Bounds): Set<Point> {
    return points
        .generateTwoElementPairs()
        .flatMap { (a,b) -> findAntinodesBetween(a, b, bounds) }
        .toSet()
}

internal fun findAntinodesBetween(a: Point, b: Point, bounds: Bounds): Set<Point> {
    val xDifference = a.x - b.x
    val yDifference = a.y - b.y

    val results = listOf(
        Point(a.x + xDifference, a.y + yDifference),
        Point(a.x - xDifference, a.y - yDifference),
        Point(b.x + xDifference, b.y + yDifference),
        Point(b.x - xDifference, b.y - yDifference)
    )

    return results
        .filter { it != a }
        .filter { it != b }
        .filter { it in bounds }
        .toSet()
}

internal fun <T> Set<T>.generateTwoElementPairs(): Sequence<Pair<T, T>> {
    var values = this.toList()
    return sequence {
        while (values.isNotEmpty()) {
            val first = values.first()
            values = values.drop(1)
            yieldAll(values.map { first to it })
        }
    }
}

internal typealias AntennaMap = Map<Char, Set<Point>>

internal fun parseAntennaLocations(input: List<String>): AntennaMap {
    val bounds = Bounds(input)
    return bounds
        .map { point -> point to input[point] }
        .filter { (_, char) -> char.isLetterOrDigit() }
        .groupBy({ (_, char) -> char }, { (point, _) -> point })
        .mapValues { entry -> entry.value.toSet() }
}