package day20

import java.lang.RuntimeException

enum class Pulse {
    Low,
    High
}

abstract sealed class ModuleType() {

    class FlipFlop : ModuleType() {
        var on = false
        override fun receivePulse(source: String, pulse: Pulse): Pulse? {
            return if (pulse == Pulse.Low) {
                on = !on
                if (on) Pulse.High else Pulse.Low
            } else {
                null
            }
        }
    }

    class Conjunction() : ModuleType() {

        val inputs: MutableList<String> = mutableListOf()
        val previousPulses: MutableList<Pulse> = mutableListOf()

        fun addInput(source: String) {
            inputs.add(source)
            previousPulses.add(Pulse.Low)
        }

        override fun receivePulse(source: String, pulse: Pulse): Pulse? {
            val index = inputs.indexOf(source)
            previousPulses[index] = pulse
            return if (previousPulses.all { it == Pulse.High }) {
                Pulse.Low
            }else {
                Pulse.High
            }
        }
    }

    class Broadcaster: ModuleType() {
        override fun receivePulse(source: String, pulse: Pulse): Pulse? = pulse

    }

    abstract fun receivePulse(source: String, pulse: Pulse): Pulse?
}

data class Module(val name: String, val type: ModuleType, val outputs: List<String>)

data class PendingPulse(val source: String, val destination: String, val pulse: Pulse)


fun main() {
    val modules = parse()
    var low = 0L
    var high = 0L
    (1..1000).forEach {
        val signals = pressButton(modules)
        low += signals.first
        high += signals.second
    }
    val mult = low * high
    println("Multiplied signals: $mult")
}

fun pressButton(modules: Map<String, Module>): Pair<Int, Int> {
    var low = 0
    var high = 0
    val pendingPulses = mutableListOf(PendingPulse("","broadcaster", Pulse.Low))
    while(pendingPulses.isNotEmpty()) {
        val signal = pendingPulses.removeFirst()
        if (signal.pulse == Pulse.High) {
            high++
        }else {
            low++
        }
        processSignal(signal, modules, pendingPulses)
    }
    return Pair(low, high)
}

fun processSignal(signal: PendingPulse, modules: Map<String, Module>, pendingPulses: MutableList<PendingPulse>) {
    val module = modules[signal.destination]
    if (module != null) {
        val nextPulse = module.type.receivePulse(signal.source, signal.pulse)
        if (nextPulse != null) {
            module.outputs.forEach { nextModule ->
                pendingPulses.add(PendingPulse(module.name, nextModule, nextPulse))
            }
        }
    }
}

fun parse(): Map<String, Module> {
    val modules = input.lines().map { line ->
        val groups = "[%&]?([a-z]+) -> (.*)".toRegex().matchEntire(line)!!.groupValues
        val name = groups[1]
        val destinations = groups[2].split(",").map { it.trim() }
        val type = if (line[0] == '%') {
            ModuleType.FlipFlop()
        } else if (line[0] == '&') {
            ModuleType.Conjunction()
        } else if (line.startsWith("broadcaster")) {
            ModuleType.Broadcaster()
        } else {
            throw RuntimeException("Unknown type: $line")
        }
        Module(name, type, destinations)
    }.associateBy { it.name }
    modules.values.forEach { mod ->
        mod.outputs.forEach { out ->
            val nextModule = modules[out]
            if (nextModule != null) {
                if (nextModule.type is ModuleType.Conjunction) {
                    nextModule.type.addInput(mod.name)
                }
            }else {
                println("Missing Module: '$out'")
            }
        }
    }
    return modules
}

private val input = """
%vn -> ts, lq
&ks -> dt
%zt -> vl
%xg -> ts, pb
&xd -> qz, bc, zt, vk, hq, qx, gc
&pm -> dt
%gb -> vj, xd
%qx -> gb
%rl -> qn
%lq -> gk
%qm -> bf
%zn -> vh, pf
%lz -> kk, vr
%bf -> rr
%gx -> vr
%zr -> vx, pf
%lt -> ng, vr
%hd -> mg, xd
%mg -> xd
%tx -> jg, vr
%gk -> kx, ts
&vr -> tr, vf, tx, ks, kk, jg
broadcaster -> qz, tx, jr, hk
%bc -> qx
%xz -> lt, vr
%jg -> sb
%qn -> zr, pf
%gc -> xv
%vx -> lj, pf
%vf -> cn
&dt -> rx
%sb -> lz, vr
%kx -> xg
%hk -> pf, tv
%cb -> pf
&dl -> dt
%vl -> xd, bc
%fl -> pp, pf
%ng -> vr, gx
%jr -> ts, qm
%cd -> vn, ts
%mt -> ts
%rr -> ts, cd
%tr -> xz
%hq -> zt
%xv -> hq, xd
%vj -> xd, hd
%pp -> zn
%vh -> pf, cb
%cn -> vr, tr
%kk -> vf
&pf -> pp, tv, rl, pm, hk
&ts -> dl, qm, kx, lq, bf, jr
%tv -> rl
&vk -> dt
%pb -> ts, mt
%lj -> pf, fl
%qz -> xd, gc
""".trimIndent()