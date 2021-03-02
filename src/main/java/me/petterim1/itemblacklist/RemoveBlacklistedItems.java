package me.petterim1.itemblacklist;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerItemHeldEvent;

public class RemoveBlacklistedItems implements Listener {

    @EventHandler
    public void onHeld(PlayerItemHeldEvent e) {
        if (e.getItem() != null && (Plugin.blacklist.contains(e.getItem().getId() + ":" + e.getItem().getDamage()) || Plugin.blacklist.contains(e.getItem().getId() + ":*"))) {
            if (e.getPlayer().hasPermission("itemblacklist.ignore")) return;
            e.getPlayer().getInventory().clear(e.getSlot(), true);
            if (Plugin.message) {
                e.getPlayer().sendMessage(Plugin.blacklistedMessage);
            }
        }
    }
}
