package day09

import utils.LongSolution
import java.util.NavigableMap
import java.util.PriorityQueue
import java.util.TreeMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

fun main() = Day09Solution().run()
class Day09Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        return DiskLayout(input.first()).defrag().checksum().toLong()
    }

    override fun part2(input: List<String>) = 0L
}

internal fun Map<Int, File>.checksum() = this.map { (index, file) -> index * file.id }.sumOf { it.toLong() }

internal data class DiskLayout(val fileLayout: NavigableMap<Int, File>, val freeSpace: List<Int>) {
    fun defrag(): NavigableMap<Int, File> {
        val freeSpacePq = PriorityQueue(freeSpace)
        var freeBlock: Int? = freeSpacePq.poll()
        while (freeBlock != null) {
            //find the last file block on the disk
            val (oldIndex, file) = fileLayout.pollLastEntry()
            if (oldIndex < freeBlock) {
                // we would be moving this file further away from the front
                // defrag is finished, put the file back
                fileLayout[oldIndex] = file
                break
            }
            fileLayout[freeBlock] = file
            freeBlock = freeSpacePq.poll()
        }
        return fileLayout
    }

    companion object {
        internal operator fun invoke(input: String): DiskLayout {
            var currentBlock = 0
            val fileLayout = mutableMapOf<Int, File>()
            val freeSpace = mutableListOf<Int>()

            val digitList = input.toCharArray()
                .map { it.digitToInt() }

            digitList
                .windowed(2, 2)
                .withIndex()
                .forEach { (index, list) ->
                    val (fileSize, freeBlocks) = list
                    val file = File(index, fileSize)
                    repeat(fileSize) { fileLayout[currentBlock++] = file }
                    repeat(freeBlocks) { freeSpace += (currentBlock++) }
                }

            if (digitList.size % 2 == 1) {
                //handle the last file at the end
                val fileSize = digitList.last()
                val index = (digitList.size / 2)
                val file = File(index, fileSize)
                repeat(fileSize) { fileLayout[currentBlock++] = file }
            }

            return DiskLayout(TreeMap(fileLayout), freeSpace)
        }
    }
}

internal data class File(val id: Int, val blockSize: Int)