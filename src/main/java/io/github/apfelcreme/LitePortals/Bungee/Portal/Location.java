package io.github.apfelcreme.LitePortals.Bungee.Portal;

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
public class Location {

    private String server = null;
    private String world = null;
    private Double x = null;
    private Double y = null;
    private Double z = null;
    private Double yaw = null;

    public Location(String server, String world, Double x, Double y, Double z, Double yaw) {
        this.server = server;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
    }

    /**
     * returns the server ip:port of the server the portal is on
     *
     * @return the server ip:port of the server the portal is on
     */
    public String getServer() {
        return server;
    }

    /**
     * returns the world the portal is on
     *
     * @return the world the portal is on
     */
    public String getWorld() {
        return world;
    }

    /**
     * returns the x coordinate
     *
     * @return the x coordinate
     */
    public Double getX() {
        return x;
    }

    /**
     * returns the y coordinate
     *
     * @return the y coordinate
     */
    public Double getY() {
        return y;
    }

    /**
     * returns the z coordinate
     *
     * @return the z coordinate
     */
    public Double getZ() {
        return z;
    }

    /**
     * returns the yaw
     * @return the yaw
     */
    public Double getYaw() {
        return yaw;
    }

    /**
     * equals
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location location = (Location) obj;
            return this.server.equals(location.getServer())
                    && this.world.equals(location.getWorld())
                    && this.x.equals(location.getX())
                    && this.y.equals(location.getY())
                    && this.z.equals(location.getZ());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "s:"+server+" xyz:"+x+":"+y+":"+z+" w:"+world+" y:"+yaw;
    }
}
