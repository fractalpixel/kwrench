package org.kwrench.collections.ringbuffer

import org.kwrench.collections.BitVector


/**
 * RingBuffer implementation with booleans that uses a backing BitVector.
 * Not thread safe.
 * @param capacity capacity of the ringbuffer.
 */
class BooleanRingBuffer(capacity: Int) : RingBufferBase<Boolean>(capacity, false) {

    private val buffer = BitVector(capacity)

    override fun getBufferElement(index: Int): Boolean = buffer[index]
    override fun setBufferElement(index: Int, value: Boolean) {buffer[index] = value }
}
