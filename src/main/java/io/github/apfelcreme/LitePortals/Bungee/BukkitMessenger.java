package io.github.apfelcreme.LitePortals.Bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.apfelcreme.LitePortals.Bungee.Portal.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.IOException;
import java.util.UUID;

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
public class BukkitMessenger {

    /**
     * the BukkitMessenger singleton instance
     */
    private static BukkitMessenger instance = null;

    /**
     * returns the singleton instance
     *
     * @return the instance
     */
    public static BukkitMessenger getInstance() {
        if (instance == null) {
            instance = new BukkitMessenger();
        }
        return instance;
    }

    /**
     * teleports a player to a location. If that location is on a different Bukkit instance, he is being sent to that server
     *
     * @param uuid           the player uuid
     * @param targetLocation the target location the player is being teleported to.
     */
    public void teleportPlayer(UUID uuid, Location targetLocation) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (player != null) {
            try {
                ServerInfo serverInfo = LitePortals.getInstance().getTargetServer(targetLocation.getServer());
                if (serverInfo != null) {
                    if (!player.getServer().getInfo().equals(serverInfo) && serverInfo.getAddress().getAddress().isReachable(2000)) {
                        player.connect(serverInfo);
                        LitePortals.getInstance().getLogger().info(player.getName() + " used a LitePortal to " + serverInfo.getName());
                    }

                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("WARP");
                    out.writeUTF(uuid.toString());
                    out.writeUTF(targetLocation.getWorld());
                    out.writeDouble(targetLocation.getX());
                    out.writeDouble(targetLocation.getY());
                    out.writeDouble(targetLocation.getZ());
                    out.writeDouble(targetLocation.getYaw());
                    serverInfo.sendData("LitePortals", out.toByteArray());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * sends a message to the bukkit to check the position of a player.
     * Bukkit then returns a message with a location of a portal if the player is standing in one
     *
     * @param player the player
     */
    public void sendPositionRequest(ProxiedPlayer player) {
        ServerInfo target = player.getServer().getInfo();
        if (target != null) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("POSITIONREQUEST");
            out.writeUTF(player.getUniqueId().toString());
            target.sendData("LitePortals", out.toByteArray());
        }
    }
}
