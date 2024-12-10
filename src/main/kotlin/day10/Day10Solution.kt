package day10

import utils.GridMap
import utils.LongSolution
import utils.Point
import utils.djikstras

fun main() = Day10Solution().run()
class Day10Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        val map = TopographicMap(input)
        return map.findTrailheads()
            .map { map.countReachableNines(it) }
            .sumOf { it.toLong() }
    }

    override fun part2(input: List<String>) = 0L
}

internal class TopographicMap(data : Array<Array<Int>>) : GridMap<Int>(data, Point::getCardinalNeighbours) {


    override fun getNeighbours(point: Point): Collection<Point> {
        val currentHeight = this[point]
        return super.getNeighbours(point)
            .map { point -> point to this[point] }
            .filter { (_, newHeight) -> newHeight == currentHeight + 1 }
            .map { (point, _) -> point }
    }

    internal fun findTrailheads(): Collection<Point> {
        return this.filter { this[it] == 0 }
    }

    fun countReachableNines(trailhead: Point) : Int {
        val reachabilityMap = djikstras(trailhead) { point -> getNeighbours(point).map { it to 1 } }
        //items with an optimum path of length 9 should be nines
        return reachabilityMap
            .filterValues { it == 9 }
            .filterKeys { point -> this[point] == 9 } //belt and braces
            .count()
    }

    companion object {
        internal operator fun invoke(input: List<String>): TopographicMap {
            val data = input
                .map { line -> line.map { it.digitToInt() }.toTypedArray() }
                .toTypedArray()
            return TopographicMap(data)
        }
    }
}