package io.github.apfelcreme.LitePortals.Bukkit;

import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
public class BungeeMessenger {

    /**
     * the BungeeMessenger singleton instance
     */
    private static BungeeMessenger instance = null;

    /**
     * returns the singleton instance
     *
     * @return instance
     */
    public static BungeeMessenger getInstance() {
        if (instance == null) {
            instance = new BungeeMessenger();
        }
        return instance;
    }


    /**
     * sends a message that a portal was built
     *
     * @param builder         the uuid of the player who built the portal
     * @param portalStructure the portalStructure that was built
     */
    public void sendPortalBuiltMessage(UUID builder, PortalStructure portalStructure) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            if (LitePortals.getInstance().getServer().getOnlinePlayers().size() > 0) {
                out.writeUTF("PORTALWASBUILT");
                out.writeUTF(builder.toString());
                out.writeUTF(LitePortals.getInstance().getServer().getIp() + "." + LitePortals.getInstance().getServer().getPort());
                out.writeUTF(portalStructure.getCenterBlock().getWorld().getName());
                out.writeDouble(portalStructure.getCenterBlock().getLocation().getX());
                out.writeDouble(portalStructure.getCenterBlock().getLocation().getY());
                out.writeDouble(portalStructure.getCenterBlock().getLocation().getZ());
                out.writeDouble(portalStructure.getYaw());
                Player player = LitePortals.getInstance().getServer().getOnlinePlayers().iterator().next();
                player.sendPluginMessage(LitePortals.getInstance(), "LitePortals", b.toByteArray());
                out.close();
                b.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a message that a player broke a portal
     *
     * @param breaker         the player who broke the portal
     * @param portalStructure the portal
     */
    public void sendPortalBrokenMessage(UUID breaker, PortalStructure portalStructure) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            if (LitePortals.getInstance().getServer().getOnlinePlayers().size() > 0) {
                out.writeUTF("PLAYERBROKEPORTAL");
                out.writeUTF(breaker.toString());
                out.writeUTF(LitePortals.getInstance().getServer().getIp() + "." + LitePortals.getInstance().getServer().getPort());
                out.writeUTF(portalStructure.getCenterBlock().getWorld().getName());
                out.writeDouble(portalStructure.getCenterBlock().getLocation().getX());
                out.writeDouble(portalStructure.getCenterBlock().getLocation().getY());
                out.writeDouble(portalStructure.getCenterBlock().getLocation().getZ());
                out.writeDouble(portalStructure.getYaw());
                Player player = LitePortals.getInstance().getServer().getOnlinePlayers().iterator().next();
                player.sendPluginMessage(LitePortals.getInstance(), "LitePortals", b.toByteArray());
                out.close();
                b.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a message that a player has used a portal
     *
     * @param user            the uuid of the player who used the portal
     * @param portalStructure the portalStructure that was used
     */
    public void sendPlayerUsedPortalMessage(UUID user, PortalStructure portalStructure) {

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            if (LitePortals.getInstance().getServer().getOnlinePlayers().size() > 0) {
                out.writeUTF("PLAYERUSEDPORTAL");
                out.writeUTF(user.toString());
                out.writeUTF(LitePortals.getInstance().getServer().getIp() + "." + LitePortals.getInstance().getServer().getPort());
                out.writeUTF(portalStructure.getCenterBlock().getWorld().getName());
                out.writeDouble(portalStructure.getCenterBlock().getLocation().getX());
                out.writeDouble(portalStructure.getCenterBlock().getLocation().getY());
                out.writeDouble(portalStructure.getCenterBlock().getLocation().getZ());
                Player player = LitePortals.getInstance().getServer().getOnlinePlayers().iterator().next();
                player.sendPluginMessage(LitePortals.getInstance(), "LitePortals", b.toByteArray());
                out.close();
                b.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a message with
     *
     * @param player          the player uuid
     * @param portalStructure the portal if there is one at the players location
     */
    public void sendPlayerPositionMessage(UUID player, PortalStructure portalStructure) {

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            if (LitePortals.getInstance().getServer().getOnlinePlayers().size() > 0) {
                out.writeUTF("POSITIONANSWER");
                out.writeUTF(player.toString());
                if (portalStructure != null) {
                    out.writeBoolean(true);
                    out.writeUTF(LitePortals.getInstance().getServer().getIp() + "." + LitePortals.getInstance().getServer().getPort());
                    out.writeUTF(portalStructure.getCenterBlock().getWorld().getName());
                    out.writeDouble(portalStructure.getCenterBlock().getLocation().getX());
                    out.writeDouble(portalStructure.getCenterBlock().getLocation().getY());
                    out.writeDouble(portalStructure.getCenterBlock().getLocation().getZ());
                } else{
                    out.writeBoolean(false);
                }
                Player p = LitePortals.getInstance().getServer().getOnlinePlayers().iterator().next();
                p.sendPluginMessage(LitePortals.getInstance(), "LitePortals", b.toByteArray());
                out.close();
                b.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
