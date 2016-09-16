package io.github.apfelcreme.LitePortals.Bukkit.Listener;

import io.github.apfelcreme.LitePortals.Bukkit.BungeeMessenger;
import io.github.apfelcreme.LitePortals.Bukkit.LitePortals;
import io.github.apfelcreme.LitePortals.Bukkit.PortalStructure;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
public class PlayerSneakListener implements Listener {

    private Map<UUID, Long> cooldowns = new HashMap<UUID, Long>();

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
//    public void onPlayerSneak(PlayerMoveEvent event) {
        Block block = event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation());
        if (block.getType() == Material.IRON_PLATE) {

            //check if there is a portal
            PortalStructure portalStructure = LitePortals.getPortalStructure(block);
            if (portalStructure!= null) {
                // prevent double teleport messages and abuse to some degree
                if ((cooldowns.get(event.getPlayer().getUniqueId()) == null)
                        || (new Date().getTime() > (cooldowns.get(event.getPlayer().getUniqueId()) + 1000))) {
                    cooldowns.put(event.getPlayer().getUniqueId(), new Date().getTime());
                    BungeeMessenger.getInstance().sendPlayerUsedPortalMessage(event.getPlayer().getUniqueId(), portalStructure);
                }
            }
        }
    }
}
