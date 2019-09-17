package org.kwrench.random;

/**
 * Workaround for very annoying Kotlin bug about negative literal constants ( https://youtrack.jetbrains.com/issue/KT-2780 )
 */
public class MurmurHashConstants {

    public static final long CONSTANT_A = 0xff51afd7ed558ccdL;
    public static final long CONSTANT_B = 0xc4ceb9fe1a85ec53L;

    private MurmurHashConstants() {
    }
}
