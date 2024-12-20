package day20

import utils.Bounds
import utils.DijkstrasAlgorithm
import utils.LongSolution
import utils.Point
import utils.get

const val PART1_MINIMUM_SAVINGS_PROD = 100

fun main() = Day20Solution(PART1_MINIMUM_SAVINGS_PROD).run()
class Day20Solution(val part1MinimumSavings: Int) : LongSolution() {
    override fun part1(input: List<String>): Long {
        return Racetrack(input).countCheats(part1MinimumSavings).toLong()
    }

    override fun part2(input: List<String>): Long {
        return Racetrack(input).countCheatsPart2(part1MinimumSavings).toLong()
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
        val pointsInNonCheatingPath = fastestPathWithoutCheating().toSet() - end
        val costs = dijkstraSearchResult.costs
        // don't need to worry about bogus cheat paths that don't cross a wall
        // these don't save any time and are automatically eliminated
        val potentialCheats = bounds
            .filter { it !in walls } //exit point must not be in a wall
            .filter { costs.containsKey(it) } //exit point must reach the end of the race
            .asSequence()
            .flatMap { exitPoint -> pointsInNonCheatingPath.map { exitPoint to it } }
            .filter { (exitPoint, startPoint) -> exitPoint.manhattanDistance(startPoint) <= 20 }
            .filter { (cheatExit, cheatStart) ->
                val cheatCost = cheatExit.manhattanDistance(cheatStart)
                val costWithoutCheating = costs[cheatStart]!!
                val costWithCheating = costs[cheatExit]!! + cheatCost //we don't teleport to the cheat exit
                costWithoutCheating - costWithCheating >= minimumSavings
            }
        return potentialCheats.count()
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