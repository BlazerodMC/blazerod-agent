package com.blazerod.agent

import com.blazerod.agent.tasks.*
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Plugin : JavaPlugin() {
    var API_KEY = ""

    val chunkHandler = ChunkHandler(this)
    val messages = Messages(this)

    override fun onEnable() {
        loadConfiguration()

        server.pluginManager.registerEvents(EventListener(this), this)

        FlushPendingMessagesTask(this).runTaskTimerAsynchronously(this, 1L, 100L)
        PeriodicSystemStats(this).runTaskTimer(this, 1L, 20 * 5)
        PeriodicTPSStatusTask(this).runTaskTimer(this, 1L, 20 * 5)
        PeriodicUserStatusTask(this).runTaskTimer(this, 1L, 20 * 5)
        DirtyChunksTask(this).runTaskTimer(this, 0L, 100L)
    }

    private fun loadConfiguration() {
        val configFile = File(dataFolder, "config.yml")
        if (!configFile.exists()) {
            config.options().copyDefaults(true)
            saveConfig()
        }

        reloadConfig()

        API_KEY = config.getString("api-key", "")

        if (API_KEY == "API_KEY") API_KEY = ""
    }
}
