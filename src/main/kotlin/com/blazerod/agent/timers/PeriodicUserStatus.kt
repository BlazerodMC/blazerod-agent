package com.blazerod.agent.timers

import com.blazerod.agent.Plugin
import org.bukkit.scheduler.BukkitRunnable

class PeriodicUserStatus(val plugin: Plugin) : BukkitRunnable() {
    override fun run() {
        plugin.sendPlayerStatus()
    }
}
