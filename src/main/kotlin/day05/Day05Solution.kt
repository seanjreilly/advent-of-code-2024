package day05

import utils.IntSolution

fun main() = Day05Solution().run()
class Day05Solution : IntSolution() {
    override fun part1(input: List<String>): Int {
        val comparator = PageRuleComparator(parsePageOrderingRules(input))
        return parsePageUpdates(input)
            .filter { comparator.isUpdateInRightOrder(it) }
            .sumOf { findMiddle(it) }
    }

    override fun part2(input: List<String>): Int {
        val comparator = PageRuleComparator(parsePageOrderingRules(input))
        return parsePageUpdates(input)
            .filter { !comparator.isUpdateInRightOrder(it) }
            .map { comparator.fixPageOrders(it) }
            .sumOf { findMiddle(it) }
    }
}

internal fun <T> findMiddle(list: List<T>): T {
    if (list.size % 2 == 0) {
        throw IllegalArgumentException("Need a list of odd length to find the middle")
    }
    return list[list.size / 2]
}

internal class PageRuleComparator(rules: List<PageOrderingRule>) : Comparator<Int> {
    //index the rules by the before page, with a set of after pages as the map value
    private val index = rules.groupBy { it.before }.mapValues { entry -> entry.value.map { it.after }.toSet() }
    private val emptySet = emptySet<Int>()

    override fun compare(o1: Int?, o2: Int?): Int {
        return when {
            o1 == null -> 0
            o2 == null -> 0
            o1 == o2 -> 0
            o2 in (index[o1] ?: emptySet) -> -1
            o1 in (index[o2] ?: emptySet) -> 1
            else -> 0
        }
    }

    fun isUpdateInRightOrder(pageUpdate: List<Int>): Boolean {
        val pagesVisitedSoFar = mutableSetOf<Int>()
        return pageUpdate.all { page ->
            val result = pagesVisitedSoFar.intersect(index[page] ?: emptySet)
            pagesVisitedSoFar += page
            result.isEmpty()
        }
    }

    fun fixPageOrders(pageUpdate: List<Int>) = pageUpdate.sortedWith(this)
}

internal data class PageOrderingRule(val before: Int, val after: Int)

internal fun parsePageOrderingRules(input: List<String>): List<PageOrderingRule> {
    return input
        .takeWhile { line -> line.isNotBlank() }
        .flatMap { line -> line.split('|') }
        .map(String::toInt)
        .windowed(2, 2)
        .map { (before, after) -> PageOrderingRule(before, after) }
}

internal fun parsePageUpdates(input: List<String>): List<List<Int>> {
    return input
        .dropWhile { it.isNotBlank() }
        .drop(1)
        .map { line -> line.split(',').map(String::toInt) }
}