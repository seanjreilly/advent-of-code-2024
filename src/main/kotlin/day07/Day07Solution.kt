package day07

import utils.LongSolution

fun main() = Day07Solution().run()
class Day07Solution : LongSolution() {
    override fun part1(input: List<String>) = solve(input, PART1_OPERATORS)
    override fun part2(input: List<String>) = solve(input, PART2_OPERATORS)
}

private fun solve(input: List<String>, operators: OperatorsList): Long {
    return parseInput(input)
        .parallelStream()
        .filter { it.couldBeValid(operators) }
        .mapToLong { it.testValue }
        .sum()
}

internal fun concatenate(left: Long, right: Long): Long {
    return (left * tenToThePowerOf(right.numberOfDecimalDigits())) + right
}

internal fun Long.numberOfDecimalDigits() : Int {
    var result = 1
    var remaining = this
    while (remaining > 9L) {
        result++
        remaining = remaining / 10
    }
    return result
}

internal fun tenToThePowerOf(n: Int) : Long {
    var result = 1L
    repeat(n) { result *= 10 }
    return result
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