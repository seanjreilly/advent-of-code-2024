package day23

import org.jgrapht.alg.clique.BronKerboschCliqueFinder
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph
import utils.StringSolution
import utils.twoElementCombinations

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
    val edges = findBiDirectionalEdges(input)

    return bronKerbosch(emptySet(), edges.keys, emptySet(), edges)!!

//    val graph = SimpleGraph<String, DefaultEdge>(DefaultEdge::class.java)
//    edges.keys.forEach { graph.addVertex(it) }
//    edges.forEach { vertex, neighbours ->
//        neighbours.forEach { neighbour -> graph.addEdge(vertex, neighbour) }
//    }
//
//    return BronKerboschCliqueFinder(graph).maximumIterator().next()
}

/*
    Use Bron-Kerbosch to find the maximal clique: https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
 */
private fun bronKerbosch(r: Set<String>, p: Set<String>, x: Set<String>, edges: Map<String, Set<String>>): Set<String>? {
    if (p.isEmpty()) {
        if (x.isEmpty()) {
            return r //r is a maximal clique
        }
    }
    val pPrime = p.toMutableSet()
    val xPrime = x.toMutableSet()

    val results = mutableListOf<Set<String>?>()

    for (v in p) {
        val vNeighbours = edges[v]!!
        results += bronKerbosch(r + v, (pPrime.intersect(vNeighbours)), (xPrime.intersect(vNeighbours)), edges)
        pPrime.remove(v)
        xPrime.add(v)
    }

    return results
        .filterNotNull()
        .maxByOrNull { it.size }
}

internal fun containNodeStartingWithT(nodeSets: Set<Set<String>>): Collection<Set<String>> {
    return nodeSets.filter { set -> set.any { it.startsWith('t') } }
}

internal fun findSetsOfThree(input: List<String>): Set<Set<String>> {
    val biDirectionalEdges = findBiDirectionalEdges(input)
    val setsOfThree = biDirectionalEdges.keys.flatMap { startNode ->
        biDirectionalEdges[startNode]!!.twoElementCombinations()
            .mapNotNull { (first, second) ->
                when (second in biDirectionalEdges[first]!!) {
                    true -> setOf(startNode, first, second)
                    false -> null
                }
            }
    }.toSet()
    return setsOfThree
}

private fun findBiDirectionalEdges(input: List<String>): Map<String, Set<String>>{
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

    return biDirectionalEdges
}