package com.ruinscraft.panilla.bukkit;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.exception.PacketException;
import com.ruinscraft.panilla.api.io.PacketRateLimiter;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class BukkitPanillaPlayer implements IPanillaPlayer {

    private final Player handle;

    private Set<PacketRateLimiter> packetRateLimiters;

    public BukkitPanillaPlayer(Player handle) {
        this.handle = handle;
        packetRateLimiters = new HashSet<>();
    }

    @Override
    public Player getHandle() {
        return handle;
    }

    @Override
    public String getName() {
        return handle.getName();
    }

    @Override
    public String getCurrentWorldName() {
        return handle.getWorld().getName();
    }

    @Override
    public boolean hasPermission(String node) {
        return handle.hasPermission(node);
    }

    @Override
    public boolean canBypassChecks(IPanilla panilla, PacketException e) {
        if (e.getFailedNbt().result == NbtCheck.NbtCheckResult.CRITICAL) {
            return false;   // to prevent crash exploits
        }

        boolean inDisabledWorld = panilla.getPConfig().disabledWorlds.contains(getCurrentWorldName());

        return inDisabledWorld || hasPermission(PConfig.PERMISSION_BYPASS);
    }

    @Override
    public Set<PacketRateLimiter> getRateLimiters() {
        return packetRateLimiters;
    }

    @Override
    public void kick(String message) {
        handle.kickPlayer(message);
    }

}
