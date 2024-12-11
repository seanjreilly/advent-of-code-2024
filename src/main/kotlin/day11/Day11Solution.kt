package day11

import utils.LongSolution

fun main() = Day11Solution().run()
class Day11Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        var stones = input.first().toStones()
        repeat(25) { stones = stones.blink() }
        return stones.size.toLong()
    }

    override fun part2(input: List<String>) = 0L
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

internal fun String.toStones() = this.split(' ').map { it.toLong() }.map {  Stone(it) }
internal fun String.isEvenLength() = this.length % 2 == 0

internal fun String.halves() : List<String> {
    require(this.isEvenLength()) { "String must be even in length" }
    val midPoint = this.length / 2
    return listOf(this.slice(0 until midPoint), this.slice(midPoint until this.length))
}