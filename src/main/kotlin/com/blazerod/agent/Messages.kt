package com.blazerod.agent

import org.bukkit.Chunk
import org.json.simple.JSONObject

class Messages(val plugin: Plugin) {
    val pendingMessages = mutableListOf<JSONObject>()
    var sentMessageCount: Long = 0


    fun sendEvent(data: JSONObject) {
        sendMessage("event", data)
    }

    fun sendChunkMap(chunk: Chunk) {
        sendMessage("map", Utils.buildChunkMapJSON(chunk))
    }

    fun sendPlayerStatus() {
        if (plugin.server.onlinePlayers.isEmpty()) return

        val players: List<JSONObject> = plugin.server.onlinePlayers.map {
            val data = JSONObject()
            data["player"] = Utils.buildPlayerJSON(it)
            data["location"] = Utils.buildLocationJSON(it.location)
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
            plugin.logger.info(messages)

            sentMessageCount += pendingMessages.size
            pendingMessages.clear()

            plugin.logger.info("Sent $sentMessageCount messages")
        }
    }

    // Private helpers

    private fun sendMessage(type: String, payload: Any?) {
        synchronized(pendingMessages) {
            val data = JSONObject()
            data["type"] = type
            data["payload"] = payload
            data["timestamp"] = Utils.unixTimestamp()
            pendingMessages.add(data)
        }
    }
}
