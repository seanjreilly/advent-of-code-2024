package day01

import utils.IntSolution
import kotlin.math.abs

fun main() = Day01Solution().run()
class Day01Solution : IntSolution() {
    override fun part1(input: List<String>): Int {
        val (firstList, secondList) = parseLines(input)
        return firstList.sorted()
            .zip(secondList.sorted()) { a, b -> absoluteDifference(a, b) }
            .sum()
    }

    override fun part2(input: List<String>) = 0
}

internal fun parseLine(line: String) : Pair<Int,Int> {
    val integers = line
        .split(' ', )
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
    return Pair(integers[0], integers[1])
}

internal fun absoluteDifference(a: Int, b: Int) = abs(a - b)

private fun parseLines(lines: List<String>): Pair<List<Int>, List<Int>> {
    val firstList = mutableListOf<Int>()
    val secondList = mutableListOf<Int>()
    lines.map { parseLine(it) }.forEach {
        firstList += it.first
        secondList += it.second
    }
    return firstList to secondList
}
