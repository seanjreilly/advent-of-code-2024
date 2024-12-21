package day21

import utils.LongSolution

fun main() = Day21Solution().run()
class Day21Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        TODO("Not yet implemented")
    }

    override fun part2(input: List<String>) = 0L
}

internal val doorKeyboardTransitions = mapOf(
    KeyTransition('A', 'A') to "A",
    KeyTransition('A', '0') to "<A",
    KeyTransition('A', '1') to "^<<A*",
    KeyTransition('A', '2') to "^<A*",
    KeyTransition('A', '3') to "^A",
    KeyTransition('A', '4') to "^^<<A*",
    KeyTransition('A', '5') to "^^<A*",
    KeyTransition('A', '6') to "^^A",
    KeyTransition('A', '7') to "^^^<<A*",
    KeyTransition('A', '8') to "^^^<A*",
    KeyTransition('A', '9') to "^^^A",
    KeyTransition('0', 'A') to ">A",
    KeyTransition('0', '0') to "A",
    KeyTransition('0', '1') to "^<A",
    KeyTransition('0', '2') to "^A",
    KeyTransition('0', '3') to "^>A*",
    KeyTransition('0', '4') to "^^<A*",
    KeyTransition('0', '5') to "^^A",
    KeyTransition('0', '6') to "^^>A*",
    KeyTransition('0', '7') to "^^^<A*",
    KeyTransition('0', '8') to "^^^A",
    KeyTransition('0', '9') to "^^^>A*",
    KeyTransition('1', 'A') to ">>vA*",
    KeyTransition('1', '0') to ">vA",
    KeyTransition('1', '1') to "A",
    KeyTransition('1', '2') to ">A",
    KeyTransition('1', '3') to ">>A",
    KeyTransition('1', '4') to "^A",
    KeyTransition('1', '5') to "^>A*",
    KeyTransition('1', '6') to "^>>A*",
    KeyTransition('1', '7') to "^^A",
    KeyTransition('1', '8') to "^^>A*",
    KeyTransition('1', '9') to "^^>>A*",
    KeyTransition('2', 'A') to ">vA*",
    KeyTransition('2', '0') to "vA",
    KeyTransition('2', '1') to "<A",
    KeyTransition('2', '2') to "A",
    KeyTransition('2', '3') to ">A",
    KeyTransition('2', '4') to "^<A*",
    KeyTransition('2', '5') to "^A",
    KeyTransition('2', '6') to "^>A*",
    KeyTransition('2', '7') to "^^<A*",
    KeyTransition('2', '8') to "^^A",
    KeyTransition('2', '9') to ">^^A*",
    KeyTransition('3', 'A') to "vA",
    KeyTransition('3', '0') to "v<A*",
    KeyTransition('3', '1') to "<<A",
    KeyTransition('3', '2') to "<A",
    KeyTransition('3', '3') to "A",
    KeyTransition('3', '4') to "^<<A*",
    KeyTransition('3', '5') to "^<A*",
    KeyTransition('3', '6') to "^A",
    KeyTransition('3', '7') to "^^<<A*",
    KeyTransition('3', '8') to "^^<A*",
    KeyTransition('3', '9') to "^^A",
    KeyTransition('4', 'A') to ">>vvA*",
    KeyTransition('4', '0') to ">vvA*",
    KeyTransition('4', '1') to "vA",
    KeyTransition('4', '2') to ">vA*",
    KeyTransition('4', '3') to ">>vA*",
    KeyTransition('4', '4') to "A",
    KeyTransition('4', '5') to ">A",
    KeyTransition('4', '6') to ">>A",
    KeyTransition('4', '7') to "^A",
    KeyTransition('4', '8') to "^>A*",
    KeyTransition('4', '9') to "^>>A*",
    KeyTransition('5', 'A') to ">vvA*",
    KeyTransition('5', '0') to "vvA",
    KeyTransition('5', '1') to "v<A*",
    KeyTransition('5', '2') to "vA",
    KeyTransition('5', '3') to "v>A*",
    KeyTransition('5', '4') to "<A",
    KeyTransition('5', '5') to "A",
    KeyTransition('5', '6') to ">A",
    KeyTransition('5', '7') to "^<A*",
    KeyTransition('5', '8') to "^A",
    KeyTransition('5', '9') to "^>A*",
    KeyTransition('6', 'A') to "vvA",
    KeyTransition('6', '0') to "<vvA*",
    KeyTransition('6', '1') to "<<vA*",
    KeyTransition('6', '2') to "<vA*",
    KeyTransition('6', '3') to "vA",
    KeyTransition('6', '4') to "<<A",
    KeyTransition('6', '5') to "<A",
    KeyTransition('6', '6') to "A",
    KeyTransition('6', '7') to "^<<A*",
    KeyTransition('6', '8') to "^<A*",
    KeyTransition('6', '9') to "^A",
    KeyTransition('7', 'A') to ">>vvvA*",
    KeyTransition('7', '0') to ">vvvA*",
    KeyTransition('7', '1') to "vvA",
    KeyTransition('7', '2') to ">vvA*",
    KeyTransition('7', '3') to ">>vvA*",
    KeyTransition('7', '4') to "vA",
    KeyTransition('7', '5') to "v>A*",
    KeyTransition('7', '6') to "v>>A*",
    KeyTransition('7', '7') to "A",
    KeyTransition('7', '8') to ">A",
    KeyTransition('7', '9') to ">>A",
    KeyTransition('8', 'A') to ">vvvA*",
    KeyTransition('8', '0') to "vvvA",
    KeyTransition('8', '1') to "<vvA*",
    KeyTransition('8', '2') to "vvA*",
    KeyTransition('8', '3') to ">vvA*",
    KeyTransition('8', '4') to "<vA*",
    KeyTransition('8', '5') to "vA",
    KeyTransition('8', '6') to "v>A*",
    KeyTransition('8', '7') to "<A",
    KeyTransition('8', '8') to "A",
    KeyTransition('8', '9') to ">A",
    KeyTransition('9', 'A') to "vvvA",
    KeyTransition('9', '0') to "<vvvA",
    KeyTransition('9', '1') to "<<vvA*",
    KeyTransition('9', '2') to "<vvA*",
    KeyTransition('9', '3') to "vvA",
    KeyTransition('9', '4') to "<<vA*",
    KeyTransition('9', '5') to "<vA*",
    KeyTransition('9', '6') to "vA",
    KeyTransition('9', '7') to "<<A",
    KeyTransition('9', '8') to "<A",
    KeyTransition('9', '9') to "A",
)

internal fun doorKeyboard(input: String) = translate(input, doorKeyboardTransitions)

internal val robotKeyboardTransitions = mapOf(
    KeyTransition('A', 'A') to "A",
    KeyTransition('A', '^') to "<A",
    KeyTransition('A', '<') to "v<<A*",
    KeyTransition('A', 'v') to "<vA*",
    KeyTransition('A', '>') to "vA",
    KeyTransition('^', 'A') to ">A",
    KeyTransition('^', '^') to "A",
    KeyTransition('^', '<') to "v<A***",
    KeyTransition('^', 'v') to "vA",
    KeyTransition('^', '>') to "v>A*",
    KeyTransition('<', 'A') to ">>^A***",
    KeyTransition('<', '^') to ">^A***",
    KeyTransition('<', '<') to "A",
    KeyTransition('<', 'v') to ">A",
    KeyTransition('<', '>') to ">>A",
    KeyTransition('v', 'A') to "^>A*",
    KeyTransition('v', '^') to "^A",
    KeyTransition('v', '<') to "<A",
    KeyTransition('v', 'v') to "A",
    KeyTransition('v', '>') to ">A",
    KeyTransition('>', 'A') to "^A",
    KeyTransition('>', '^') to "<^A*",
    KeyTransition('>', '<') to "<<A",
    KeyTransition('>', 'v') to "<A",
    KeyTransition('>', '>') to "A",
)

internal fun robotKeyboard(input: String) = translate(input, robotKeyboardTransitions)

private fun translate(input: String, transitionMap: Map<KeyTransition, String>): String {
    var result = ""
    var currentPosition = 'A'
    input.forEach { newPosition ->
        val transition = KeyTransition(currentPosition, newPosition)
        result += transitionMap[transition]!!.replace("*", "") //remove notes to self
        currentPosition = newPosition
    }
    return result
}

data class KeyTransition(val fromKey: Char, val toKey: Char)
typealias Keyboard = (String) -> String