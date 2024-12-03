package day03

import utils.IntSolution

fun main() = Day03Solution().run()
class Day03Solution : IntSolution() {
    override fun part1(input: List<String>): Int {
        val joinedLines = input.joinToString("")
        return findMulInstructions(joinedLines).sumOf { (x, y) -> x * y }
    }

    override fun part2(input: List<String>): Int {
        val joinedLines = input.joinToString("")
        return findMulInstructions(removeDisabledMulInstructions(joinedLines)).sumOf { (x, y) -> x * y }
    }
}

private val PARSE_INSTRUCTION_REGEX = """mul\((\d+),(\d+)\)""".toRegex()
internal fun findMulInstructions(line: String): List<Pair<Int, Int>> {
    return PARSE_INSTRUCTION_REGEX
        .findAll(line)
        .map { it.destructured.let { (x,y) -> x.toInt() to y.toInt()  } }
        .toList()
}

internal fun removeDisabledMulInstructions(input: String): String {
    return input
        .split("do()")
        .joinToString("") { it.split("don't()").first() }
}