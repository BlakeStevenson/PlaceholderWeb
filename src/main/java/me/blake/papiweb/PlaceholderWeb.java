package me.blake.papiweb;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class PlaceholderWeb extends JavaPlugin {

    private ServerThread server;
    @Override
    public void onEnable() {
        this.getCommand("placeholderweb").setExecutor(new PlaceholderWebCommand(this));
        this.getCommand("placeholderweb").setTabCompleter(new PlaceholderWebTabCompletion());
        this.saveDefaultConfig();
        setupWebServer();
    }

    @Override
    public void onDisable() {
        server.getApp().stop();
        server.interrupt();
    }

    private void setupWebServer() {
        // Get the current class loader.
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Temporarily set this thread's class loader to the plugin's class loader.
        Thread.currentThread().setContextClassLoader(PlaceholderWeb.class.getClassLoader());

        // Instantiate the web server (which will now load using the plugin's class loader).
        server = new ServerThread(this);
        server.start();

        // Put the original class loader back where it was.
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    public String getPlaceholders(Context ctx) {
        Server server = this.getServer();
        OfflinePlayer player;
        if (ctx.pathParam("player").matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}")) {
            player = server.getOfflinePlayer(UUID.fromString(ctx.pathParam("player")));
        } else {
            player = server.getOfflinePlayer(ctx.pathParam("player"));
        }

        Map<String, String> output = new HashMap<>();
        if (ctx.pathParam("placeholder").contains(this.getConfig().getString("separator"))) {
            String[] placeholders = ctx.pathParam("placeholder").split(this.getConfig().getString("separator"));
            for (String p : placeholders) {
                output.put(p, PlaceholderAPI.setPlaceholders(player, "%" + p + "%"));
            }
        } else {
            String p = ctx.pathParam("placeholder");
            output.put(p, PlaceholderAPI.setPlaceholders(player, "%" + p + "%"));
        }
        Gson gson = new Gson();
        return gson.toJson(output);

    }

    public ServerThread getServerThread() {
        return server;
    }
    public void restartServer() {
        server.getApp().stop();
        server.interrupt();
        setupWebServer();

    }
}
