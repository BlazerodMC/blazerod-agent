package com.blazerod.agent.tasks

import com.blazerod.agent.Plugin
import org.bukkit.scheduler.BukkitRunnable

class PeriodicTPSStatusTask(val plugin: Plugin) : BukkitRunnable() {
    override fun run() {
        plugin.messages.sendTPSStatus()
    }
}
