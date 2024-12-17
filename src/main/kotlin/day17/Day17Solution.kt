package day17

import day17.Operand.ComboOperand
import day17.Operand.LiteralOperand
import utils.StringSolution

fun main() = Day17Solution().run()
class Day17Solution : StringSolution() {
    override fun part1(input: List<String>): String {
        val program = Program(input)
        program.execute()
        return program.outputs.joinToString(",")
    }

    override fun part2(input: List<String>): String {
        val originalProgram = Program(input)
        fun recursiveSearch(previousRegisterAValue: Long, existingMatchingTerms: Int): Long? {
            if (existingMatchingTerms == originalProgram.instructions.size) {
                return previousRegisterAValue
            }

            val newBaseValue = previousRegisterAValue.shl(3)
            return (0L..7L)
                .map { newBaseValue + it }
                .map { potentialNewRegisterAValue ->
                    val program = originalProgram.copy(registerA = potentialNewRegisterAValue)
                    program.execute()
                    potentialNewRegisterAValue to program.outputs
                }
                .filter { (_, outputs) -> outputs.isTailOf(originalProgram.instructions) }
                .map { it.first }
                .firstNotNullOfOrNull { lng -> recursiveSearch(lng, existingMatchingTerms + 1) }
        }

        return recursiveSearch(0L, 0).toString()
    }
}

internal data class Program(var registerA: Long, var registerB: Long, var registerC: Long, val instructions: List<Int>) {
    fun execute() {
        while (instructionPointer <= (instructions.size - 1)) {
            val instruction: Instruction = instructionsByOpCode[instructions[instructionPointer]]
            val operandID = instructions[instructionPointer + 1]
            instruction.execute(operandID, this)
            instructionPointer += 2
        }
    }

    var instructionPointer = 0
    var outputs = mutableListOf<Int>()

    companion object {
        internal operator fun invoke(input: List<String>) : Program {
            val registerA = input.first().split(": ")[1].toLong()
            val instructions = input
                .drop(4)
                .first()
                .split(": ")[1]
                .split(',')
                .map(String::toInt)
            return Program(registerA, 0.toLong(), 0.toLong(), instructions)
        }
    }
}

internal sealed interface Operand {
    fun getValue(operandID: Int, program: Program) : Long

    object LiteralOperand : Operand {
        override fun getValue(operandID: Int, program: Program) = operandID.toLong()
    }

    object ComboOperand: Operand {
        override fun getValue(operandID: Int, program: Program): Long {
            return when(operandID) {
                in 0..3 -> operandID.toLong()
                4 -> program.registerA
                5 -> program.registerB
                6 -> program.registerC
                else -> throw IllegalArgumentException("Combo operands >= 7 are reserved and will not appear in valid programs")
            }
        }
    }
}

internal val instructionsByOpCode:List<Instruction> = listOf<Instruction>(
    Adv, Bxl, Bst, Jnz, Bxc, Out, Bdv, Cdv

)

internal sealed class Instruction(val operand: Operand) {
    abstract fun execute(operandID: Int, program: Program)
}

internal object Adv : Instruction(ComboOperand) {
    override fun execute(operandID: Int, program: Program) {
        val operandValue = operand.getValue(operandID, program)
        program.registerA = program.registerA shr operandValue.toInt()
    }
}

internal object Bxl : Instruction(LiteralOperand) {
    override fun execute(operandID: Int, program: Program) {
        val operandValue = operand.getValue(operandID, program)
        val registerB = program.registerB xor operandValue
        program.registerB = registerB
    }
}

internal object Bst : Instruction(ComboOperand) {
    override fun execute(operandID: Int, program: Program) {
        val operandValue = operand.getValue(operandID, program)
        program.registerB = operandValue and 7L
    }
}

internal object Jnz : Instruction(LiteralOperand) {
    override fun execute(operandID: Int, program: Program) {
        val operandValue = operand.getValue(operandID, program)
        if (program.registerA != 0L) {
            program.instructionPointer = operandValue.toInt() - 2
        }
    }
}

internal object Bxc : Instruction(LiteralOperand) {
    override fun execute(operandID: Int, program: Program) {
        program.registerB = program.registerB xor program.registerC
    }
}

internal object Out : Instruction(ComboOperand) {
    override fun execute(operandID: Int, program: Program) {
        val operandValue = operand.getValue(operandID, program)
        program.outputs += operandValue.mod(8)
    }
}

internal object Bdv : Instruction(ComboOperand) {
    override fun execute(operandID: Int, program: Program) {
        val operandValue = operand.getValue(operandID, program)
        program.registerB = program.registerA shr operandValue.toInt()
    }
}

internal object Cdv : Instruction(ComboOperand) {
    override fun execute(operandID: Int, program: Program) {
        val operandValue = operand.getValue(operandID, program)
        program.registerC = program.registerA shr operandValue.toInt()
    }
}

internal fun <T> List<T>.isTailOf(other: List<T>): Boolean {
    val sizeDifference = other.size - size
    if (sizeDifference < 0) {
        return false
    }
    return other.drop(sizeDifference) == this
}