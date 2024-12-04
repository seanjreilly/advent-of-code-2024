package day04

import utils.*

fun main() = Day04Solution().run()
class Day04Solution : IntSolution() {
    override fun part1(input: List<String>) = countXmasInstances(input)

    override fun part2(input: List<String>) = countMasCrossInstances(input)
}

internal fun countXmasInstances(input: List<String>): Int {
    val bounds = Bounds(input)

    fun moveNext(point: Point, dir: Direction) : Triple<Char, Point, Direction>? {
        val newPoint = point.move(dir)
        if (newPoint !in bounds) {
            return null
        }
        val newValue = input[newPoint]
        return Triple(newValue, newPoint, dir)
    }

    return bounds
        .filter { input[it] == 'X' }
        .flatMap { start -> Direction.entries.map { dir -> Triple('X', start, dir) } }
        .mapNotNull { (_, point, dir) -> moveNext(point, dir) }
        .filter { (char, _, _) -> char == 'M' }
        .mapNotNull { (_, point, dir) -> moveNext(point, dir) }
        .filter { (char, _, _) -> char == 'A' }
        .mapNotNull { (_, point, dir) -> moveNext(point, dir) }
        .count { (char, _, _) -> char == 'S' }
}

internal fun countMasCrossInstances(input: List<String>): Int {
    val gridData = input.map { it.toCharArray().toTypedArray() }.toTypedArray()
    val bounds = Bounds(input).shrink() //'A' characters at the edge of the grid cannot be in a cross

    return bounds
        .filter { input[it] == 'A' }
        .count { detectDiagonalCross(gridData, it) }
}

internal fun detectDiagonalCross(grid: Array<Array<Char>>, centerPoint: Point): Boolean {
    return DiagonalDirection.entries
        .flatMap { listOf(it, it.opposite()) }
        .map { dir -> dir.moveOperation(centerPoint) }
        .map { point -> grid[point] }
        .windowed(2, step = 2)
        .count { chars -> chars == listOf('M', 'S') } == 2
}