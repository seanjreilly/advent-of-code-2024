package day11

import utils.LongSolution

fun main() = Day11Solution().run()
class Day11Solution : LongSolution() {
    override fun part1(input: List<String>) = solve(input, 25)
    override fun part2(input: List<String>) = solve(input, 75)

    private fun solve(input: List<String>, times: Int): Long {
        var stones = input.first().toParallelStones()
        repeat(times) { stones = stones.blink() }
        return stones.totalCount()
    }
}

internal data class ParallelStones(val stoneCounts: Map<Stone, Long>) {
    fun blink() : ParallelStones {
        val newStoneCounts = stoneCounts.entries
            .flatMap { entry -> entry.key.blink().map { it to entry.value } }
            .groupBy({ it.first }, { it.second })
            .mapValues { entry -> entry.value.sum() }
        return ParallelStones(newStoneCounts)
    }

    fun totalCount() = stoneCounts.values.sum()
}

internal data class Stone(val value: Long, private val stringValue: String = value.toString()) {
    fun blink(): List<Stone> {
        return when {
            value == 0L -> listOf(Stone(1))
            stringValue.isEvenLength() -> value.toString().halves().map { Stone(it.toLong()) }
            else -> listOf(Stone(value * 2024L))
        }
    }
}

internal fun List<Stone>.blink() = this.flatMap { it.blink() }

internal fun String.toParallelStones() = ParallelStones(this.toStones().associate { it to 1L })

internal fun String.toStones() = this.split(' ').map { it.toLong() }.map {  Stone(it) }
internal fun String.isEvenLength() = this.length % 2 == 0

internal fun String.halves() : List<String> {
    require(this.isEvenLength()) { "String must be even in length" }
    val midPoint = this.length / 2
    return listOf(this.slice(0 until midPoint), this.slice(midPoint until this.length))
}