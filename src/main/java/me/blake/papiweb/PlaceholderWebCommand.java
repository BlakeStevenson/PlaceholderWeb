package me.blake.papiweb;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

public class PlaceholderWebCommand implements CommandExecutor {

    private String getIP()
    {
        try
        {
            URL url = new URL("http://bot.whatismyipaddress.com/");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ipAddress;
            ipAddress = (in.readLine()).trim();
            if (!(ipAddress.length() > 0))
            {
                try
                {
                    InetAddress ip = InetAddress.getLocalHost();
                    System.out.println((ip.getHostAddress()).trim());
                    return ((ip.getHostAddress()).trim());
                }
                catch(Exception ex)
                {
                    return "ERROR";
                }
            }
            return (ipAddress);
        }
        catch(Exception e)
        {
            try
            {
                InetAddress ip = InetAddress.getLocalHost();
                System.out.println((ip.getHostAddress()).trim());
                return ((ip.getHostAddress()).trim());
            }
            catch(Exception ex)
            {
                return "ERROR";
            }
        }
    }

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
                    if(plugin.getConfig().getString("auth").contains("true")) {
                        sender.sendMessage(ChatColor.RED + "Auth is Enabled, url will be generated with a key!");
                        List<String> uuidkey = plugin.getConfig().getStringList("keys");
                        for (String keys2 : uuidkey) {
                            TextComponent component = new TextComponent(ChatColor.GREEN + "http://" + getIP() + ":" + plugin.getConfig().getInt("port") + "/" + args[1] + "/" + args[2] + "?key=" + keys2.replaceAll("[\\[\\],]",""));
                            String url = ("http://" + getIP() + ":" + plugin.getConfig().getInt("port") + "/" + args[1] + "/" + args[2] + "?key=" + keys2.replaceAll("[\\[\\],]",""));
                            component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                            sender.spigot().sendMessage(component);
                        }
                        return true;
                    } else {
                        TextComponent component = new TextComponent(ChatColor.GREEN + "http://" + getIP() + ":" + plugin.getConfig().getInt("port") + "/" + args[1] + "/" + args[2]);
                        String url = ("http://" + getIP() + ":" + plugin.getConfig().getInt("port") + "/" + args[1] + "/" + args[2]);
                        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                        sender.spigot().sendMessage(component);
                        return true;
                    }
                }
            } else if(args[0].equals("generateurl")) {
                sender.sendMessage(ChatColor.RED + "Usage: /placeholderweb generateurl <player> <placeholders>");
                return true;
            }
        }

        return false;
    }
}
