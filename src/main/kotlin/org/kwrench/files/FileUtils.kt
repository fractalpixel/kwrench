package org.kwrench.files

import java.io.File
import java.io.RandomAccessFile
import java.util.*

/**
 * Visit files matching the file filter under this directory and subdirectories matching the directory filter.
 * @param visitor method called for each matching file.  Takes the file and the parent path (as a list of directories) as parameters.
 */
fun File.recursivelyIterateFiles(visitor: (file: File, parentPath: List<File>) -> Unit,
                                 fileFilter: (File) -> Boolean,
                                 directoryFilter: (File) -> Boolean = { true },
                                 parentPath: List<File> = emptyList(),
                                 includeThisDirectoryInPath: Boolean = false) {

    if (this.isFile && fileFilter(this)) visitor(this, parentPath)
    else if (this.isDirectory && directoryFilter(this)) {
        val path = ArrayList(parentPath)
        if (includeThisDirectoryInPath) path.add(this)
        for (file in this.listFiles()) {
            file.recursivelyIterateFiles(visitor, fileFilter, directoryFilter, path, true)
        }
    }
}


/**
 * Lists files matching the file filter under this directory and subdirectories matching the directory filter.
 * @return list with pairs of subdirectory paths and files.  For files directly under this path the subdirectory path will be empty.
 */
fun File.recursivelyListFiles(fileFilter: (File) -> Boolean,
                              directoryFilter: (File) -> Boolean = { true },
                              outputList: MutableList<Pair<List<File>, File>> = java.util.ArrayList(),
                              parentPath: List<File> = emptyList(),
                              includeThisDirectoryInPath: Boolean = false): List<Pair<List<File>, File>> {

    if (this.isFile && fileFilter(this)) outputList.add(Pair(parentPath, this))
    else if (this.isDirectory && directoryFilter(this)) {
        val path = ArrayList(parentPath)
        if (includeThisDirectoryInPath) path.add(this)
        for (file in this.listFiles()) {
            file.recursivelyListFiles(fileFilter, directoryFilter, outputList, path, true)
        }
    }

    return outputList
}

/**
 * Creates a map with file paths being converted to keys, and files being converted to values.
 * @param fileFilter takes the path of the file, plus the file itself, and returns a key to use in the map for the value generated for the file.
 */
fun <K, V> File.recursivelyMapFiles(fileFilter: (File) -> Boolean,
                                    directoryFilter: (File) -> Boolean = {true},
                                    keyConverter: (List<File>) -> K,
                                    valueConverter: (File) -> V): MutableMap<K, V> {

    val map = LinkedHashMap<K, V>()

    val files = this.recursivelyListFiles(fileFilter, directoryFilter)
    for ((path, file) in files) {
        val p = ArrayList(path)
        p.add(file)
        val key = keyConverter(p)
        val value = valueConverter(file)
        map.put(key, value)
    }

    return map
}
