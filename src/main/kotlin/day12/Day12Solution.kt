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
            else -> require(
                plots.all { plot -> plot.getCardinalNeighbours().any { it in plots } },
                { "Plots in a region must be contiguous" }
            )
        }
    }

    val area = plots.size
    fun perimeter() = plots.sumOf { 4 - neighboursInRegion(it) }
    fun neighboursInRegion(plot: Plot) = plot.getCardinalNeighbours().filter { it in plots }.size
    fun fencePrice() = area * perimeter()
    fun fencePricePart2() = sides() * area

    fun sides() : Int {

        val directionToBorder = mapOf(
            North to Border.Top,
            East to Border.Right,
            South to Border.Bottom,
            West to Border.Left
        )

        val sideSegments = plots
            .flatMap { plot ->
                CardinalDirection.entries
                    .filter { direction -> plot.move(direction) !in plots }
                    .map { direction -> directionToBorder[direction]!! }
                    .map { border -> SideSegment(plot, border)  }
            }
            .toSet()

        val distinctHorizontalEdges = sideSegments.groupBy { segment -> segment.plot.y }
            .values
            .sumOf { segmentsInRow ->
                val topEdgesInRow = segmentsInRow
                    .filter { segment -> segment.border == Border.Top }
                    .map { it.plot.x }
                    .sorted()

                val distinctTopEdgesInRow = if (topEdgesInRow.size == 0) {
                    0
                } else {
                    topEdgesInRow
                        .windowed(2)
                        .count { (first, second) -> second > first + 1 } + 1
                }

                val bottomEdgesInRow = segmentsInRow
                    .filter { segment -> segment.border == Border.Bottom }
                    .map { it.plot.x }
                    .sorted()

                val distinctBottomEdgesInRow = if (bottomEdgesInRow.size == 0) {
                    0
                } else {
                    bottomEdgesInRow
                        .windowed(2)
                        .count { (first, second) -> second > first + 1 } + 1
                }

                distinctTopEdgesInRow + distinctBottomEdgesInRow
            }

        val distinctVerticalEdges = sideSegments.groupBy { segment -> segment.plot.x }
            .values
            .sumOf { segmentsInColumn ->
                val leftEdgesInColumn = segmentsInColumn
                    .filter { segment -> segment.border == Border.Left }
                    .map { it.plot.y }
                    .sorted()
                val distinctLeftEdgesInColumn = if(leftEdgesInColumn.size == 0) {
                    0
                } else {
                    leftEdgesInColumn
                        .windowed(2)
                        .count { (first, second) -> second > first + 1 } + 1
                }

                val rightEdgesInColumn = segmentsInColumn
                    .filter { segment -> segment.border == Border.Right }
                    .map { it.plot.y }
                    .sorted()

                val distinctRightEdgesInColumn = if (rightEdgesInColumn.size == 0) {
                    0
                } else {
                    rightEdgesInColumn
                        .windowed(2)
                        .count { (first, second) -> second > first + 1 } + 1
                }

                distinctLeftEdgesInColumn + distinctRightEdgesInColumn
            }

        return distinctVerticalEdges + distinctHorizontalEdges
    }
}

private data class SideSegment(val plot: Plot, val border: Border)

enum class Border() {
    Top,
    Bottom,
    Left,
    Right
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