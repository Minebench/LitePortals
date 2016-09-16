package io.github.apfelcreme.LitePortals.Bungee;

import io.github.apfelcreme.LitePortals.Bungee.Database.DatabaseController;
import io.github.apfelcreme.LitePortals.Bungee.Database.MongoConnector;
import io.github.apfelcreme.LitePortals.Bungee.Database.MongoController;
import io.github.apfelcreme.LitePortals.Bungee.Listener.BukkitMessageListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.zaiyers.UUIDDB.bungee.UUIDDB;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Copyright (C) 2016 Lord36 aka Apfelcreme
 * <p>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * @author Lord36 aka Apfelcreme
 */
public class LitePortals extends Plugin {


    /**
     * the database controller
     */
    private DatabaseController databaseController = null;

    /**
     * a cache for name -> uuid. Just in case some player hammers /portal list [Name] or so
     */
    private Map<String, UUID> uuidCache = null;

    /**
     * onEnable
     */
    @Override
    public void onEnable() {
        // initialize some map
        uuidCache = new HashMap<String, UUID>();

        // initialize the database connection
        databaseController = new MongoController();

        // register a command
        getProxy().getPluginManager().registerCommand(this, new CommandExecutor("portal"));

        // register the stuff for the plugin message connection
        getProxy().registerChannel("LitePortals");
        getProxy().getPluginManager().registerListener(this, new BukkitMessageListener());

        getProxy().getScheduler().schedule(this, new Runnable() {
            public void run() {
                System.out.println(PortalManager.getInstance().getPortals().size() + "|" + PortalManager.getInstance().getLatestConstructions().size());
                System.out.println(uuidCache.toString());
            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    /**
     * closed the Mongo Connector
     */
    @Override
    public void onDisable() {
        MongoConnector.getInstance().close();
    }

    /**
     * returns the server info with the given ip (xxx.xxx.xxx.xxx.PORT)
     *
     * @param serverIp the ip.port
     * @return the serverInfo
     */
    public ServerInfo getTargetServer(String serverIp) {
        for (ServerInfo serverInfo : getProxy().getServers().values()) {
            if (serverInfo.getAddress().equals(new InetSocketAddress(serverIp.split(Pattern.quote("."))[0],
                    Integer.parseInt(serverIp.split(Pattern.quote("."))[1])))) {
                return serverInfo;
            }
        }
        return null;
    }

    /**
     * returns the database controller
     *
     * @return the database controller
     */
    public DatabaseController getDatabaseController() {
        return this.databaseController;
    }

    /**
     * returns the plugin instance
     *
     * @return the plugin instance
     */
    public static LitePortals getInstance() {
        return (LitePortals) ProxyServer.getInstance().getPluginManager().getPlugin("LitePortals");
    }

    /**
     * sends a message to a player
     *
     * @param uuid    the players uuid
     * @param message the message
     */
    public static void sendMessage(UUID uuid, String message) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (player != null) {
            player.sendMessage(TextComponent.fromLegacyText(getPrefix() + message));
        }

    }

    /**
     * returns the plugin prefix
     *
     * @return the plugin prefix
     */
    public static String getPrefix() {
        return LitePortalsConfig.getInstance().getText("prefix");
    }

    /**
     * returns the uuid of the player with the given uuid
     *
     * @param name a players name
     * @return his uuid
     */
    public UUID getUUIDByName(String name) {
        name = name.toUpperCase();
        if (uuidCache.containsKey(name)) {
            return uuidCache.get(name);
        } else if (getProxy().getPluginManager().getPlugin("UUIDDB") != null) {
            UUID uuid = UUID.fromString(UUIDDB.getInstance().getStorage().getUUIDByName(name, false));
            uuidCache.put(name, uuid);
            return uuid;
        } else {
            try {
                URL url = new URL(LitePortalsConfig.getInstance().getConfiguration().getString("uuidUrl")
                        .replace("{0}", name));
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder json = new StringBuilder();
                int read;
                while ((read = in.read()) != -1) {
                    json.append((char) read);
                }
                if (json.length() == 0) {
                    return null;
                }
                JSONObject jsonObject = (JSONObject) (new JSONParser().parse(json.toString()));
                String id = jsonObject.get("id").toString();
                UUID uuid = UUID.fromString(id.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                        "$1-$2-$3-$4-$5"));
                uuidCache.put(name, uuid);
                return uuid;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * returns the name of a player
     *
     * @param uuid a players uuid
     * @return his name
     */
    public String getNameByUUID(UUID uuid) {
        if (uuidCache.containsValue(uuid)) {
            for (Map.Entry<String, UUID> entry : uuidCache.entrySet()) {
                if (entry.getValue().equals(uuid)) {
                    return entry.getKey();
                }
            }
        } else if (getProxy().getPluginManager().getPlugin("UUIDDB") != null) {
            String name = UUIDDB.getInstance().getStorage().getNameByUUID(uuid);
            uuidCache.put(name.toUpperCase(), uuid);
            return name;
        } else {
            try {
                URL url = new URL(LitePortalsConfig.getInstance().getConfiguration().getString("nameUrl")
                        .replace("{0}", uuid.toString().replace("-", "")));
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder json = new StringBuilder();
                int read;
                while ((read = in.read()) != -1) {
                    json.append((char) read);
                }
                Object obj = new JSONParser().parse(json.toString());
                JSONArray jsonArray = (JSONArray) obj;
                String name = (String) ((JSONObject) jsonArray.get(jsonArray.size() - 1)).get("name");
                uuidCache.put(name.toUpperCase(), uuid);
                return name;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

}

