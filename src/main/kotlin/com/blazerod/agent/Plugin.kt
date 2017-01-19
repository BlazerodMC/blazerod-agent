package com.blazerod.agent

import com.blazerod.agent.tasks.*
import org.bukkit.Chunk
import org.bukkit.plugin.java.JavaPlugin
import org.json.simple.JSONObject
import java.time.Instant

class Plugin : JavaPlugin() {
    val dirtyChunks = mutableSetOf<Chunk>()
    val pendingMessages = mutableListOf<JSONObject>()
    var sentMessageCount: Long = 0

    override fun onEnable() {
        server.pluginManager.registerEvents(EventListener(this), this)

        FlushPendingMessages(this).runTaskTimerAsynchronously(this, 1L, 100L)
        PeriodicSystemStats(this).runTaskTimer(this, 1L, 20 * 5)
        PeriodicTPSStatus(this).runTaskTimer(this, 1L, 20 * 5)
        PeriodicUserStatus(this).runTaskTimer(this, 1L, 20 * 5)
        SendMapData(this).runTaskTimer(this, 0L, 100L)
    }

    fun sendEvent(data: JSONObject) {
        sendMessage("event", data)
    }

    fun sendChunkMap(chunk: Chunk) {
        sendMessage("map", Utils.buildChunkMapJSON(chunk))
    }

    fun sendPlayerStatus() {
        if (server.onlinePlayers.isEmpty()) return

        val players: List<JSONObject> = server.onlinePlayers.map {
            val data = JSONObject()
            data["player"] = Utils.buildPlayerJSON(it)
            data["location"] = Utils.buildLocationJSON(it.location)
            data["timestamp"] = Instant.now().epochSecond
            data
        }

        sendMessage("players", players)
    }

    fun sendTPSStatus() {
        sendMessage("tps", Utils.getTPS())
    }

    fun sendSystemStats() {
        sendMessage("system", Utils.buildSystemStatsJSON())
    }

    fun flushPendingMessages() {
        synchronized(pendingMessages) {
            if (pendingMessages.isEmpty()) return

            val messages = pendingMessages.map { it.toJSONString() }.joinToString(separator = "\n")
            logger.info(messages)

            sentMessageCount += pendingMessages.size
            pendingMessages.clear()

            logger.info("Sent $sentMessageCount messages")
        }
    }

    // Private helpers

    private fun sendMessage(type: String, payload: Any?) {
        synchronized(pendingMessages) {
            val data = JSONObject()
            data["type"] = type
            data["payload"] = payload
            pendingMessages.add(data)
        }
    }
}
