package day09

import utils.LongSolution
import java.util.*

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
        val freeChunks = buildFreeChunks()

        unmovedFilesByFileId.forEach { (file, fileStartingPosition) ->

            val qualifyingSecondaryIndices = freeChunks
                .tailMap(file.blockSize, true)
                .filterValues { it.isNotEmpty() }
                .values
            if (qualifyingSecondaryIndices.isEmpty()) {
                return@forEach
            }

            val bestSecondaryIndex: PriorityQueue<FreeChunk> = qualifyingSecondaryIndices.minBy { it.first().startingPosition }
            if (bestSecondaryIndex.peek().startingPosition > fileStartingPosition) {
                //the earliest chunk that's big enough is to the right of the file's current position
                return@forEach
            }

            val chunk = bestSecondaryIndex.poll()
            repeat(file.blockSize) { int ->
                fileLayout[chunk.startingPosition + int] = file
                fileLayout.remove(fileStartingPosition + int)
            }

            //put the remaining portion of this free chunk (if any) back in the tree of free chunks
            val remainingFreeSpace = FreeChunk(chunk.startingPosition + file.blockSize, chunk.size - file.blockSize)
            if (remainingFreeSpace.size > 0) {
                freeChunks.store(remainingFreeSpace)
            }
        }

        return fileLayout
    }

    /**
     * build an index of free chunks ordered by starting position
     */
    private fun buildFreeChunks(): TreeMap<Int, PriorityQueue<FreeChunk>> {
        val freeChunks = TreeMap<Int, PriorityQueue<FreeChunk>>()

        var startOfCurrentFreeChunk = freeSpace.first()
        var lastFreeBlockProcessed = freeSpace.first()
        var currentFreeChunkSize = 1

        freeSpace.drop(1).forEach {
            if (it > lastFreeBlockProcessed + 1) {
                // store the last block and add a new one
                freeChunks.store(FreeChunk(startOfCurrentFreeChunk, currentFreeChunkSize))

                startOfCurrentFreeChunk = it
                currentFreeChunkSize = 0
            }
            lastFreeBlockProcessed = it
            currentFreeChunkSize++
        }
        freeChunks.store(FreeChunk(startOfCurrentFreeChunk, currentFreeChunkSize)) //store final freeChunk
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

private val comparator: Comparator<FreeChunk> = compareBy { it.startingPosition }
private fun TreeMap<Int, PriorityQueue<FreeChunk>>.store(chunk: FreeChunk) {
    val secondaryIndex = this.computeIfAbsent(chunk.size) { PriorityQueue<FreeChunk>(comparator) }
    secondaryIndex += chunk
}

internal data class File(val id: Int, val blockSize: Int)
private data class FreeChunk(val startingPosition: Int, val size: Int)