package me.petterim1.itemblacklist;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.util.List;

public class Plugin extends PluginBase implements Listener {

    static List<String> blacklist;
    static String blacklistedMessage;
    static boolean message;
    private Config data;

    public void onEnable() {
        saveDefaultConfig();
        saveResource("data.yml");
        blacklistedMessage = getConfig().getString("blacklistedMessage");
        message = !TextFormat.clean(blacklistedMessage).isEmpty();
        data = new Config(getDataFolder() + "/data.yml", Config.YAML);
        blacklist = data.getStringList("blacklist");
        if (getConfig().getBoolean("removeBlacklistedItems")) {
            getServer().getPluginManager().registerEvents(new RemoveBlacklistedItems(), this);
        }
        if (getConfig().getBoolean("blockInventoryTransactions")) {
            getServer().getPluginManager().registerEvents(new BlockInventoryTransactions(), this);
        }
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() != null && blacklist.contains(e.getItem().getId() + ":" + e.getItem().getDamage())) {
            if (e.getPlayer().hasPermission("itemblacklist.ignore")) return;
            e.setCancelled(true);
            if (message) {
                e.getPlayer().sendMessage(blacklistedMessage);
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("blacklist")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    sender.sendMessage("§cBlacklisted items: ");
                    sender.sendMessage(blacklist.toString());
                    return true;
                }
            } else if (args.length == 2) {
                if (args[1].length() < 3 || !args[1].contains(":")) {
                    return false;
                }
                if (args[0].equalsIgnoreCase("add")) {
                    if (blacklist.contains(args[1])) {
                        sender.sendMessage("§aItem " + args[1] + " is already on the blacklist");
                    } else {
                        blacklist.add(args[1]);
                        save();
                        sender.sendMessage("§aAdded " + args[1] + " to blacklist");
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (blacklist.contains(args[1])) {
                        blacklist.remove(args[1]);
                        save();
                        sender.sendMessage("§aRemoved " + args[1] + " from blacklist");
                    } else {
                        sender.sendMessage("§aItem " + args[1] + " is not on the blacklist");
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void save() {
        data.set("blacklist", blacklist);
        data.save();
    }
}
