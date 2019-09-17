package org.kwrench.random

/**
 * MurmurHash3 implementation for a single 64 bit value.
 * (https://code.google.com/p/smhasher/wiki/MurmurHash3)
 */
class MurmurHash3 : Hash {

    override fun hash(input: Long): Long {
        var value = input

        // If the input is zero, replace it with an arbitrary non-zero constant
        if (value == 0L) value = SEED_TO_USE_INSTEAD_OF_ZERO

        value = value xor value.ushr(33)
        value *= MurmurHashConstants.CONSTANT_A
        value = value xor value.ushr(33)
        value *= MurmurHashConstants.CONSTANT_B
        return value xor value.ushr(33)
    }

    companion object {
        /**
         * Just an arbitrary value to use instead of a zero seed, as the hash function doesn't work with zero seeds.
         */
        private val SEED_TO_USE_INSTEAD_OF_ZERO = 9837421349L
    }
}
