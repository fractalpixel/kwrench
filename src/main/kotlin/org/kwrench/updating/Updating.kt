package org.kwrench.updating

import org.kwrench.time.Time


/**
 * Something that is updated over time.
 */
interface Updating {

    /**
     * @param time current simulation time.  Also contains the duration of the last time step.
     */
    fun update(time: Time)

}