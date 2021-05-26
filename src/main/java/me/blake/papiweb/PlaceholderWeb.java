package me.blake.papiweb;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PlaceholderWeb extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        new ServerRunnable(this, this.getConfig().getInt("port")).runTaskAsynchronously(this);
    }
}
