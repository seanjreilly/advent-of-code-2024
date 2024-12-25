package utils

/**
 * A chunk of input lines, separated by blank lines
 */
class Chunk(val lines: List<String>): Iterable<String> {
    /**
     * Switch columns to rows
     */
    fun pivot(): Chunk {
        val columns = List(lines.first().length) { StringBuilder() }
        lines.forEach { line -> line.forEachIndexed { int, char -> columns[int].append(char) } }
        return Chunk(columns.map { it.toString() })
    }

    override fun iterator(): Iterator<String> = lines.iterator()
}

fun List<String>.toChunks() : Sequence<Chunk> {
    var list = this
    return sequence {
        var chunkInProgress = mutableListOf<String>()
        list.forEach { line ->
            if (line.isNotBlank()) {
                chunkInProgress += line
            } else {
                yield(Chunk(chunkInProgress))
                chunkInProgress = mutableListOf<String>()
            }
        }
        if (chunkInProgress.isNotEmpty()) {
            yield(Chunk(chunkInProgress))
        }
    }
}