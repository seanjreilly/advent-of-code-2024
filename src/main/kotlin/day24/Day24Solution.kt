package day24

import day24.Operation.*
import utils.StringSolution

fun main() = Day24Solution().run()
class Day24Solution : StringSolution() {
    override fun part1(input: List<String>): String {
        return findZGateOutputs(input).toString()
    }

    override fun part2(input: List<String>): String {
        /*
            The results are sorted alphabetically, so we don't actually
            need to find the pairs — if we find 8 gates that break the rules
            and sort them we don't need to determine exactly which gates should
            be swapped
         */
        return findInvalidGates(input)
            .map { it.outputWire }
            .sorted()
            .joinToString(",")
    }
}

internal fun findInvalidGates(input: List<String>): Set<Gate> {
    val gates = parseGates(input)
    val invalidGates = mutableListOf<Gate>()

    //return any gates that break the rules for a ripple carry adder
    // https://en.wikipedia.org/wiki/Adder_(electronics)#/media/File:Full-adder_logic_diagram.svg

    // If the output is a zWire, the operator should be XOR (unless it is z45)
    invalidGates += gates
        .filter { it.outputWire != "z45" }
        .filter { it.outputWire.startsWith('z') && it.operation != XOR }

    // If the output is not a zWire and the inputs are not x and y, the operator should be AND or OR
    invalidGates += gates
        .filter { !it.outputWire.startsWith('z') }
        .filter { !(it.sortedInputs[0].startsWith('x')) && !(it.sortedInputs[1].startsWith('y')) }
        .filter { it.operation !in setOf(AND, OR) }

    // If the inputs are x and y and the operator is XOR, the output wire should be the input of another XOR gate
    invalidGates += gates
        .filter { it.sortedInputs != listOf("x00", "y00")}
        .filter { it.sortedInputs[0].startsWith('x') && it.sortedInputs[1].startsWith('y') }
        .filter { it.operation == XOR }
        .filter { gate -> gates.none { gate.outputWire in it.sortedInputs && it.operation == XOR } }

    // If the operator is AND, the output wire should be the input of an OR gate
    invalidGates += gates
        .filter { it.sortedInputs != listOf("x00", "y00")}
        .filter { it.operation == AND }
        .filter { gate -> gates.none { gate.outputWire in it.sortedInputs && it.operation == OR } }

    return invalidGates.toSet() //some gates break more than one rule
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
    val sortedInputs by lazy { listOf(wire1, wire2).sorted() }
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