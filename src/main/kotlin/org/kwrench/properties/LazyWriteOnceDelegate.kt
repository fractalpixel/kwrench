package org.kwrench.properties

import kotlin.reflect.KProperty

/**
 * A property delegate that allows writing the value only once, and not reading the value before it has been initialized.
 * Throws IllegalStateException if these invariables are broken.
 * @param onValueInitialized called when the value is set.
 */
class LazyWriteOnceDelegate<T: Any>(private val onValueInitialized: ((T) -> Unit)? = null) {

    private var initialized = false
    private lateinit var value: T

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (!initialized) throw IllegalStateException("Can not read the property ${property.name} before it has been initialized!")
        else return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (initialized) throw IllegalStateException("The property ${property.name} has already been initialized, can not change it anymore!")
        else {
            initialized = true
            this.value = value
            onValueInitialized?.invoke(value)
        }
    }

}