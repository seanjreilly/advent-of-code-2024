package day21

import utils.LongSolution

fun main() = Day21Solution().run()
class Day21Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        return input.sumOf { calculatePart1Complexity(it) }.toLong()
    }

    override fun part2(input: List<String>): Long {
        return input.sumOf { calculatePart2Complexity(it) }
    }
}

internal fun calculatePart1Complexity(code: String): Int {
    return robotKeypad(code, 2, 0).toInt() * code.dropLast(1).toInt()
}

internal fun calculatePart2Complexity(code: String): Long {
    return robotKeypad(code, 25, 0) * code.dropLast(1).toLong()
}

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

val completeDoorKeypadTransitions = generateAllShortestTransitions(immediateDoorKeypadTransitions)

fun doorKeypad(input: String) : Set<String> {
    var results = listOf("")
    var currentPosition = 'A'
    input.forEach { newPosition ->
        val transition = KeyTransition(currentPosition, newPosition)
        val newSegments = completeDoorKeypadTransitions[transition]!!
        results = results.flatMap { oldResult -> newSegments.map { oldResult + it } }
        currentPosition = newPosition
    }
    return results.toSet()
}

val immediateRobotKeypadTransitions: Map<Char, Map<Char, Char>> = mapOf(
    'A' to mapOf('^' to '<', '>' to 'v'),
    '^' to mapOf('A' to '>', 'v' to 'v'),
    '>' to mapOf('A' to '^', 'v' to '<'),
    'v' to mapOf('^' to '^', '<' to '<', '>' to '>'),
    '<' to mapOf('v' to '>')
)

val completeRobotKeypadTransitions = generateAllShortestTransitions(immediateRobotKeypadTransitions)

fun robotKeypad(input: String) : Set<String> {
    var results = listOf("")
    var currentPosition = 'A'
    input.forEach { newPosition ->
        val transition = KeyTransition(currentPosition, newPosition)
        val newSegments = completeRobotKeypadTransitions[transition]!!
        results = results.flatMap { oldResult -> newSegments.map { oldResult + it } }
        currentPosition = newPosition
    }
    return results.toSet()
}

val cache = mutableMapOf<CacheKey, Long>()
data class CacheKey(val input: String, val rounds: Int, val currentRound: Int)

fun robotKeypad(input: String, rounds: Int, currentRound: Int) : Long {
    val keypad: Keypad = if (currentRound == 0) { ::doorKeypad } else { ::robotKeypad }
    return cache.getOrPut(CacheKey(input, rounds, currentRound)) {
        val paths = keypad(input)
        if (currentRound == rounds) {
            return@getOrPut paths.minOf { it.length }.toLong()
        }
        return@getOrPut keypad(input)
            .minOf { path ->
                //break into segments and recurse on each segment (the lower keypad will reset its state once A is pressed)
                val segments = path.split('A').dropLast(1).map { it + 'A' }
                segments.sumOf { segment -> robotKeypad(segment, rounds, currentRound + 1) }
            }
    }
}

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

data class KeyTransition(val fromKey: Char, val toKey: Char)
typealias Keypad = (String) -> Set<String>