package day17

import day17.Operand.ComboOperand
import day17.Operand.LiteralOperand
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Day17SolutionTest {
    private val sampleInput = """
        Register A: 729
        Register B: 0
        Register C: 0

        Program: 0,1,5,4,3,0
    """.trimIndent().lines()

    private val solution = Day17Solution()

    @Test
    fun `parseProgram should return a Program instance`() {
        val program = Program(sampleInput)

        assert(program.registerA == 729L)
        assert(program.registerB == 0L)
        assert(program.registerC == 0L)
        assert(program.instructions == listOf(0,1,5,4,3,0))
    }

    @Nested
    inner class ProgramTest {
        @Test
        fun `constructor should initialise the instruction pointer to zero`() {
            val program = Program(sampleInput)

            assert(program.instructionPointer == 0)
        }

        @Test
        fun `execute should run the program and return the expected output given the first sample`() {
            val program = Program(0L, 0L, 9, listOf(2,6))
            program.execute()

            assert(program.registerB == 1L)
        }

        @Test
        fun `execute should run the program and return the expected output given the second sample`() {
            val program = Program(10L, 0L, 0, listOf(5,0,5,1,5,4))
            program.execute()

            assert(program.outputs == listOf(0,1,2))
        }

        @Test
        fun `execute should run the program and return the expected output given the third sample`() {
            val program = Program(2024L, 0L, 0, listOf(0,1,5,4,3,0))
            program.execute()

            assert(program.outputs == listOf(4,2,5,6,7,7,7,7,3,1,0))
            assert(program.registerA == 0L)
        }

        @Test
        fun `execute should run the program and return the expected output given the fourth sample`() {
            val program = Program(0L, 29L, 0, listOf(1,7))
            program.execute()

            assert(program.registerB == 26L)
        }

        @Test
        fun `execute should run the program and return the expected output given the fifth sample`() {
            val program = Program(0L, 2024L, 43690L, listOf(4,0))
            program.execute()

            assert(program.registerB == 44354L)
        }
    }

    @Nested
    inner class OperandTest {


        @Nested
        inner class LiteralOperandTest {

            internal val program = Program(sampleInput)

            @Test
            fun `getValue should return the operandID`() {
                assert(LiteralOperand.getValue(0, program) == 0L)
                assert(LiteralOperand.getValue(1, program) == 1L)
                assert(LiteralOperand.getValue(2, program) == 2L)
                assert(LiteralOperand.getValue(3, program) == 3L)
                assert(LiteralOperand.getValue(4, program) == 4L)
                assert(LiteralOperand.getValue(5, program) == 5L)
                assert(LiteralOperand.getValue(6, program) == 6L)
                assert(LiteralOperand.getValue(7, program) == 7L)
            }
        }

        @Nested inner class ComboOperandTest {
            internal val program = Program(89L, 76L, 114L, emptyList())

            @Test
            fun `getValue should return zero given an operandID of zero`() {
                assert(ComboOperand.getValue(0, program) == 0L)
            }

            @Test
            fun `getValue should return 1 given an operandID of 1`() {
                assert(ComboOperand.getValue(1, program) == 1L)
            }

            @Test
            fun `getValue should return 2 given an operandID of 2`() {
                assert(ComboOperand.getValue(2, program) == 2L)
            }

            @Test
            fun `getValue should return 3 given an operandID of 3`() {
                assert(ComboOperand.getValue(3, program) == 3L)
            }

            @Test
            fun `getValue should return register A given an operandID of 4`() {
                assert(ComboOperand.getValue(4, program) == program.registerA)
            }

            @Test
            fun `getValue should return register B given an operandID of 5`() {
                assert(ComboOperand.getValue(5, program) == program.registerB)
            }

            @Test
            fun `getValue should return register C given an operandID of 6`() {
                assert(ComboOperand.getValue(6, program) == program.registerC)
            }

            @Test
            fun `getValue should throw an Exception given an operandID of 7`() {
                assertThrows<IllegalArgumentException> { ComboOperand.getValue(7, program) }
            }
        }
    }

    @Nested
    inner class InstructionTest {
        @Test
        fun `adv should divide register A by 2 ^ combo operand, truncate to an integer, and store in register A`() {
            val program = Program(100L, 0L, 0L, emptyList())

            val instruction: Instruction = Adv

            instruction.execute(2, program)

            assert(program.registerA == 25L)
        }

        @Test
        fun `adv should use ComboOperand`() {
            assert(Adv.operand == ComboOperand)
        }

        @Test
        fun `bxl should calculate the bitwise XOR of register B and the literal operand, and store in register B`() {
            val program = Program(0L, 7L, 0L, emptyList())

            val instruction: Instruction = Bxl

            instruction.execute(6, program)

            assert(program.registerB == 1L)

        }

        @Test
        fun `bxl should use LiteralOperand`() {
            assert(Bxl.operand == LiteralOperand)
        }

        @Test
        fun `bst should calculate the value of the combo operand modulo 8 (thereby keeping only its lowest 3 bits), and store in register B`() {
            val program = Program(125L, 0L, 0L, emptyList())
            val instruction: Instruction = Bst

            instruction.execute(4, program)

            assert(program.registerB == 5L)
        }

        @Test
        fun `bst should use ComboOperand`() {
            assert(Bst.operand == ComboOperand)
        }

        @Test
        fun `jnz should do nothing if register A is 0`() {
            val originalInstructionPointer = 3
            //copy doesn't include the instruction pointer
            val originalProgram = Program(0L, 0L, 0L, emptyList()).apply { instructionPointer = originalInstructionPointer }
            val program = originalProgram.copy().apply { instructionPointer = originalInstructionPointer }
            val ignored = 4
            val instruction: Instruction = Jnz

            instruction.execute(ignored, program)

            assert(program == originalProgram)
            assert(program.instructionPointer == originalInstructionPointer)
        }

        @Test
        fun `jnz should set the instruction pointer using its literal operand if the A register is not zero`() {
            val program = Program(1L, 0L, 0L, emptyList()).apply { instructionPointer = 2 }
            val instruction: Instruction = Jnz

            instruction.execute(7, program)

            //lower the instructionPointer by 2 so when it's increased by later we get the right result
            assert(program.instructionPointer == 5)
        }

        @Test
        fun `jnz should set the instruction pointer to a negative number using its literal operand if the A register is not zero and the literal is small enough`() {
            val program = Program(1L, 0L, 0L, emptyList()).apply { instructionPointer = 2 }
            val instruction: Instruction = Jnz

            instruction.execute(1, program)

            //lower the instructionPointer by 2 so when it's increased by later we get the right result
            assert(program.instructionPointer == -1)
        }

        @Test
        fun `jnz should use LiteralOperand`() {
            assert(Jnz.operand == LiteralOperand)
        }

        @Test
        fun `bxc should calculate the bitwise XOR of register B and register C, and store in register B`() {
            //For legacy reasons, this instruction reads an operand but ignores it
            val program = Program(0L, 7L, 6L, emptyList())

            val instruction: Instruction = Bxc

            instruction.execute(1, program)

            assert(program.registerB == 1L)
        }

        @Test
        fun `out should calculate the value of its combo operand modulo 8, and add to outputs`() {
            val program = Program(87L, 0L, 0L, emptyList())
            program.outputs.removeAll { true }

            val instruction: Instruction = Out
            instruction.execute(4, program)

            assert(program.outputs == listOf(7))
        }

        @Test
        fun `out should use ComboOperand`() {
            assert(Out.operand == ComboOperand)
        }

        @Test
        fun `bdv should divide register A by 2 ^ combo operand, truncate to an integer, and store in register B`() {
            val program = Program(100L, 0L, 0L, emptyList())

            val instruction: Instruction = Bdv

            instruction.execute(2, program)

            assert(program.registerB == 25L)
        }

        @Test
        fun `bdv should use ComboOperand`() {
            assert(Bdv.operand == ComboOperand)
        }

        @Test
        fun `cdv should divide register A by 2 ^ combo operand, truncate to an integer, and store in register C`() {
            val program = Program(100L, 0L, 0L, emptyList())

            val instruction: Instruction = Cdv

            instruction.execute(2, program)

            assert(program.registerC == 25L)
        }

        @Test
        fun `cdv should use ComboOperand`() {
            assert(Cdv.operand == ComboOperand)
        }
    }

    @Test
    fun `the instructionsByOpCode array should have each instruction in a position related to its opcode`() {
        assert(instructionsByOpCode.size == 8)
        assert(instructionsByOpCode[0] == Adv)
        assert(instructionsByOpCode[1] == Bxl)
        assert(instructionsByOpCode[2] == Bst)
        assert(instructionsByOpCode[3] == Jnz)
        assert(instructionsByOpCode[4] == Bxc)
        assert(instructionsByOpCode[5] == Out)
        assert(instructionsByOpCode[6] == Bdv)
        assert(instructionsByOpCode[7] == Cdv)
    }

    @Test
    fun `part1 should execute the sample program and return its output`() {
        val expectedResult = "4,6,3,5,6,3,5,2,1,0"
        assert(solution.part1(sampleInput) == expectedResult)
    }
}