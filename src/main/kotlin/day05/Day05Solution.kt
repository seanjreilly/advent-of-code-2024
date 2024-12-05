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

    override fun part2(input: List<String>) = 0
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

internal data class PageOrderingRule(val before: Int, val after: Int)
internal fun parsePageOrderingRules(input: List<String>): List<PageOrderingRule> {
    return input
        .takeWhile { line -> line.isNotBlank() }
        .flatMap { line -> line.split('|') }
        .map(String::toInt)
        .windowed(2,2)
        .map { (before, after) -> PageOrderingRule(before, after) }
}

internal fun parsePageUpdates(input: List<String>): List<List<Int>> {
    return input
        .dropWhile { it.isNotBlank() }
        .drop(1)
        .map { line -> line.split(',').map(String::toInt) }
}

internal fun <T> findMiddle(list: List<T>): T {
    if (list.size % 2 == 0) {
        throw IllegalArgumentException("Need a list of odd length to find the middle")
    }
    return list[list.size/2]
}