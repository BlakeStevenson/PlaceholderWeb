package me.blake.papiweb;

import io.javalin.Javalin;

import java.util.logging.Level;

public class ServerThread extends Thread {
    private Javalin app;
    private final PlaceholderWeb plugin;

    public ServerThread(PlaceholderWeb pl) {
        plugin = pl;
    }

    @Override
    public void run() {

        app = Javalin.create().start(plugin.getConfig().getInt("port"));
        app.get("/:player/:placeholder", ctx -> {
            if(plugin.getConfig().getBoolean("debug")) {
                plugin.getLogger().log(Level.INFO, ctx.ip() + ": " + ctx.method() + " " + ctx.fullUrl() + " " + ctx.status());
            }
            if(plugin.getConfig().getStringList("keys").contains(ctx.queryParam("key")) || !plugin.getConfig().getBoolean("auth")) {
                ctx.contentType("application/json");
                ctx.result(plugin.getPlaceholders(ctx));
            } else {
                ctx.status(401);
                ctx.contentType("text/plain");
                ctx.result("401 Unauthorized\n" + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion());
            }
        });
        app.get("/*", ctx -> {
            if(plugin.getConfig().getBoolean("debug")) {
                plugin.getLogger().log(Level.INFO, ctx.ip() + ": " + ctx.method() + " " + ctx.fullUrl() + " " + ctx.status());
            }
            ctx.status(404);
            ctx.result("404 Not Found\n" + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion());
        });
    }
    public Javalin getApp() {
        return app;
    }
}
