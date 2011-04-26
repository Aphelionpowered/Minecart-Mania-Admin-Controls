package com.afforess.minecartmanaiaadmincontrols.permissions;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

import com.afforess.minecartmaniaadmincontrols.MinecartManiaAdminControls;
import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.config.LocaleParser;
import com.afforess.minecartmaniacore.signs.MinecartTypeSign;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.signs.SignManager;

public class PermissionBlockListener extends BlockListener{
	
	@Override
	public void onSignChange(SignChangeEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Sign sign = SignManager.getSignAt(event.getBlock().getLocation());
		Collection<SignAction> actions = sign.getSignActions();
		Iterator<SignAction> i = actions.iterator();
		Player player = event.getPlayer();
		while (i.hasNext()) {
			SignAction action = i.next();
			if (!MinecartManiaAdminControls.permissions.canCreateSign(player, action.getName())) {
				event.setCancelled(true);
				player.sendMessage(LocaleParser.getTextKey("LackPermissionForSign", action.getFriendlyName()));
				SignManager.updateSign(sign.getLocation(), null);
				return;
			}
		}
		
		if (sign instanceof MinecartTypeSign) {
			if (!MinecartManiaAdminControls.permissions.canCreateSign(player, "minecarttypesign")) {
				player.sendMessage(LocaleParser.getTextKey("LackPermissionForSign", "Minecart Type Sign"));
				SignManager.updateSign(sign.getLocation(), null);
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (event.getBlock().getState() instanceof org.bukkit.block.Sign) {
			Sign sign = SignManager.getSignAt(event.getBlock().getLocation());
			Collection<SignAction> actions = sign.getSignActions();
			Iterator<SignAction> i = actions.iterator();
			Player player = event.getPlayer();
			while (i.hasNext()) {
				SignAction action = i.next();
				if (!MinecartManiaAdminControls.permissions.canBreakSign(player, action.getName())) {
					event.setCancelled(true);
					player.sendMessage(LocaleParser.getTextKey("LackPermissionToRemoveSign", action.getFriendlyName()));
					break;
				}
			}
			
			if (sign instanceof MinecartTypeSign) {
				if (!MinecartManiaAdminControls.permissions.canBreakSign(player, "minecarttypesign")) {
					player.sendMessage(LocaleParser.getTextKey("LackPermissionToRemoveSign", "Minecart Type Sign"));
					event.setCancelled(true);
				}
			}
		}
		if (event.isCancelled()) {
			MinecartManiaCore.server.getScheduler().scheduleSyncDelayedTask(MinecartManiaCore.instance, new SignTextUpdater(event.getBlock().getLocation()), 5);
		}
	}
}
