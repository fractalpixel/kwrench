package org.kwrench.updating.strategies

import org.kwrench.time.Time
import org.kwrench.updating.Updater
import org.kwrench.updating.Updating


/**
 * Contains zero or more build strategies that are executed in reverse order of addition (first added strategy is executed last).
 * @param initialStrategies initial strategies to add.
 *                          The strategies are executed before existing strategies, in reverse order (last added strategy is executed first).
 */
// TODO: Write test or investigate why broken.
@Deprecated("Currently broken?")
class ChainedStrategy(vararg initialStrategies: UpdateStrategy) : UpdateStrategy {

    private var lastUpdating: Updater? = null

    private val terminalUpdating = object : ThreadLocal<Updater>() {
        override fun initialValue(): Updater {
            return Updater(null)
        }
    }

    init {
        lastUpdating = terminalUpdating.get()
        addStrategies(*initialStrategies)
    }

    /**
     * @param updateStrategies strategies to add.  The strategies are executed before existing strategies, in reverse order (last added strategy is executed first).
     */
    fun addStrategies(vararg updateStrategies: UpdateStrategy) {
        for (updateStrategy in updateStrategies) {
            addStrategy(updateStrategy)
        }
    }

    /**
     * @param updateStrategies strategies to add.  The strategies are executed before existing strategies, in reverse order (last added strategy is executed first).
     */
    fun addStrategies(updateStrategies: Collection<UpdateStrategy>) {
        for (updateStrategy in updateStrategies) {
            addStrategy(updateStrategy)
        }
    }

    /**
     * @param updateStrategy strategy to add.  The strategy is executed before existing strategies (last added strategy is executed first).
     */
    fun addStrategy(updateStrategy: UpdateStrategy) {
        lastUpdating = Updater(lastUpdating, updateStrategy)
    }

    /**
     * Removes all strategies from this chained strategies.
     */
    fun removeAllStrategies() {
        lastUpdating = terminalUpdating.get()
    }

    override fun update(simulation: Updating, externalTime: Time) {
        terminalUpdating.get().simulation = simulation
        lastUpdating!!.update(externalTime)
    }
}
