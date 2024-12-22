package day22

import utils.LongSolution

fun main() = Day22Solution().run()
class Day22Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        return input
            .map { it.toLong() }
            .sumOf { nextSecretNumberAfterNRounds(it, 2000) }
    }

    override fun part2(input: List<String>) = findBestPriceChange(input.map { it.toLong() }).second
}

internal fun findBestPriceChange(secretNumbers: List<Long>): Pair<PriceChangePattern, Long> {
    return secretNumbers
        .parallelStream()
        .map { findChangesAndPrices(it, 2000) }
        .map(::removeDuplicates)
        .reduce(::merge)
        .get()
        .maxBy { it.value }
        .toPair()
}

internal fun merge(first: Map<PriceChangePattern, Long>, second: Map<PriceChangePattern, Long>): Map<PriceChangePattern, Long> {
    val result = first.toMutableMap()
    second.forEach { (key, value) -> result.merge(key, value, Long::plus) }
    return result
}

internal fun removeDuplicates(prices: List<Pair<PriceChangePattern, Long>>): Map<PriceChangePattern, Long> {
    return prices.asReversed().associate { it }
}

internal fun findChangesAndPrices(secretNumber: Long, rounds: Int): List<Pair<PriceChangePattern, Long>> {
    val prices = mutableListOf<Int>(secretNumber.mod(10))

    var currentSecretNumber = secretNumber
    var roundsRemaining = rounds
    do {
        currentSecretNumber = nextSecretNumber(currentSecretNumber)
        prices += currentSecretNumber.mod(10)
        roundsRemaining--
    } while (roundsRemaining > 0)

    fun convertWindow(window: List<Int>) : Pair<PriceChangePattern, Long> {
        val window_1 = window[1]
        val window_2 = window[2]
        val window_3 = window[3]
        val window_4 = window[4]
        val firstChange = (window_1 - window[0]).toByte()
        val secondChange = (window_2 - window_1).toByte()
        val thirdChange = (window_3 - window_2).toByte()
        val fourthChange = (window_4 - window_3).toByte()
        return PriceChangePattern(firstChange, secondChange, thirdChange, fourthChange) to window_4.toLong()
    }

    return prices
        .windowed(5)
        .map { convertWindow(it) }
}
internal data class PriceChangePattern(val firstChange : Byte, val secondChange: Byte, val thirdChange: Byte, val fourthChange: Byte)

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