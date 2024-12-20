package day20

import utils.*

const val MINIMUM_SAVINGS_PROD = 100

fun main() = Day20Solution(MINIMUM_SAVINGS_PROD).run()
class Day20Solution(val minimumSavings: Int) : IntSolution() {
    override fun part1(input: List<String>): Int {
        return Racetrack(input).countCheats(minimumSavings)
    }

    override fun part2(input: List<String>): Int {
        return Racetrack(input).countCheatsPart2(minimumSavings)
    }
}

internal data class Racetrack(val start: Point, val end: Point, val walls: Set<Point>, val bounds: Bounds) {

    private val dijkstraSearchResult: DijkstrasAlgorithm.Result<Point> by lazy { runDijkstrasSearch() }

    fun fastestPathWithoutCheating(): List<Point> {
        val previousNodes = dijkstraSearchResult.previousNodes
        val result = mutableListOf<Point>(start)
        var currentPoint = start
        while (currentPoint != end) {
            currentPoint = previousNodes[currentPoint]!!.first()
            result += currentPoint
        }
        return result
    }

    fun countCheats(minimumSavings: Int): Int {
        val pointsInNonCheatingPath = fastestPathWithoutCheating().toSet() - end
        val costs = dijkstraSearchResult.costs
        // don't need to worry about bogus cheat paths that don't cross a wall
        // these don't save any time and are automatically eliminated
        val potentialCheats = bounds
            .filter { it !in walls } //exit point must not be in a wall
            .map { it to it.pointsWithManhattanDistance(2).toSet().intersect(pointsInNonCheatingPath) }
            .flatMap { (exit, pointsOnOriginalPath) -> pointsOnOriginalPath.map { exit to it } }
            .filter{ (cheatExit, _) -> costs.containsKey(cheatExit) } //exit point must reach the end of the race
            .filter { (cheatExit, cheatStart) ->
                val cheatCost = cheatExit.manhattanDistance(cheatStart)
                val costWithoutCheating = costs[cheatStart]!!
                val costWithCheating = costs[cheatExit]!! + cheatCost //we don't teleport to the cheat exit
                costWithoutCheating - costWithCheating >= minimumSavings
            }
        return potentialCheats.count()
    }

    fun countCheatsPart2(minimumSavings: Int): Int {
        val pointsInNonCheatingPath = fastestPathWithoutCheating().toSet()
        val costs = dijkstraSearchResult.costs

        return (pointsInNonCheatingPath)
            .parallelStream()
            .flatMap { startPoint ->
                pointsInNonCheatingPath
                    .parallelStream()
                    .filter { startPoint.manhattanDistance(it) <= 20 }
                    .map { startPoint to it }
            }.filter { (cheatStart, cheatExit) ->
                val cheatCost = cheatStart.manhattanDistance(cheatExit)
                val costWithoutCheating = costs[cheatStart]!!
                val costWithCheating = costs[cheatExit]!! + cheatCost //we don't teleport to the cheat exit
                costWithoutCheating - costWithCheating >= minimumSavings
            }
            .count()
            .toInt()
    }

    private fun runDijkstrasSearch(): DijkstrasAlgorithm.Result<Point> {
        val dijkstras = DijkstrasAlgorithm<Point> { point ->
            point.getCardinalNeighbours()
                .filter { it in bounds }
                .filter { it !in walls }
                .map { it to 1 }
        }

        val searchResult = dijkstras.search(end)
        return searchResult
    }

    companion object {
        internal operator fun invoke(input: List<String>): Racetrack {
            var start: Point? = null
            var end: Point? = null
            val walls = mutableSetOf<Point>()
            val bounds = Bounds(input)

            bounds.forEach {
                when (input[it]) {
                    'S' -> start = it
                    'E' -> end = it
                    '#' -> walls += it
                }
            }

            return Racetrack(start!!, end!!, walls, bounds)
        }
    }
}