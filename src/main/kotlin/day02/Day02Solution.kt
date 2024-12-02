package day02

import utils.IntSolution

fun main() = Day02Solution().run()
class Day02Solution : IntSolution() {
    override fun part1(input: List<String>) = input.map { parseLine(it) }.count { isSafe(it) }

    override fun part2(input: List<String>) = 0
}

internal fun parseLine(line: String) = line.split(' ').map(String::toInt)
internal fun isSafe(levels: List<Int>): Boolean {
    val allowedRange = 1..3
    return when (levels[1] > levels[0]) {
        true -> levels.windowed(2).all { (first, second) -> second - first in allowedRange }
        false -> levels.windowed(2).all { (first, second) -> first - second in allowedRange }
    }
}