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
        return findMulInstructions(findEnabledMulInstructions(joinedLines)).sumOf { (x, y) -> x * y }
    }
}

private val PARSE_INSTRUCTION_REGEX = """mul\((\d+),(\d+)\)""".toRegex()
internal fun findMulInstructions(line: String): List<Pair<Int, Int>> {
    return PARSE_INSTRUCTION_REGEX
        .findAll(line)
        .map { match ->
            val groups = match.groups
            groups[1]!!.value.toInt() to groups[2]!!.value.toInt()
        }
        .toList()
}

val DO_INSTRUCTION_REGEX = """do\(\)""".toRegex()
internal fun findEnabledMulInstructions(input: String): String {
    return DO_INSTRUCTION_REGEX.splitToSequence(input)
        .joinToString("") {
            val index = it.indexOf("don't()")
            when (index) {
                -1 -> it
                else -> it.substring(0 until index)
            }
        }
}