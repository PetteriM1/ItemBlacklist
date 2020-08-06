package me.petterim1.itemblacklist;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;

public class BlockInventoryTransactions implements Listener {

    @EventHandler
    public void onInventoryTransaction(InventoryTransactionEvent e) {
        for (InventoryAction action : e.getTransaction().getActionList()) {
            Item source = action.getSourceItem();
            String s = source != null ? (source.getId() + ":" + source.getDamage()) : "";
            Item target = action.getTargetItem();
            String t = target != null ? (target.getId() + ":" + target.getDamage()) : "";
            if (Plugin.blacklist.contains(s) || Plugin.blacklist.contains(t)) {
                e.setCancelled(true);
            }
        }
    }
}
