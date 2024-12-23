package day23

import utils.StringSolution
import utils.twoElementCombinations
import java.util.stream.Collectors.toSet

fun main() = Day23Solution().run()
class Day23Solution : StringSolution() {
    override fun part1(input: List<String>): String {
        return containNodeStartingWithT(findSetsOfThree(input)).count().toString()
    }

    override fun part2(input: List<String>): String {
        return findLargestFullyConnectedSet(input)
            .sorted()
            .joinToString(",")
    }
}

internal fun findLargestFullyConnectedSet(input: List<String>): Set<String> {
    val (edges, setsOfThree) = findBiDirectionalEdgesAndSetsOfThree(input)

    var currentGeneration: Set<Set<String>> = setsOfThree
    while (currentGeneration.size > 1) {
        currentGeneration = edges.entries
            .parallelStream()
            .flatMap { (key, values) ->
                currentGeneration
                    .parallelStream()
                    .filter { set -> values.containsAll(set) }
                    .map { set -> set + key }
            }.collect(toSet())
    }
    return currentGeneration.first()
}

internal fun containNodeStartingWithT(nodeSets: Set<Set<String>>): Collection<Set<String>> {
    return nodeSets.filter { set -> set.any { it.startsWith('t') } }
}

internal fun findSetsOfThree(input: List<String>): Set<Set<String>> {
    return findBiDirectionalEdgesAndSetsOfThree(input).second
}

private fun findBiDirectionalEdgesAndSetsOfThree(input: List<String>): Pair<MutableMap<String, Set<String>>, Set<Set<String>>> {
    val rawEdges: List<Pair<String, String>> = input
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

    val setsOfThree = biDirectionalEdges.keys.flatMap { startNode ->
        biDirectionalEdges[startNode]!!.twoElementCombinations()
            .mapNotNull { (first, second) ->
                when (second in biDirectionalEdges[first]!!) {
                    true -> setOf(startNode, first, second)
                    false -> null
                }
            }
    }.toSet()

    return biDirectionalEdges to setsOfThree
}