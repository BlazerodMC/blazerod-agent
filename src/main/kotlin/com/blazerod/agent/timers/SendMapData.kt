package com.blazerod.agent.timers

import com.blazerod.agent.Plugin
import org.bukkit.Chunk
import org.bukkit.scheduler.BukkitRunnable

class SendMapData(val plugin: Plugin): BukkitRunnable() {
    override fun run() {
        var chunk: Chunk
        while(plugin.dirtyChunks.size > 0) {
            chunk = plugin.dirtyChunks.first()
            plugin.dirtyChunks.remove(chunk)
            plugin.sendChunkMap(chunk)
        }
    }
}
