package day09

import utils.LongSolution
import java.util.NavigableMap
import java.util.PriorityQueue
import java.util.TreeMap
import java.util.TreeSet
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

fun main() = Day09Solution().run()
class Day09Solution : LongSolution() {
    override fun part1(input: List<String>) = DiskLayout(input.first()).defrag().checksum()

    override fun part2(input: List<String>) = DiskLayout(input.first()).defragPart2().checksum()
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

    fun defragPart2(): NavigableMap<Int, File> {
        val unmovedFilesByFileId = buildListOfFilesById()
        val freeChunks = buildFreeTrunkTree()

        unmovedFilesByFileId.forEach { (file, fileStartingPosition) ->

            val chunk = freeChunks
                .firstOrNull { it.size >= file.blockSize && it.startingPosition < fileStartingPosition }
            if (chunk == null) {
                return@forEach
            }

            freeChunks -= chunk
            repeat(file.blockSize) { int ->
                fileLayout[chunk.startingPosition + int] = file
                fileLayout.remove(fileStartingPosition + int)
            }

            //put the remaining portion of this free chunk (if any) back in the tree of free chunks
            val remainingFreeSpace = FreeChunk(chunk.startingPosition + file.blockSize, chunk.size - file.blockSize)
            if (remainingFreeSpace.size > 0) {
                freeChunks.add(remainingFreeSpace)
            }
        }

        return fileLayout
    }

    /**
     * build a treeset of freechunks ordered by starting position
     */
    private fun buildFreeTrunkTree(): TreeSet<FreeChunk> {
        val freeChunkComparator: Comparator<FreeChunk> = compareBy({ it.startingPosition })
        val freeChunks = TreeSet<FreeChunk>(freeChunkComparator)

        var startOfCurrentFreeChunk = freeSpace.first()
        var lastFreeBlockProcessed = freeSpace.first()
        var currentFreeChunkSize = 1

        freeSpace.drop(1).forEach {
            if (it > lastFreeBlockProcessed + 1) {
                // store the last block and add a new one
                freeChunks.add(FreeChunk(startOfCurrentFreeChunk, currentFreeChunkSize))

                startOfCurrentFreeChunk = it
                currentFreeChunkSize = 0
            }
            lastFreeBlockProcessed = it
            currentFreeChunkSize++
        }
        freeChunks.add(FreeChunk(startOfCurrentFreeChunk, currentFreeChunkSize)) //store final freeChunk
        return freeChunks
    }

    /**
     * build a list of unmoved files by id (descending)
     */
    private fun buildListOfFilesById(): List<Pair<File, Int>> {
        //the original list is sorted so we don't need to sort
        return fileLayout
            .entries
            .groupBy { it.value }
            .mapValues { it.value.minOf { entry -> entry.key } }
            .toList()
            .map { (file, startingPosition) -> file to startingPosition }
            .reversed()
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
private data class FreeChunk(val startingPosition: Int, val size: Int)