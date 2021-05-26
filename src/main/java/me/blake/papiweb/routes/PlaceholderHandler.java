package me.blake.papiweb.routes;

import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD.DefaultHandler;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;
import me.blake.papiweb.WebServer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class PlaceholderHandler extends DefaultHandler {
    public String getText(Map<String, String> urlParams, IHTTPSession session) {
        if (WebServer.plugin.getConfig().getStringList("keys").contains(session.getParms().get("key"))) {
            Server server = WebServer.plugin.getServer();
            OfflinePlayer player;
            if (urlParams.get("player").matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}")) {
                player = server.getOfflinePlayer(UUID.fromString(urlParams.get("player")));
            } else {
                player = server.getOfflinePlayer(urlParams.get("player"));
            }

            Map<String, String> output = new HashMap<>();
            if (urlParams.get("placeholder").contains(",")) {
                String[] placeholders = urlParams.get("placeholder").split(",");
                for (String p : placeholders) {
                    output.put(p, PlaceholderAPI.setPlaceholders(player, "%" + p + "%"));
                }
            } else {
                String p = urlParams.get("placeholder");
                output.put(p, PlaceholderAPI.setPlaceholders(player, "%" + p + "%"));
            }
            Gson gson = new Gson();
            return gson.toJson(output);
        }
        return "Unauthorized";
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    public String getText() {
        // not implemented
        return null;
    }

    @Override
    public NanoHTTPD.Response.IStatus getStatus() {
        return NanoHTTPD.Response.Status.OK;
    }

    public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        String text = getText(urlParams, session);
        ByteArrayInputStream inp = new ByteArrayInputStream(text.getBytes());
        int size = text.getBytes().length;
        return newFixedLengthResponse(getStatus(), getMimeType(), inp, size);
    }
}
