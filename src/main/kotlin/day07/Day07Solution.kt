package day07

import utils.LongSolution

fun main() = Day07Solution().run()
class Day07Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        return parseInput(input)
            .filter { it.couldBeValid() }
            .sumOf { it.testValue }
    }

    override fun part2(input: List<String>): Long {
        return parseInput(input)
            .filter { it.couldBeValid(PART2_OPERATORS) }
            .sumOf { it.testValue }
    }
}

internal fun concatenate(left: Long, right: Long): Long {
    return  "${left}${right}".toLong()
}

//region operator definitions

typealias OperatorsList = List<(Long, Long) -> Long>
private val PART1_OPERATORS : OperatorsList = listOf(Long::plus, Long::times)
internal val PART2_OPERATORS = PART1_OPERATORS + ::concatenate

//endregion

internal data class CalibrationEquation(val testValue: Long, val inputs: List<Long>) {
    fun couldBeValid(operators: OperatorsList = PART1_OPERATORS): Boolean {
        var lastRoundOfResults = listOf(inputs.first())
        var tail = inputs.drop(1)
        while (tail.isNotEmpty()) {
            val currentValue = tail.first()
            tail = tail.drop(1)
            val nextRoundOfResults = operators.flatMap { operator ->
                lastRoundOfResults
                    .map { operator.invoke(it, currentValue) }
                    .filter { it <= testValue  }
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