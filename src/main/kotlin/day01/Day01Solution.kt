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

    override fun part2(input: List<String>): Int {
        val (firstList, secondList) = parseLines(input)
        val occurrences = countOccurrences(secondList)
        return firstList.sumOf { it * (occurrences[it] ?: 0) }
    }
}

internal fun absoluteDifference(a: Int, b: Int) = abs(a - b)
internal fun countOccurrences(list: List<Any>) = list.groupingBy { it }.eachCount()
internal fun parseLine(line: String) = line.split(' ').filter { it.isNotEmpty() }.map { it.toInt() }

internal fun parseLines(lines: List<String>): Pair<List<Int>, List<Int>> {
    val result = lines
        .flatMap { parseLine(it).withIndex() }
        .groupBy({ it.index }, { it.value }) //index will be zero or 1 because of where we added it
    return result[0]!! to result[1]!!
}