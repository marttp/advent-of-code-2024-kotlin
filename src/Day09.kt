import helper.util.println
import helper.util.readInput

fun main() {
    fun parseDiskMap(line: String): Pair<List<Int>, List<Int>> {
        val files = mutableListOf<Int>()
        val spaces = mutableListOf<Int>()
        line.forEachIndexed { i, c ->
            if (i % 2 == 0) files.add(c.digitToInt())
            else spaces.add(c.digitToInt())
        }
        return files to spaces
    }

    fun createDisk(files: List<Int>, spaces: List<Int>): List<Int?> {
        val disk = mutableListOf<Int?>()
        files.forEachIndexed { fileId, fileSize ->
            repeat(fileSize) { disk.add(fileId) }
            if (fileId < spaces.size) {
                repeat(spaces[fileId]) { disk.add(null) }
            }
        }
        return disk
    }

    fun part1(input: List<String>): Long {
        val (files, spaces) = parseDiskMap(input[0])
        val disk = createDisk(files, spaces).toMutableList()

        for (i in disk.indices.reversed()) {
            val fileId = disk[i] ?: continue
            val targetIdx = disk.subList(0, i).indexOfFirst { it == null }
            if (targetIdx != -1) {
                disk[targetIdx] = fileId
                disk[i] = null
            }
        }

        return disk.mapIndexed { index, fileId ->
            if (fileId != null) index.toLong() * fileId.toLong() else 0L
        }.sum()
    }

    fun part2(input: List<String>): Long {
        val (files, spaces) = parseDiskMap(input[0])
        val disk = createDisk(files, spaces).toMutableList()

        data class FileSpan(val id: Int, val start: Int, val length: Int)

        fun getFileSpans(): List<FileSpan> {
            val spans = mutableListOf<FileSpan>()
            var start = 0
            var currentId: Int? = null
            var length = 0

            disk.forEachIndexed { i, blockId ->
                when {
                    currentId == null && blockId != null -> {
                        currentId = blockId
                        start = i
                        length = 1
                    }
                    currentId == blockId && blockId != null -> length++
                    currentId != null && currentId != blockId -> {
                        spans.add(FileSpan(currentId!!, start, length))
                        currentId = blockId
                        start = i
                        length = if (blockId != null) 1 else 0
                    }
                }
            }
            currentId?.let { spans.add(FileSpan(it, start, length)) }
            return spans
        }

        fun findLeftmostSpace(requiredLen: Int): Int? {
            var currentSpace = 0
            var spaceStart: Int? = null

            disk.forEachIndexed { i, blockId ->
                if (blockId == null) {
                    if (spaceStart == null) spaceStart = i
                    currentSpace++
                    if (currentSpace >= requiredLen) return spaceStart
                } else {
                    currentSpace = 0
                    spaceStart = null
                }
            }
            return null
        }

        getFileSpans()
            .sortedByDescending { it.id }
            .forEach { (id, start, length) ->
                findLeftmostSpace(length)?.let { target ->
                    if (target < start) {
                        repeat(length) { disk[start + it] = null }
                        repeat(length) { disk[target + it] = id }
                    }
                }
            }

        return disk.mapIndexed { index, fileId ->
            if (fileId != null) index.toLong() * fileId.toLong() else 0L
        }.sum()
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}