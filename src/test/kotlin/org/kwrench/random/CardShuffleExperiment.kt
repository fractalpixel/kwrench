package org.kwrench.random

import org.kwrench.collections.randomElement
import org.kwrench.math.round

// Calculates probability curves for randomizer cards with reshuffle-on-crit mechanics.
// TODO: Not a test, clean away from this codebase...

class Crd(val num: Int,
          val crs: Boolean = false,
          val crf: Boolean = false,
          val reshfl: Boolean = crs || crf)

val cs = ArrayList<Crd>()
val ds = ArrayList<Crd>()
val res = HashMap<Int, Int>()

fun main() {
    cs.add(Crd(-5, crf = true))
    for (i in 1..1) cs.add(Crd(-4))
    for (i in 1..1) cs.add(Crd(-3))
    for (i in 1..2) cs.add(Crd(-2))
    for (i in 1..3) cs.add(Crd(-1))

    for (i in 1..4) cs.add(Crd(0))
    for (i in 1..4) cs.add(Crd(1))

    for (i in 1..3) cs.add(Crd(2))
    for (i in 1..2) cs.add(Crd(3))
    for (i in 1..1) cs.add(Crd(4))
    for (i in 1..1) cs.add(Crd(5))
    cs.add(Crd(6, crs = true))

    println("Num crds ${cs.size}")

    var resfls = 0
    var cardsBetweenReshfls = 0
    var crds = 0
    fun reshfl() {
        cs.addAll(ds)
        ds.clear()
        resfls++
        cardsBetweenReshfls += crds
        crds = 0
    }

    // Test
    val r = Rand.default
    val tests = 1000000
    for (i in 1..tests) {
        crds++
        // Reshfl if needed
        if (cs.isEmpty()) {
            reshfl()
        }

        // Draw
        val c: Crd = cs.randomElement(r)
        cs.remove(c)
        ds.add(c)

        // Count result
        res[c.num] = (res[c.num] ?: 0) + 1

        // Reshfl if needed
        if (c.reshfl) {
            reshfl()
        }
    }

    println("Results: ")
    println("Reshuffles every ${(1.0*cardsBetweenReshfls / resfls).round()} card on average")
    for (num in res.keys.sorted()) {
        val count = res[num] ?: 0
        println("$num:  \t$count \t${(1000 * count / tests).toString().padStart(4)} promille\t" + "*".repeat((100.0 * count / tests).round()))
    }

}
