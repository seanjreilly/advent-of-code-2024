package day24

import day24.Operation.*
import utils.LongSolution

fun main() = Day24Solution().run()
class Day24Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        return findZGateOutputs(input)
    }

    override fun part2(input: List<String>) = 0L
}

internal fun findZGateOutputs(input: List<String>): Long {
    fun isZWire(gate: Gate): Boolean = gate.outputWire.startsWith('z')

    var gates = parseGates(input)
    val wires:WireMap = parseInitialWireValues(input)

    // if no remaining gates contribute to a z gate the answer is finished
    while(gates.any(::isZWire)) {
        val newGates = mutableListOf<Gate>()
        gates.forEach { gate ->
            val result = gate.waitForInput(wires)
            if (result != null) {
                wires[gate.outputWire] = result
            } else {
                newGates += gate
            }
        }
        gates = newGates
    }

    return wires.entries
        .filter { it.key.startsWith('z') }
        .sortedBy { it.key }
        .map { it.value.toLong() }
        .withIndex()
        .sumOf { (index, value) -> value.shl(index) }
}

internal data class Gate(val wire1: String, val wire2: String, val operation: Operation, val outputWire: String) {
    fun calculate(x: Int, y: Int): Int {
        return when(operation) {
            AND -> x and y
            OR -> x or y
            XOR -> x xor y
        }
    }

    fun waitForInput(wires: WireMap): Int? {
        val wire1Value = wires[wire1]
        val wire2Value = wires[wire2]
        return if (wire1Value != null && wire2Value != null) {
            calculate(wire1Value, wire2Value)
        } else {
            null
        }
    }
}

internal enum class Operation { AND, OR, XOR }


internal typealias WireMap = MutableMap<String, Int>
internal fun parseInitialWireValues(input: List<String>): WireMap {
    return input
        .takeWhile { it.isNotBlank() }
        .map { it.split(": ") }
        .associate { (first, second) -> first to second.toInt() }
        .toMutableMap()
}

private val GATE_REGEX = """(...) (AND|OR|XOR) (...) -> (...)""".toRegex()
internal fun parseGates(input: List<String>): List<Gate> {
    return input
        .dropWhile { it.isNotBlank() }
        .drop(1)
        .map(::parseGate)
}

internal fun parseGate(input: String): Gate {
    val (wire1, rawOperation, wire2, outputWire) = GATE_REGEX.matchEntire(input)!!.destructured
    val operation = Operation.valueOf(rawOperation)
    return Gate(wire1, wire2, operation, outputWire)
}