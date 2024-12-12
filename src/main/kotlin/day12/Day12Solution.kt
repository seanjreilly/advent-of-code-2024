package day12

import utils.Bounds
import utils.CardinalDirection
import utils.CardinalDirection.*
import utils.LongSolution
import utils.Point
import utils.get

fun main() = Day12Solution().run()
class Day12Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        return findRegions(input).sumOf { it.fencePrice().toLong() }
    }

    override fun part2(input: List<String>): Long {
        return findRegions(input).sumOf { it.fencePricePart2().toLong() }
    }
}

typealias Plot = Point

internal data class Region(val plantType: Char, val plots: Set<Plot>) {
    constructor(plantType: Char, vararg plots : Plot) : this(plantType, plots.toSet())
    init {
        when (plots.size) {
            0 -> throw IllegalArgumentException("A region must contain at least one plot")
            1 -> true //a single plot is automatically contiguous
            else -> require(plots.all { plot -> plot.getCardinalNeighbours().any { it in plots } }) { "Plots in a region must be contiguous" }
        }
    }

    val area = plots.size
    fun perimeter() = plots.sumOf { 4 - neighboursInRegion(it) }
    fun neighboursInRegion(plot: Plot) = plot.getCardinalNeighbours().filter { it in plots }.size
    fun fencePrice() = area * perimeter()
    fun fencePricePart2() = sides() * area

    fun sides() : Int {

        val sideSegments = plots
            .flatMap { plot ->
                CardinalDirection.entries
                    .filter { direction -> plot.move(direction) !in plots }
                    .map { direction -> SideSegment(plot, direction)  }
            }
            .toSet()

        fun countUniqueEdges(
            segmentsInRowOrColumn: List<SideSegment>,
            vararg directions: CardinalDirection,
            transform: (SideSegment) -> Int
        ): Int {
            return directions.sumOf { direction ->
                segmentsInRowOrColumn
                    .filter { it.border == direction }
                    .map(transform)
                    .sorted()
                    .windowed(2, partialWindows = true)
                    .count { list -> list.size == 1 || list.reduceRight { first, second -> second - first } > 1 }
                //a partial window means there was only one side segment in this row/column. That's a unique side.
            }
        }

        val distinctHorizontalEdges = sideSegments
            .groupBy { it.plot.y }
            .values
            .sumOf { segmentsInRow -> countUniqueEdges(segmentsInRow, North, South) { it.plot.x } }

        val distinctVerticalEdges = sideSegments
            .groupBy { it.plot.x }
            .values
            .sumOf { segmentsInColumn -> countUniqueEdges(segmentsInColumn, East, West) { it.plot.y } }
        return distinctVerticalEdges + distinctHorizontalEdges
    }
}

internal fun findRegions(map: List<String>): Collection<Region> {
    val bounds = Bounds(map)
    val visitedPlots = mutableSetOf<Plot>()

    fun exploreRegion(firstPlot: Plot): Region {
        val plantType = map[firstPlot]
        val plotsInRegion = mutableSetOf<Plot>()
        var searchGeneration = setOf(firstPlot)
        while (searchGeneration.isNotEmpty()) {
            plotsInRegion += searchGeneration
            visitedPlots += searchGeneration
            searchGeneration = searchGeneration
                .flatMap { it.getCardinalNeighbours() }
                .filter { it !in visitedPlots } //which includes plots in the region already
                .filter { it in bounds }
                .filter { map[it] == plantType }
                .toSet() //use a set in case a large number of plots all border the same new neighbour
        }
        return Region(plantType, plotsInRegion)
    }

    return bounds
        .mapNotNull { plot ->
            when (plot in visitedPlots) {
                true -> null //we've already processed this plot
                false -> exploreRegion(plot) //new region
            }
        }
}

private data class SideSegment(val plot: Plot, val border: CardinalDirection)