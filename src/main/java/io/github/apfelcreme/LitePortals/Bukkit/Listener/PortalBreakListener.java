package io.github.apfelcreme.LitePortals.Bukkit.Listener;

import io.github.apfelcreme.LitePortals.Bukkit.BungeeMessenger;
import io.github.apfelcreme.LitePortals.Bukkit.LitePortals;
import io.github.apfelcreme.LitePortals.Bukkit.PortalStructure;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

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
public class PortalBreakListener implements Listener {

    /**
     * listens to a block break
     *
     * @param event
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.OBSIDIAN || event.getBlock().getType() == Material.IRON_PLATE) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        int xC = event.getBlock().getX() + x;
                        int yC = event.getBlock().getY() + y;
                        int zC = event.getBlock().getZ() + z;
                        if (event.getBlock().getWorld().getBlockAt(xC, yC, zC).getType() == Material.IRON_PLATE) {
                            PortalStructure portalStructure = LitePortals.getPortalStructure(event.getBlock().getWorld().getBlockAt(xC, yC, zC));
                            if (portalStructure != null && portalStructure.getBlocks().contains(event.getBlock())) {
                                BungeeMessenger.getInstance().sendPortalBrokenMessage(event.getPlayer().getUniqueId(), portalStructure);
                            }
                        }
                    }
                }
            }

        }
    }
}
