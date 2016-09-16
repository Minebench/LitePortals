package io.github.apfelcreme.LitePortals.Bukkit.Listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.github.apfelcreme.LitePortals.Bukkit.BungeeMessenger;
import io.github.apfelcreme.LitePortals.Bukkit.LitePortals;
import io.github.apfelcreme.LitePortals.Bukkit.PortalStructure;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

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
public class BungeeMessageListener implements PluginMessageListener {

    /**
     * listenes to portal messages
     *
     * @param s      the channel
     * @param player ?
     * @param bytes  the data
     */
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if (!s.equals("LitePortals")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subChannel = in.readUTF();
        if (subChannel.equals("WARP")) {
            Player p = LitePortals.getInstance().getServer().getPlayer(UUID.fromString(in.readUTF()));
            Location location = new Location(Bukkit.getWorld(in.readUTF()),
                    in.readDouble() + 0.5, in.readDouble() + 0.2, in.readDouble() + 0.5,
                    (float) in.readDouble(), 0);
            if (p != null) {
                p.teleport(location);
                for (int i = 0; i < 10; i++) {
                    p.getWorld().spigot().playEffect(location, Effect.PORTAL, 0, 0,
                            (float) (-1 + Math.random() * 2),
                            (float) (Math.random() * 2),
                            (float) (-1 + Math.random() * 2), 0, 1, 50);
                }
            }
        } else if (subChannel.equals("POSITIONREQUEST")) {
            Player p = LitePortals.getInstance().getServer().getPlayer(UUID.fromString(in.readUTF()));
            if (p != null) {
                PortalStructure portalStructure = LitePortals.getPortalStructure(p.getLocation());
                BungeeMessenger.getInstance().sendPlayerPositionMessage(p.getUniqueId(), portalStructure);
            }
        }

    }
}
