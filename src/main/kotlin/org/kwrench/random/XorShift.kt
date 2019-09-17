package org.kwrench.random

/**
 * XorShift random number generator.
 *
 * Uses the xorshift variant xorshift128+ described in http://xorshift.di.unimi.it/
 * See http://xorshift.di.unimi.it/xorshift128plus.c for original c code.
 */
class XorShift(seed: Long = System.nanoTime()) : BaseRand(seed) {

    // Random number generator state (2 * 64 bits)
    private var seed0: Long = 0
    private var seed1: Long = 0

    override fun setHashedSeed(seed: Long) {

        seed0 = seed

        // Derive second seed from the original using a hash function
        seed1 = seedHasher.hash(seedHasher.hash(seed))

        // Replace zero seeds with arbitrary seeds
        if (seed0 == 0L) seed0 = 9045324987L
        if (seed1 == 0L) seed1 = 3912353188L
    }

    override fun nextLong(): Long {
        var s1 = seed0
        val s0 = seed1

        seed0 = s0
        s1 = s1 xor (s1 shl 23)
        seed1 = s1 xor s0 xor s1.ushr(17) xor s0.ushr(26)
        return seed1 + s0
    }

    override fun nextRand(): Rand {
        return XorShift(nextLong())
    }

    companion object {
        private val seedHasher = MurmurHash3()
    }

}
