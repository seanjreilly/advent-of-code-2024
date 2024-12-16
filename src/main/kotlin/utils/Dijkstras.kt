package utils

import java.util.*

fun <N> dijkstras(vararg startNodes: N, costLimit: Int = Int.MAX_VALUE, neighboursMapping: (N) -> Collection<Pair<N, Int>>) : Map<N, Int> {
    return DijkstrasAlgorithm(costLimit, neighboursMapping).search(*startNodes).costs
}

class DijkstrasAlgorithm<N>(val costLimit: Int = Int.MAX_VALUE, val neighboursMapping: (N) -> Collection<Pair<N, Int>>) {
    constructor(neighboursMapping: (N) -> Collection<Pair<N, Int>>): this(Int.MAX_VALUE, neighboursMapping)

    fun search(vararg startNodes: N, ) : Result<N> {
        val tentativeDistances = mutableMapOf<N, Int>()
        val unvisitedNodes = PriorityQueue<Pair<N, Int>>(compareBy { it.second })
        val previousNodes = mutableMapOf<N, MutableSet<N>>() //also track the previous nodes, when you want to find the actual path

        /*
            Dijkstra's algorithm usually features just one start node, but you can occasionally have more,
            such as when modelling nodes as point + direction, if you're allowed to enter the maze
            facing whichever way you want.
         */
        startNodes.forEach { tentativeDistances[it] = 0 }
        unvisitedNodes += startNodes.map{ node -> node to 0 } //add the start nodes to the priority queue to kick things off

        val visitedNodes = mutableSetOf<N>()

        while (unvisitedNodes.isNotEmpty()) {
            val currentNode = unvisitedNodes.remove().first

            //do an extra filter to remove the duplicate entries from the priority queue (see below)
            if (currentNode in visitedNodes) {
                continue
            }

            visitedNodes += currentNode

            val distanceToCurrentNode = tentativeDistances[currentNode] ?: break //stop if we've reached an unreachable point

            val neighbours = neighboursMapping(currentNode)
            neighbours
                .filter { it.first !in visitedNodes }
                .forEach { (newNode, transitionCost) ->
                    val currentCostToNode = tentativeDistances[newNode] ?: Int.MAX_VALUE
                    val altDistance = distanceToCurrentNode + transitionCost
                    if (altDistance <= costLimit) { //filter out more expensive paths
                        if (altDistance < currentCostToNode) {
                            tentativeDistances[newNode] = altDistance
                            previousNodes[newNode] = mutableSetOf(currentNode)
                            unvisitedNodes.add(newNode to altDistance) //don't remove the old point (slow), just leave a duplicate entry
                        } else if (altDistance == currentCostToNode) {
                            previousNodes[newNode]!!.add(currentNode)
                        }
                    }
                }
        }
        return Result<N>(tentativeDistances, previousNodes)
    }

    data class Result<N>(val costs: Map<N, Int>, val previousNodes: Map<N, Set<N>>)
}