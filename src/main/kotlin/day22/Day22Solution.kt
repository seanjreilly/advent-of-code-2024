package day22

import utils.LongSolution

fun main() = Day22Solution().run()
class Day22Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        return input
            .map { it.toLong() }
            .sumOf { nextSecretNumberAfterNRounds(it, 2000) }
    }

    override fun part2(input: List<String>) = 0L
}

internal fun nextSecretNumberAfterNRounds(secretNumber: Long, rounds: Int): Long {
    var currentValue = secretNumber
    var roundsRemaining = rounds
    do {
        currentValue = nextSecretNumber(currentValue)
        roundsRemaining--
    } while (roundsRemaining > 0)
    return currentValue
}

internal fun nextSecretNumber(secretNumber: Long): Long {
    var result = secretNumber
    result = prune(mix(result shl 6, result))
    result = prune(mix(result shr 5, result))
    result = prune(mix(result shl 11, result))
    return result
}

internal fun mix(numberToMix: Long, secretNumber: Long) = numberToMix xor secretNumber
internal fun prune(secretNumber: Long) = secretNumber and 16777215L