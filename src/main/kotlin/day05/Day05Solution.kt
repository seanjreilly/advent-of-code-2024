package day05

import utils.IntSolution

fun main() = Day05Solution().run()
class Day05Solution : IntSolution() {
    override fun part1(input: List<String>): Int {
        val rules = parsePageOrderingRules(input)
        return parsePageUpdates(input)
            .filter { isUpdateInRightOrder(it, rules) }
            .sumOf { findMiddle(it) }
    }

    override fun part2(input: List<String>): Int {
        val rules = parsePageOrderingRules(input)
        val comparator = PageRuleComparator(rules)
        return parsePageUpdates(input)
            .filter { !isUpdateInRightOrder(it, rules) }
            .map { fixPageOrders(it, comparator) }
            .sumOf { findMiddle(it) }
    }
}

internal fun isUpdateInRightOrder(pageUpdate: List<Int>, rules: List<PageOrderingRule>): Boolean {
    val pagePositions = pageUpdate.mapIndexed { index, pageNumber -> pageNumber to index }.toMap()

    fun isRuleViolated(rule: PageOrderingRule): Boolean {
        if (rule.before !in pagePositions) {
            return false
        }
        if (rule.after !in pagePositions) {
            return false
        }
        return pagePositions[rule.before]!! > pagePositions[rule.after]!!
    }

    return rules.none { isRuleViolated(it) }
}

internal fun fixPageOrders(pageUpdate: List<Int>, comparator: PageRuleComparator): List<Int> {
    return pageUpdate.sortedWith(comparator)
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