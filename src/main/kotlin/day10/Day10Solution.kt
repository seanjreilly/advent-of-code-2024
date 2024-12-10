package day10

import utils.GridMap
import utils.IntSolution
import utils.Point
import utils.dijkstras

fun main() = Day10Solution().run()
class Day10Solution : IntSolution() {
    override fun part1(input: List<String>): Int {
        val map = TopographicMap(input)
        return map.findTrailheads()
            .sumOf { map.countReachableNines(it) }
    }

    override fun part2(input: List<String>): Int {
        val map = TopographicMap(input)
        return map.findTrailheads()
            .sumOf { map.getRating(it) }
    }
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
        val reachabilityMap = dijkstras(trailhead) { point -> getNeighbours(point).map { it to 1 } }
        //items with an optimum path of length 9 should be nines
        return reachabilityMap
            .filterValues { it == 9 }
            .filterKeys { point -> this[point] == 9 } //belt and braces
            .count()
    }

    /**
     * Find the rating: the number of distinct routes to a point of height 9 from this point
     */
    fun getRating(point: Point) : Int {
        return when (this[point]) {
            9 -> 1
            else -> getNeighbours(point).sumOf { getRating(it) }
        }
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