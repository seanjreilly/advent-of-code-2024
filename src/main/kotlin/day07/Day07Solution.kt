package day07

import utils.LongSolution

fun main() = Day07Solution().run()
class Day07Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        return parseInput(input)
            .filter { it.couldBeValid() }
            .sumOf { it.testValue }
    }

    override fun part2(input: List<String>) = 0L
}

private val OPERATORS : List<(Long, Long) -> Long> = listOf(Long::plus, Long::times)
internal data class CalibrationEquation(val testValue: Long, val inputs: List<Long>) {
    fun couldBeValid(): Boolean {
        var lastRoundOfResults = listOf(inputs.first())
        var tail = inputs.drop(1)
        while (tail.isNotEmpty()) {
            val currentValue = tail.first()
            tail = tail.drop(1)
            val nextRoundOfResults = OPERATORS.flatMap { operator ->
                val resultsToAdd: List<Long> = lastRoundOfResults.map { it: Long -> operator.invoke(it, currentValue) }
                resultsToAdd
            }
            lastRoundOfResults = nextRoundOfResults
        }
        return testValue in lastRoundOfResults
    }
    constructor(testValue: Int, vararg inputs: Int ) : this(testValue.toLong(), inputs.map { it.toLong() })

    companion object {
        operator fun invoke(line: String) : CalibrationEquation {
            val (rawTestValue, rawInputs) = line.split(": ", limit = 2)
            val inputs = rawInputs.split(' ').filter { it.isNotBlank() }.map(String::toLong)
            return CalibrationEquation(rawTestValue.toLong(), inputs)
        }
    }

}

internal fun parseInput(input: List<String>) = input.map { CalibrationEquation(it) }