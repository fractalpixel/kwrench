package org.kwrench.generator.table

import org.kwrench.generator.Generator
import org.kwrench.generator.GeneratorBase
import org.kwrench.random.Rand
import org.kwrench.symbol.Symbol
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.math.max
import kotlin.reflect.KClass

/**
 * A [Generator] that selects one of the contained generators based on their weight and returns its result.
 * At least one entry must be added to the entries before calling [generate].
 * The relative weights for the entries are calculated by the [selectionStrategy].  By default all entries are
 * equally probable with a weight of 1.  Entries may have a weight assigned manually, if not, the [selectionStrategy]
 * calculates a weight for them based on their position in the table.
 */
// TODO: Determine type based on contents?
class TableGenerator<T: Any>(override val type: KClass<T>,
                             var selectionStrategy: TableSelectionStrategy = FlatSelectionStrategy,
                             vararg initialEntries: Generator<T>
): GeneratorBase<T>() {

    private val editableEntries = ArrayList<Entry<T>>()

    init {
        // Add initial entries.
        for (initialEntry in initialEntries) {
            add(initialEntry)
        }
    }

    /**
     * Number of entries.
     */
    val entryCount: Int get() = editableEntries.size

    /**
     * Weight of the entry with the specified index.
     */
    fun getWeight(index: Int): Double {
        if (index < 0 || index >= entryCount) throw IllegalArgumentException("Index out of bounds: $index, expected value in range 0 (inclusive) to $entryCount (exclusive)")

        val weight = editableEntries[index].weight ?: selectionStrategy.calculateWeight(index, entryCount)
        return max(weight, 0.0) // Clamp negative weights to zero
    }

    /**
     * @returns the generator and custom weight at the specified index.
     */
    operator fun get(index: Int): Entry<T> {
        return editableEntries[index]
    }

    /**
     * Update the generator and custom weight at the specified index.
     */
    operator fun set(index: Int, entry: Entry<T>) {
        editableEntries[index] = entry
    }

    /**
     * Update the generator and custom weight at the specified index.
     */
    fun set(index: Int, generator: Generator<T>, weight: Double? = null) {
        set(index, Entry(generator, weight))
    }

    /**
     * Add new entry to the end.
     */
    fun add(generator: Generator<T>, weight: Double? = null) {
        add(Entry(generator, weight))
    }

    /**
     * Add new entry to the end.
     */
    fun add(entry: Entry<T>) {
        editableEntries.add(entry)
    }

    /**
     * Insert new entry at the specified index.
     */
    fun add(index: Int, generator: Generator<T>, weight: Double? = null) {
        add(index, Entry(generator, weight))
    }

    /**
     * Insert new entry at the specified index.
     */
    fun add(index: Int, entry: Entry<T>) {
        editableEntries.add(index, entry)
    }

    /**
     * Remove entry at the specified index.
     */
    fun remove(index: Int) {
        editableEntries.removeAt(index)
    }

    /**
     * List of entries.
     */
    val entries: List<Entry<T>> get() = editableEntries


    override fun generate(random: Rand, parameters: Map<Symbol, Any>?): T {
        if (editableEntries.isEmpty()) throw IllegalStateException("No editableEntries have been added to the TableGenerator, can not randomize an entry.")

        // Sum weights
        var sum = 0.0
        for (i in 0 until entryCount) {
            sum += getWeight(i)
        }

        // Randomize
        val selection = random.nextDouble(sum)

        // Find
        var selectedIndex = 0
        var weightSoFar = 0.0
        for (i in 0 until entryCount) {
            weightSoFar += getWeight(i)

            if (selection <= weightSoFar) {
                selectedIndex = i
                break
            }
        }

        // Get generator
        val randomGenerator: Generator<T> = get(selectedIndex).generator

        // Generate
        return randomGenerator.generate(random, parameters)
    }

    /**
     * Used for storing generators that may have a custom weight assigned.
     * If no weight is assigned, the [selectionStrategy] of the table is used to calculate the weight for the entry,
     * based on its position.
     */
    data class Entry<T: Any>(var generator: Generator<T>, var weight: Double? = null)
}