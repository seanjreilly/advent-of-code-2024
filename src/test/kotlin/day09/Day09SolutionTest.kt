package day09

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

class Day09SolutionTest {
    private val sampleInput = """
        2333133121414131402
        00...111...2...333.44.5555.6666.777.888899
        """.trimIndent().lines()

    private val tinySampleInput = """
        12345
    """.trimIndent().lines()

    private val solution = Day09Solution()

    @Nested
    inner class DiskLayoutTest {
        @Test
        fun `constructor should return the DiskLayout associated with the disk map`() {
            val diskMap = tinySampleInput.first()

            val expectedFiles = listOf(
                File(0, 1),
                File(1, 3),
                File(2, 5)
            )

            val expectedFileLayout = mapOf(
                0 to expectedFiles[0],
                3 to expectedFiles[1],
                4 to expectedFiles[1],
                5 to expectedFiles[1],
                10 to expectedFiles[2],
                11 to expectedFiles[2],
                12 to expectedFiles[2],
                13 to expectedFiles[2],
                14 to expectedFiles[2],
            )

            val expectedFreeSpace = listOf(
                1,2,6,7,8,9
            )

            val layout = DiskLayout(diskMap)

            assert(layout.fileLayout == expectedFileLayout)
            assert(layout.freeSpace == expectedFreeSpace)
        }

        @Test
        fun `freeSpace should be in sorted order`() {
            val diskLayout = DiskLayout(tinySampleInput.first())

            assert(diskLayout.freeSpace == diskLayout.freeSpace.sorted())
        }

        @Test
        fun `defrag should move ending file blocks into earlier available free spaces`() {
            val layout = DiskLayout(sampleInput.first())

            val updatedFileLayout: NavigableMap<Int, File> = layout.defrag()

            val expectedRawResult = "0099811188827773336446555566"
            assert(updatedFileLayout.size == expectedRawResult.length)
            expectedRawResult.forEachIndexed { index, char ->
                val expectedFileId = char.digitToInt()
                assert(updatedFileLayout[index]!!.id == expectedFileId)
            }

        }

        @Test
        fun `defragPart2 should move entire files into free space big enough to fit them`() {
            val layout = DiskLayout(sampleInput.first())

            val updatedFileLayout: NavigableMap<Int, File> = layout.defragPart2()

            val expectedRawResult = "00992111777.44.333....5555.6666.....8888.."
            val expectedResult = expectedRawResult.withIndex().filter { it.value != '.' }

            assert(updatedFileLayout.size == expectedResult.size)
            expectedResult.forEach { (index, char) ->
                val expectedFileId = char.digitToInt()
                assert(updatedFileLayout[index]!!.id == expectedFileId)
            }
        }
    }

    @Test
    fun `checksum should return the sum of each allocated block's position times the id of the file in the block`() {
        val fileLayout : SortedMap<Int, File> = DiskLayout(sampleInput.first()).defrag()
        val expectedChecksum = 1928L

        val checksum: Long = fileLayout.checksum()

        assert(checksum == expectedChecksum)
    }

    @Test
    fun `part1 should parse the disk layout, defrag the disk, and return the checksum of the defragged disk`() {
        assert(solution.part1(sampleInput) == 1928L)
    }

    @Test
    fun `part1 should return a positive number with production input`() {
        assert(solution.part1(solution.readInput()) > 0L)
    }

    @Test
    fun `part2 should parse the disk layout, defrag the disk with the part2 approach, and return the checksum of the defragged disk`() {
        assert(solution.part2(sampleInput) == 2858L)
    }

    @Test
    fun `production input is a single line`() {
        assert(solution.readInput().size == 1)
    }
}