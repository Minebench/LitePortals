package io.github.apfelcreme.LitePortals.Bungee.Portal;

import io.github.apfelcreme.LitePortals.Bungee.LitePortals;
import io.github.apfelcreme.LitePortals.Bungee.LitePortalsConfig;
import io.github.apfelcreme.LitePortals.Bungee.PortalManager;

import java.text.DecimalFormat;
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
public class Portal {

    private UUID id = null;
    private UUID builder = null;
    private String name = null;
    private Boolean enabled = null;
    private Boolean locked = null;
    private Location location = null;
    private UUID target = null;

    public Portal(UUID builder, Location location) {
        while (id == null) {
            UUID newId = UUID.randomUUID();
            boolean duplicate = false;
            for (Portal portal : PortalManager.getInstance().getPortals()) {
                if (portal.getId().equals(newId)) {
                    duplicate = true;
                }
            }
            if (!duplicate) {
                id = newId;
            }
        }
        this.builder = builder;
        this.enabled = true;
        this.locked = false;
        this.location = location;
        this.target = null;
    }

    public Portal(UUID id, UUID builder, String name, Boolean enabled, Boolean locked, Location location, UUID target) {
        this.id = id;
        this.builder = builder;
        this.name = name;
        this.enabled = enabled;
        this.locked = locked;
        this.location = location;
        this.target = target;
    }

    /**
     * returns the portal id
     *
     * @return the portal id
     */
    public UUID getId() {
        return id;
    }

    /**
     * returns the builders uuid
     *
     * @return the builders uuid
     */
    public UUID getBuilder() {
        return builder;
    }

    /**
     * returns the portal name
     *
     * @return the portal name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the portal name
     *
     * @param name the portal name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns whether the portal is being loaded or not
     *
     * @return true or false
     */
    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * returns whether the portal is available for all players or just the owner
     *
     * @return true or false
     */
    public Boolean isLocked() {
        return locked;
    }

    /**
     * sets the lock status
     *
     * @param locked true or false
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * returns the portal location
     *
     * @return the portal location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * returns the target portal
     *
     * @return the target portal
     */
    public UUID getTarget() {
        return target;
    }

    /**
     * sets the target portal
     *
     * @param target target portal
     */
    public void setTarget(UUID target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "Portal{" +
                "id=" + id +
                ", n='" + name +
                ", t=" + target +
                ", l=" + locked +
                '}';
    }

    /**
     * returns an info string
     *
     * @return a string containing some information about the portal
     */
    public String getInfo() {
        return LitePortalsConfig.getInstance().getText("info.info.element")
                .replace("{0}", name)
                .replace("{1}", locked ? LitePortalsConfig.getInstance().getText("info.lock.symbol.locked")
                        : LitePortalsConfig.getInstance().getText("info.lock.symbol.notLocked"))
                .replace("{2}", location.getWorld())
                .replace("{3}", new DecimalFormat("0").format(location.getX()))
                .replace("{4}", new DecimalFormat("0").format(location.getY()))
                .replace("{5}", new DecimalFormat("0").format(location.getZ()))
                .replace("{6}", LitePortals.getInstance().getTargetServer(location.getServer()).getName())
                .replace("{7}", LitePortals.getInstance().getNameByUUID(builder))
                .replace("{8}", target != null ?
                        PortalManager.getInstance().getPortal(target).getName() :
                        LitePortalsConfig.getInstance().getText("info.info.none"));
    }
}
