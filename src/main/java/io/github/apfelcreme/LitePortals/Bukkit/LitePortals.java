package io.github.apfelcreme.LitePortals.Bukkit;

import io.github.apfelcreme.LitePortals.Bukkit.Listener.BungeeMessageListener;
import io.github.apfelcreme.LitePortals.Bukkit.Listener.PlayerSneakListener;
import io.github.apfelcreme.LitePortals.Bukkit.Listener.PortalBreakListener;
import io.github.apfelcreme.LitePortals.Bukkit.Listener.PortalBuildListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class LitePortals extends JavaPlugin {

    @Override
    public void onEnable() {

        // register the Plugin channels for the bungee communication
        getServer().getMessenger().registerOutgoingPluginChannel(this, "LitePortals");
        getServer().getMessenger().registerIncomingPluginChannel(this, "LitePortals",
                new BungeeMessageListener());

        // register the block listener
        getServer().getPluginManager().registerEvents(new PortalBuildListener(), this);
        getServer().getPluginManager().registerEvents(new PortalBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerSneakListener(), this);
    }

    /**
     * returns the plugin instance
     *
     * @return the plugin instance
     */
    public static LitePortals getInstance() {
        return (LitePortals) Bukkit.getServer().getPluginManager().getPlugin("LitePortals");
    }

    /**
     * returns a PortalStructure if there is one on the give block
     *
     * @param location a location
     * @return a PortalStructure if there is a portal
     */
    public static PortalStructure getPortalStructure(Location location) {
        return getPortalStructure(location.getBlock());
    }

    /**
     * returns a PortalStructure if there is one on the give block
     *
     * @param block a block, must be an Iron Plate
     * @return a PortalStructure if there is a portal
     */
    public static PortalStructure getPortalStructure(Block block) {
        if (block.getType() == Material.IRON_PLATE) {
            PortalStructure portalStructure = portalBuilt(BlockFace.EAST, block);
            if (portalStructure == null) {
                portalStructure = portalBuilt(BlockFace.SOUTH, block);
                if (portalStructure == null) {
                    portalStructure = portalBuilt(BlockFace.WEST, block);
                    if (portalStructure == null) {
                        portalStructure = portalBuilt(BlockFace.NORTH, block);
                    }
                }
            }
            return portalStructure;
        }
        return null;
    }

    /**
     * checks if a portal was built
     *
     * @param direction the direction that should be checked
     * @param block     the center block (pressure plate)
     * @return a portal structure, or null if none was found
     */
    private static PortalStructure portalBuilt(BlockFace direction, Block block) {
        World world = block.getWorld();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        Material p = Material.OBSIDIAN;
        switch (direction) {
            case NORTH:
                if ((world.getBlockAt(x - 1, y, z).getType() == p)
                        && (world.getBlockAt(x - 1, y + 1, z).getType() == p)
                        && (world.getBlockAt(x + 1, y, z).getType() == p)
                        && (world.getBlockAt(x + 1, y + 1, z).getType() == p)
                        && (world.getBlockAt(x, y, z + 1).getType() == p)
                        && (world.getBlockAt(x, y + 1, z + 1).getType() == p)) {
                    return new PortalStructure(block, 180.0, Arrays.asList(
                            block,
                            world.getBlockAt(x - 1, y, z),
                            world.getBlockAt(x - 1, y + 1, z),
                            world.getBlockAt(x + 1, y, z),
                            world.getBlockAt(x + 1, y + 1, z),
                            world.getBlockAt(x, y, z + 1),
                            world.getBlockAt(x, y + 1, z + 1)));
                }
                break;
            case SOUTH:
                if ((world.getBlockAt(x - 1, y, z).getType() == p)
                        && (world.getBlockAt(x - 1, y + 1, z).getType() == p)
                        && (world.getBlockAt(x + 1, y, z).getType() == p)
                        && (world.getBlockAt(x + 1, y + 1, z).getType() == p)
                        && (world.getBlockAt(x, y, z - 1).getType() == p)
                        && (world.getBlockAt(x, y + 1, z - 1).getType() == p)) {
                    return new PortalStructure(block, 0.0, Arrays.asList(
                            block,
                            world.getBlockAt(x - 1, y, z),
                            world.getBlockAt(x - 1, y + 1, z),
                            world.getBlockAt(x + 1, y, z),
                            world.getBlockAt(x + 1, y + 1, z),
                            world.getBlockAt(x, y, z - 1),
                            world.getBlockAt(x, y + 1, z - 1)));
                }
                break;
            case EAST:
                if ((world.getBlockAt(x, y, z - 1).getType() == p)
                        && (world.getBlockAt(x, y + 1, z - 1).getType() == p)
                        && (world.getBlockAt(x, y, z + 1).getType() == p)
                        && (world.getBlockAt(x, y + 1, z + 1).getType() == p)
                        && (world.getBlockAt(x - 1, y, z).getType() == p)
                        && (world.getBlockAt(x - 1, y + 1, z).getType() == p)) {
                    return new PortalStructure(block, 270.0, Arrays.asList(
                            block,
                            world.getBlockAt(x, y, z - 1),
                            world.getBlockAt(x, y + 1, z - 1),
                            world.getBlockAt(x, y, z + 1),
                            world.getBlockAt(x, y + 1, z + 1),
                            world.getBlockAt(x - 1, y, z),
                            world.getBlockAt(x - 1, y + 1, z)));
                }
                break;
            case WEST:
                if ((world.getBlockAt(x, y, z - 1).getType() == p)
                        && (world.getBlockAt(x, y + 1, z - 1).getType() == p)
                        && (world.getBlockAt(x, y, z + 1).getType() == p)
                        && (world.getBlockAt(x, y + 1, z + 1).getType() == p)
                        && (world.getBlockAt(x + 1, y, z).getType() == p)
                        && (world.getBlockAt(x + 1, y + 1, z).getType() == p)) {
                    return new PortalStructure(block, 90.0, Arrays.asList(
                            block,
                            world.getBlockAt(x, y, z - 1),
                            world.getBlockAt(x, y + 1, z - 1),
                            world.getBlockAt(x, y, z + 1),
                            world.getBlockAt(x, y + 1, z + 1),
                            world.getBlockAt(x + 1, y, z),
                            world.getBlockAt(x + 1, y + 1, z)));
                }
                break;
        }
        return null;
    }

}
