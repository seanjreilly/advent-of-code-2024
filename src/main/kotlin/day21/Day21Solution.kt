package day21

import utils.LongSolution

fun main() = Day21Solution().run()
class Day21Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        TODO("Not yet implemented")
    }

    override fun part2(input: List<String>) = 0L
}

internal fun translateAllKeyboards(input: String): String {
    val rounds: List<Keyboard> = listOf(::doorKeyboard, ::robotKeyboard, ::robotKeyboard)
    return rounds.fold(input) { initial, operation -> operation(initial) }
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

val immediateDoorKeypadTransitions: Map<Char, Map<Char, Char>> = mapOf(
    'A' to mapOf('0' to '<', '3' to '^'),
    '0' to mapOf('2' to '^', 'A' to '>'),
    '1' to mapOf('4' to '^', '2' to '>'),
    '2' to mapOf('1' to '<', '5' to '^', '3' to '>', '0' to 'v'),
    '3' to mapOf('2' to '<', '6' to '^', 'A' to 'v'),
    '4' to mapOf('7' to '^', '5' to '>', '1' to 'v'),
    '5' to mapOf('4' to '<', '8' to '^', '6' to '>', '2' to 'v'),
    '6' to mapOf('5' to '<', '9' to '^', '3' to 'v'),
    '7' to mapOf('8' to '>', '4' to 'v'),
    '8' to mapOf('7' to '<', '9' to '>', '5' to 'v'),
    '9' to mapOf('8' to '<', '6' to 'v')
)

val immediateRobotKeypadTransitions: Map<Char, Map<Char, Char>> = mapOf(
    'A' to mapOf('^' to '<', '>' to 'v'),
    '^' to mapOf('A' to '>', 'v' to 'v'),
    '>' to mapOf('A' to '^', 'v' to '<'),
    'v' to mapOf('^' to '^', '<' to '<', '>' to '>'),
    '<' to mapOf('v' to '>')
)

internal fun generateAllShortestTransitions(immediateTransitions: Map<Char, Map<Char, Char>>): Map<KeyTransition, Set<String>> {

    fun translateNodesToDirections(nodePath: String) : String {
        return nodePath
            .toList()
            .windowed(2, 1)
            .map { (current, next) -> immediateTransitions[current]!![next]!!  }
            .joinToString("", postfix = "A")
    }

    fun findShortestMappings(from: Char) : Map<KeyTransition, Set<String>> {
        val pathsStartingAtFrom = mutableSetOf<String>(from.toString())
        var latestGenerationOfPaths = pathsStartingAtFrom.toSet()
        while (latestGenerationOfPaths.isNotEmpty()) {
            pathsStartingAtFrom += latestGenerationOfPaths
            latestGenerationOfPaths = latestGenerationOfPaths
                    .flatMap { path -> immediateTransitions[path.last()]!!.keys
                    .filter { it !in path }
                    .map { path + it }
                }
                .toSet()
        }

        return pathsStartingAtFrom
            .groupBy { it.last() }
            .mapValues { (_, values) ->
                val minLength = values.minOf { it.length }
                values
                    .filter { it.length == minLength }
                    .map { translateNodesToDirections(it) }
                    .toSet()
            }
            .mapKeys { (key, _) ->
                KeyTransition(from, key)
            }
    }

    val result = mutableMapOf<KeyTransition, Set<String>>()
    result += immediateTransitions.keys.associate { KeyTransition(it, it) to setOf("A") }

    immediateTransitions.keys.forEach { from ->
        result += findShortestMappings(from)
    }

    return result
}

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
    KeyTransition('v', 'A') to ">^A*",
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