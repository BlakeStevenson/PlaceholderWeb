package me.blake.papiweb;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlaceholderWebTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {

        List<String> pwebarg1 = new ArrayList<>();
        if (args.length == 1) {
            pwebarg1.add("generate");
            pwebarg1.add("reload");
            pwebarg1.add("debug");
            pwebarg1.add("generateurl");
        }

        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            for (String a : pwebarg1) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        } else if (args.length == 2 && args[0].equals("generateurl")) {
            List<String> pwebarg2 = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (Player player : players) {
                pwebarg2.add(player.getName());
            }
            pwebarg2.add("server");

            return pwebarg2;
        }

        return null;
    }
}
