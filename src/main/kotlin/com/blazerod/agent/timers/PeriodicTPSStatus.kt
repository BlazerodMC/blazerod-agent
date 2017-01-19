package com.blazerod.agent.timers

import com.blazerod.agent.Plugin
import org.bukkit.scheduler.BukkitRunnable

class PeriodicTPSStatus(val plugin: Plugin) : BukkitRunnable() {
    override fun run() {
        plugin.sendTPSStatus()
    }
}
