package me.blake.papiweb;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class PlaceholderWebCommand implements CommandExecutor {

    private final PlaceholderWeb plugin;

    public PlaceholderWebCommand(PlaceholderWeb pl) {
       plugin = pl;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length > 0) {
            if(args[0].equals("generate")) {
                if (sender instanceof Player) {
                    sender.sendMessage(ChatColor.RED + "[PlaceholderWeb] You may only generate new keys from the console!");
                } else {
                    UUID uuid = UUID.randomUUID();
                    List<String> list = plugin.getConfig().getStringList("keys");
                    list.add(uuid.toString());
                    plugin.getConfig().set("keys", list);
                    plugin.saveConfig();
                    sender.sendMessage(ChatColor.GREEN + "[PlaceholderWeb] Key added: " + uuid);
                    plugin.reloadConfig();
                }
                return true;
            } else if (args[0].equals("reload")) {
                plugin.reloadConfig();
                plugin.restartServer();
                sender.sendMessage(ChatColor.GREEN + "[PlaceholderWeb] Reloaded!");
                return true;
            } else if (args[0].equals("debug")) {
                if (plugin.getConfig().getBoolean("debug")) {
                    plugin.getConfig().set("debug", false);
                    sender.sendMessage(ChatColor.YELLOW + "[PlaceholderWeb] Debug mode disabled!");
                } else {
                    plugin.getConfig().set("debug", true);
                    sender.sendMessage(ChatColor.YELLOW + "[PlaceholderWeb] Debug mode enabled!");
                }
                plugin.saveConfig();
                plugin.reloadConfig();
                return true;
            } else if(args.length == 3) {
                if(args[0].equals("generateurl")) {
                    InetAddress ip = null;
                    try {
                        ip = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        plugin.getLogger().log(Level.SEVERE, "No server IP found!");
                    }
                    assert ip != null;
                    sender.sendMessage(ChatColor.GREEN + "http://" + ip.getHostAddress() + ":" + plugin.getConfig().getInt("port") + "/" + args[1] + "/" + args[2]);
                    return true;
                }
            } else if(args[0].equals("generateurl")) {
                sender.sendMessage(ChatColor.RED + "Usage: /placeholderweb generateurl <player> <placeholders>");
                return true;
            }
        }

        return false;
    }
}
