package day10

import utils.GridMap
import utils.IntSolution
import utils.Point
import utils.toGrid

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
    constructor(input: List<String>) : this(input.toGrid(Char::digitToInt))

    override fun getNeighbours(point: Point): Collection<Point> {
        val currentHeight = this[point]
        return super.getNeighbours(point).filter {  this[it] == currentHeight + 1 }
    }

    internal fun findTrailheads() = this.filter { this[it] == 0 }

    fun countReachableNines(trailhead: Point): Int {
        return findUniquePathsToHeight9(trailhead)
            .map { it.first() }
            .distinct().size
    }

    /**
     * Find the rating: the number of distinct routes to a point of height 9 from this point
     */
    fun getRating(trailhead: Point) = findUniquePathsToHeight9(trailhead).size

    /**
     * Finds every distinct route to a point of height 9 from this point
     */
    private fun findUniquePathsToHeight9(point: Point): List<List<Point>> {
        return when (this[point]) {
            9 -> listOf(listOf(point))
            else -> {
                getNeighbours(point).flatMap { findUniquePathsToHeight9(it).map { result -> result + point } }
            }
        }
    }
}