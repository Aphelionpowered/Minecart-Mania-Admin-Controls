package com.afforess.minecartmaniaadmincontrols.commands;

import java.util.ArrayList;

import net.minecraft.server.Packet;
import net.minecraft.server.Packet23VehicleSpawn;
import net.minecraft.server.Packet29DestroyEntity;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftMinecart;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class RedrawMinecartCommand extends MinecartManiaCommand {
    
    public boolean isPlayerOnly() {
        return false;
    }
    
    public CommandType getCommand() {
        return CommandType.Hide;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final Player[] online = Bukkit.getServer().getOnlinePlayers();
        final ArrayList<MinecartManiaMinecart> minecarts = MinecartManiaWorld.getMinecartManiaMinecartList();
        for (final Player p : online) {
            final CraftPlayer player = (CraftPlayer) p;
            for (final MinecartManiaMinecart minecart : minecarts) {
                final Entity passenger = minecart.minecart.getPassenger();
                minecart.minecart.eject();
                Packet packet = new Packet29DestroyEntity(minecart.minecart.getEntityId());
                player.getHandle().netServerHandler.sendPacket(packet);
                packet = null;
                final Vector motion = minecart.minecart.getVelocity();
                minecart.stopCart();
                if (minecart.isStandardMinecart()) {
                    packet = new Packet23VehicleSpawn(((CraftMinecart) minecart.minecart).getHandle(), 10);
                } else if (minecart.isPoweredMinecart()) {
                    packet = new Packet23VehicleSpawn(((CraftMinecart) minecart.minecart).getHandle(), 12);
                } else if (minecart.isStorageMinecart()) {
                    packet = new Packet23VehicleSpawn(((CraftMinecart) minecart.minecart).getHandle(), 11);
                }
                player.getHandle().netServerHandler.sendPacket(packet);
                if (passenger != null) {
                    final Runnable update = new Runnable() {
                        public void run() {
                            minecart.minecart.setVelocity(motion);
                            minecart.minecart.setPassenger(passenger);
                        }
                    };
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MinecartManiaCore.getInstance(), update);
                    
                }
            }
        }
        sender.sendMessage(LocaleParser.getTextKey("AdminControlsRedrawMinecarts"));
        return true;
    }
    
}
