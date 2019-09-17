package org.kwrench.updating

import org.kwrench.time.Time
import org.kwrench.updating.strategies.ChainedStrategy
import org.kwrench.updating.strategies.UpdateStrategy

/**
 * An Updating simulation, along with an UpdateStrategy that is used for it.
 * @param simulation simulation to build. If null, does noting
 * @param updateStrategy strategy to use when updating, or null to not use any special strategy,
 * *                       just build the simulation directly with the provided time.
 */
class Updater(var simulation: Updating?, updateStrategy: UpdateStrategy? = null) : UpdatingWithStrategy(updateStrategy) {

    /**
     * Create a UpdatingAndStrategy with two or more build strategies, applied in reverse order (last added strategies are executed first).
     * @param simulation simulation to build
     */
    constructor(simulation: Updating,
                firstUpdateStrategy: UpdateStrategy,
                secondUpdateStrategy: UpdateStrategy,
                vararg additionalUpdateStrategies: UpdateStrategy
    ) : this(simulation, ChainedStrategy(firstUpdateStrategy, secondUpdateStrategy, *additionalUpdateStrategies)) {}

    override protected fun doUpdate(time: Time) {
        simulation?.update(time)
    }

}
