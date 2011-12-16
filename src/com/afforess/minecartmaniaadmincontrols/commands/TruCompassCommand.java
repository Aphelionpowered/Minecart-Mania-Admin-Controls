package com.afforess.minecartmaniaadmincontrols.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.afforess.minecartmaniacore.utils.DirectionUtils;

public class TruCompassCommand extends MinecartManiaCommand {
    
    public boolean isPlayerOnly() {
        return true;
    }
    
    public CommandType getCommand() {
        return CommandType.TruCompass;
    }
    
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        DirectionUtils.CompassDirection facingDir = DirectionUtils.getDirectionFromRotation((((Player) sender).getLocation().getYaw() - 90.0F));
        sender.sendMessage(ChatColor.YELLOW + facingDir.toString());
        return true;
    }
    
}
