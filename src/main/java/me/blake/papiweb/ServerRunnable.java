package me.blake.papiweb;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class ServerRunnable extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final int port;

    public ServerRunnable(JavaPlugin plugin, int port) {
        this.plugin = plugin;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            new WebServer(port, plugin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
