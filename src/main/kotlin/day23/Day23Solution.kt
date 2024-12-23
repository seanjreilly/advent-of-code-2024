package day23

import utils.LongSolution
import utils.twoElementCombinations

fun main() = Day23Solution().run()
class Day23Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        return containNodeStartingWithT(findSetsOfThree(input)).count().toLong()
    }

    override fun part2(input: List<String>) = 0L
}

internal fun containNodeStartingWithT(nodeSets: Set<Set<String>>): Collection<Set<String>> {
    return nodeSets.filter { set -> set.any { it.startsWith('t') } }
}

internal fun findSetsOfThree(input: List<String>): Set<Set<String>> {
    val rawEdges: List<Pair<String,String>> = input
        .map { it.split('-') }
        .map { (first, second) -> first to second }

    //forward edges first
    val biDirectionalEdges = rawEdges.groupBy({ it.first }, { it.second })
        .mapValues { it.value.toSet() }
        .toMutableMap()

    //add reverse edges
    rawEdges.groupBy({ it.second }, { it.first })
        .mapValues { it.value.toSet() }
        .forEach { key, values ->
            biDirectionalEdges.merge(key, values, Set<String>::plus)
        }

    return biDirectionalEdges.keys.flatMap { startNode ->
        biDirectionalEdges[startNode]!!.twoElementCombinations()
            .mapNotNull { (first, second) ->
                when (second in biDirectionalEdges[first]!!) {
                    true -> setOf(startNode, first, second)
                    false -> null
                }
            }
    }.toSet()
}