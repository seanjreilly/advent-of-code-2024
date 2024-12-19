package day19

import utils.LongSolution

fun main() = Day19Solution().run()
class Day19Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        val towelPatterns = parseTowelPatterns(input)
        val desiredDesigns = parseDesiredDesigns(input)
        return desiredDesigns
            .count { isDesignPossible(it, towelPatterns) }
            .toLong()
    }

    override fun part2(input: List<String>): Long {
        val counter = TowelCombinationCounter(parseTowelPatterns(input))
        val desiredDesigns = parseDesiredDesigns(input)
        return desiredDesigns
            .sumOf { counter.countPossibleTowelCombinations(it) }
    }
}

internal class TowelCombinationCounter(val towels: Set<String>) {

    private val substringMap = mutableMapOf<String, Long>("" to 1) //base case

    fun countPossibleTowelCombinations(towelDesign: String): Long {
        val cachedResult = substringMap[towelDesign]
        if (cachedResult != null) {
            return cachedResult
        }

        val result = towels
            .filter { towelDesign.startsWith(it) }
            .map { towelDesign.substring(it.length) }
            .sumOf { remainingDesign -> countPossibleTowelCombinations(remainingDesign) }
        substringMap.putIfAbsent(towelDesign, result)
        return result
    }
}

internal fun isDesignPossible(towelDesign: String, towels: Set<String>): Boolean {
    if (towelDesign == "") {
        return true //base case
    }
    return towels
        .filter { towelDesign.startsWith(it) }
        .map { towelDesign.substring(it.length) }
        .any { remainingDesign ->
            isDesignPossible(remainingDesign, towels)
        }
}

internal fun parseTowelPatterns(input: List<String>): Set<String> {
    return input.first().split(", ").toSet()
}

internal fun parseDesiredDesigns(input: List<String>): List<String> {
    return input.drop(2)
}
