package me.blake.papiweb;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import me.blake.papiweb.routes.PlaceholderHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class WebServer extends RouterNanoHTTPD {
    public static JavaPlugin plugin;

    public WebServer(int port, JavaPlugin plugin) throws IOException {
        super(port);
        WebServer.plugin = plugin;
        addMappings();
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("[PlaceholderWeb] Running on port " + port + "!");
    }

    @Override
    public void addMappings() {
        // todo fill in the routes
        try {
            addRoute("/:player/:placeholder", PlaceholderHandler.class);
        } catch(Exception ignored) {

        }
    }
}