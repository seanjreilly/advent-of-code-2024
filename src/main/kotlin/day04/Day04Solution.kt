package day04

import utils.Direction
import utils.GridMap
import utils.LongSolution
import utils.Point
import kotlin.collections.filter

fun main() = Day04Solution().run()
class Day04Solution : LongSolution() {
    override fun part1(input: List<String>) = countXmasInstances(input).toLong()

    override fun part2(input: List<String>) = 0L
}

internal fun countXmasInstances(input: List<String>): Int {
    val gridData = input.map { it.toCharArray().toTypedArray() }.toTypedArray()
    val map = WordSearch(gridData)

    fun moveNext(point: Point, dir: Direction) : Triple<Char, Point, Direction>? {
        val newPoint = point.move(dir)
        if (newPoint !in map) {
            return null
        }
        val newValue = map[newPoint]
        return Triple(newValue, newPoint, dir)
    }

    val startingPoints = map.filter { map[it] == 'X' }

    return startingPoints
        .flatMap { start -> Direction.entries.map { dir -> Triple('X', start, dir) } }
        .mapNotNull { (_, point, dir) -> moveNext(point, dir) }
        .filter { (char, _, _) -> char == 'M' }
        .mapNotNull { (_, point, dir) -> moveNext(point, dir) }
        .filter { (char, _, _) -> char == 'A' }
        .mapNotNull { (_, point, dir) -> moveNext(point, dir) }
        .count { (char, _, _) -> char == 'S' }
}

private class WordSearch(gridData: Array<Array<Char>>) : GridMap<Char>(
    gridData, getNeighboursMethod = Point::getCardinalAndDiagonalNeighbours
)