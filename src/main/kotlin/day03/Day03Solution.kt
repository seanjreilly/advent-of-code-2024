package day03

import utils.IntSolution

fun main() = Day03Solution().run()
class Day03Solution : IntSolution() {
    override fun part1(input: List<String>): Int {
        return findMulInstructions(input).sumOf { (x, y) -> x * y }
    }

    override fun part2(input: List<String>) = 0
}

private val PARSE_INSTRUCTION_REGEX = """mul\((\d+),(\d+)\)""".toRegex()

internal fun findMulInstructions(input: List<String>): Sequence<Pair<Int, Int>> = sequence {
    val line = input.joinToString("")
    PARSE_INSTRUCTION_REGEX.findAll(line).forEach { match ->
        val groups = match.groups
        yield(groups[1]!!.value.toInt() to groups[2]!!.value.toInt())
    }
}