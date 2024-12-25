package day25

import utils.IntSolution
import utils.toChunks

fun main() = Day25Solution().run()
class Day25Solution : IntSolution() {
    override fun part1(input: List<String>): Int {
        val locks = parseLocks(input)
        val keys = parseKeys(input)
        return locks.sumOf { lock -> keys.count { lock.fits(it) } }
    }

    override fun part2(input: List<String>) = 0
}

internal data class Key(val segmentHeights: List<Int>) {
    constructor(seg0: Int, seg1: Int, seg2: Int, seg3: Int, seg4: Int) : this(listOf(seg0, seg1, seg2, seg3, seg4))
    init { assert(this.segmentHeights.size == 5) { "A Key must have 5 segments" } }
}

internal data class Lock(val pinHeights: List<Int>) {
    constructor(pin0: Int, pin1: Int, pin2: Int, pin3: Int, pin4: Int) : this(listOf(pin0, pin1, pin2, pin3, pin4))
    init { assert(this.pinHeights.size == 5) { "A Lock must have 5 pins" } }

    fun fits(key: Key): Boolean {
        return pinHeights.zip(key.segmentHeights).all { (lockPin, keyPin) -> lockPin + keyPin <= 5 }
    }
}

internal fun parseKeys(input: List<String>): List<Key> {
    return input.toChunks()
        .filter { it.first() == "....."}
        .map { it.pivot() }
        .map { chunk ->
            val pinLengths = chunk.map { it.count('#') - 1 } //the bottom line is one
            Key(pinLengths)
        }
        .toList()
}

internal fun parseLocks(input: List<String>): List<Lock> {
    return input.toChunks()
        .filter { it.first() == "#####" }
        .map { it.pivot() }
        .map { chunk ->
            val pinLengths = chunk.map { it.count('#') - 1 } //the top line is one
            Lock(pinLengths)
        }
        .toList()
}

internal fun String.count(char: Char): Int = this.count { it == char }