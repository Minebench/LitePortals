package io.github.apfelcreme.LitePortals.Bukkit;

import org.bukkit.block.Block;
import java.util.List;

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
public class PortalStructure {

    private Block centerBlock = null;
    private Double yaw = null;
    private List<Block> blocks = null;

    public PortalStructure(Block centerBlock, Double yaw, List<Block> blocks) {
        this.centerBlock = centerBlock;
        this.yaw = yaw;
        this.blocks = blocks;
    }

    /**
     * returns the center block
     *
     * @return the center block
     */
    public Block getCenterBlock() {
        return centerBlock;
    }

    /**
     * returns the direction the portal faces to
     * NORTH = 180
     * SOUTH = 0
     * EAST = 270
     * WEST = 90
     * @return the yaw
     */
    public Double getYaw() {
        return yaw;
    }

    /**
     * returns a list of all blocks
     *
     * @return a list of all blocks
     */
    public List<Block> getBlocks() {
        return blocks;
    }

    @Override
    public String toString() {
        return "PortalStructure{" +
                "centerBlock=" + centerBlock +
                ", yaw=" + yaw +
                ", blocks=" + blocks +
                '}';
    }
}
