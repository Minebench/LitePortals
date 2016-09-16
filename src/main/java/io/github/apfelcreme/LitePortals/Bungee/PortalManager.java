package io.github.apfelcreme.LitePortals.Bungee;

import io.github.apfelcreme.LitePortals.Bungee.Portal.Location;
import io.github.apfelcreme.LitePortals.Bungee.Portal.Portal;

import java.util.*;

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
public class PortalManager {

    /**
     * a list of constructions that have not been saved yet by /portal create
     */
    private Map<UUID, Portal> latestConstructions = null;

    /**
     * a list of all portals
     */
    private List<Portal> portals = null;

    /**
     * the portalManager singleton instance
     */
    private static PortalManager instance = null;

    private PortalManager() {

        // initialize some maps
        latestConstructions = new HashMap<UUID, Portal>();
        portals = new ArrayList<Portal>();

        // load all portals
        reload();
    }

    /**
     * reloads all portals
     *
     * @return the number of portals that were loaded
     */
    public int reload() {
        portals = LitePortals.getInstance().getDatabaseController().loadPortals();
        LitePortals.getInstance().getLogger().info(portals.size() + " Portals have been loaded!");
        return portals.size();
    }

    /**
     * returns the singleton instance
     *
     * @return the instance
     */
    public static PortalManager getInstance() {
        if (instance == null) {
            instance = new PortalManager();
        }
        return instance;
    }

    /**
     * returns a portal with the given uuid
     *
     * @param uuid the portal id
     * @return the portal
     */
    public Portal getPortal(UUID uuid) {
        for (Portal portal : portals) {
            if (portal.getId().equals(uuid)) {
                return portal;
            }
        }
        return null;
    }

    /**
     * returns a portal with the given name
     *
     * @param name  a portal name
     * @param owner the portal owner - both parameters combined are used to identify a portal
     * @return the portal
     */
    public Portal getPortal(UUID owner, String name) {
        for (Portal portal : portals) {
            if (portal.getBuilder().equals(owner) && portal.getName().equals(name)) {
                return portal;
            }
        }
        return null;
    }

    /**
     * checks if there is a portal at the given location
     *
     * @param location a location
     * @return the portal, if there is one, or null if there is none
     */
    public Portal portalExists(Location location) {
        for (Portal portal : portals) {
            if (portal.getLocation().equals(location)) {
                return portal;
            }
        }
        return null;
    }

    /**
     * removes a portal from the list
     *
     * @param uuid the portals uuid
     */
    public void removePortal(UUID uuid) {
        Iterator<Portal> portalIterator = portals.iterator();
        while (portalIterator.hasNext()) {
            Portal portal = portalIterator.next();
            if (portal.getId().equals(uuid)) {
                portalIterator.remove();
            }
        }
    }

    /**
     * returns all portals of a player
     *
     * @param player a players uuid
     * @return a list of portals
     */
    public List<Portal> getPlayerPortals(UUID player) {
        List<Portal> playerPortals = new ArrayList<Portal>();
        for (Portal portal : portals) {
            if (portal.getBuilder().equals(player)) {
                playerPortals.add(portal);
            }
        }
        return playerPortals;
    }

    /**
     * returns the list of portals
     *
     * @return the list of portals
     */
    public List<Portal> getPortals() {
        return this.portals;
    }

    /**
     * returns a portal structure if there is one at this position
     *
     * @param location a location
     * @return a portal structure that has not been saved yet
     */
    public Portal getLatestConstruction(Location location) {
        for (Portal portalStructure : latestConstructions.values()) {
            if (portalStructure.getLocation().equals(location)) {
                return portalStructure;
            }
        }
        return null;
    }

    /**
     * checks if there is a portal structure that was not saves yet
     *
     * @param location a location
     * @return the portal structure, if there is one, or null if there is none
     */
    public Portal structureExists(Location location) {
        System.out.println(location.toString());
        for (Portal structure : latestConstructions.values()) {
            if (structure.getLocation().equals(location)) {
                System.out.println("   " + location.toString());
                return structure;
            }
        }
        return null;
    }

    /**
     * returns the map of portals that have been built but have not yet been registered
     *
     * @return a map, with a players uuid as a key, and his latest portal construction as its value
     */
    public Map<UUID, Portal> getLatestConstructions() {
        return this.latestConstructions;
    }

}
