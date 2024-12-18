package day18

import utils.Bounds
import utils.DijkstrasAlgorithm
import utils.LongSolution
import utils.Point

internal val PROD_BOUNDS = Bounds(0 ..70, 0..70)
internal const val PROD_PART1_FALLEN_BLOCKS = 1024

fun main() = Day18Solution(PROD_BOUNDS, PROD_PART1_FALLEN_BLOCKS).run()
class Day18Solution(val bounds: Bounds, val part1FallenBlocks: Int) : LongSolution() {
    override fun part1(input: List<String>): Long {
        return findShortestPathToExit(bounds, parseFallingBytes(input), part1FallenBlocks).toLong()
    }

    override fun part2(input: List<String>) = 0L
}

internal fun findShortestPathToExit(bounds: Bounds, fallingBytes: List<Point>, bytesFallenSoFar: Int): Int {
    val corruptedLocations:Set<Point> = fallingBytes.take(bytesFallenSoFar).toSet()
    val endPoint = getExitPoint(bounds)

    val dijkstras = DijkstrasAlgorithm<Point> { point ->
        point.getCardinalNeighbours()
            .filter { it in bounds  }
            .filter { it !in corruptedLocations  }
            .map { it to 1 }
    }

    val result = dijkstras.search(Point(0,0))
    return result.costs[endPoint]!!
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