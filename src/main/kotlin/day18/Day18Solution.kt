package day18

import utils.Bounds
import utils.DijkstrasAlgorithm
import utils.Point
import utils.StringSolution

internal val PROD_BOUNDS = Bounds(0 ..70, 0..70)
internal const val PROD_PART1_FALLEN_BLOCKS = 1024

fun main() = Day18Solution(PROD_BOUNDS, PROD_PART1_FALLEN_BLOCKS).run()
class Day18Solution(val bounds: Bounds, val part1FallenBlocks: Int) : StringSolution() {
    override fun part1(input: List<String>): String {
        return findShortestPathToExit(bounds, parseFallingBytes(input), part1FallenBlocks).toString()
    }

    override fun part2(input: List<String>) : String {
        val blockingPoint = findFirstFallingByteThatBlocksPath(bounds, parseFallingBytes(input))
        return "${blockingPoint.x},${blockingPoint.y}"
    }
}

internal fun findFirstFallingByteThatBlocksPath(bounds: Bounds, fallingBytes: List<Point>): Point {
    var range = fallingBytes.indices
    while (range.start < range.endInclusive) {
        val midPoint = (range.start + range.endInclusive) / 2
        val reachable = isExitReachable(bounds, fallingBytes, midPoint + 1)
        range = when(reachable) {
            true -> (midPoint + 1)..range.last
            false -> range.start .. midPoint
        }
    }

    //verify solution
    check(isExitReachable(bounds, fallingBytes, range.start)) { "dropping one fewer bytes should not block the path" }
    check(!isExitReachable(bounds, fallingBytes, range.start + 1)) { "dropping this byte should block the path" }

    return fallingBytes[range.start]
}

internal fun isExitReachable(bounds: Bounds, fallingBytes: List<Point>, bytesFallenSoFar: Int): Boolean {
    return dijkstras(bounds, fallingBytes, bytesFallenSoFar).costs.containsKey(getExitPoint(bounds))
}

internal fun findShortestPathToExit(bounds: Bounds, fallingBytes: List<Point>, bytesFallenSoFar: Int): Int {
    return dijkstras(bounds, fallingBytes, bytesFallenSoFar).costs[getExitPoint(bounds)]!!
}

private fun dijkstras(
    bounds: Bounds,
    fallingBytes: List<Point>,
    bytesFallenSoFar: Int
): DijkstrasAlgorithm.Result<Point> {
    val corruptedLocations: Set<Point> = fallingBytes.take(bytesFallenSoFar).toSet()

    val dijkstras = DijkstrasAlgorithm<Point> { point ->
        point.getCardinalNeighbours()
            .filter { it in bounds }
            .filter { it !in corruptedLocations }
            .map { it to 1 }
    }

    return dijkstras.search(Point(0, 0))
}

internal fun parseFallingBytes(input: List<String>): List<Point> {
    return input.map(::parseFallingByte)
}

internal fun parseFallingByte(input: String): Point {
    val (x,y) = input.split(',').map(String::toInt)
    return Point(x,y)
}

internal fun getExitPoint(bounds: Bounds): Point {
    return Point(bounds.lastX, bounds.lastY)
}