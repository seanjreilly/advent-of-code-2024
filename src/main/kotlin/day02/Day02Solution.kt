package day02

import utils.IntSolution

fun main() = Day02Solution().run()
class Day02Solution : IntSolution() {
    override fun part1(input: List<String>) = input.map { parseLine(it) }.count { isSafe(it) }

    override fun part2(input: List<String>) = input.map { parseLine(it) }.count { isSafeWithDampening(it) }
}

internal fun parseLine(line: String) = line.split(' ').map(String::toInt)
internal fun isSafe(levels: List<Int>): Boolean {
    val allowedRange = 1..3
    return when (levels[1] > levels[0]) {
        true -> levels.windowed(2).all { (first, second) -> second - first in allowedRange }
        false -> levels.windowed(2).all { (first, second) -> first - second in allowedRange }
    }
}
internal fun isSafeWithDampening(levels: List<Int>): Boolean {
    val indexedLevels = levels.withIndex()
    return (0 until levels.size).any { i ->
        val dampenedLevels = indexedLevels.filter { it.index != i  }.map { it.value }
        isSafe(dampenedLevels)
    }
}