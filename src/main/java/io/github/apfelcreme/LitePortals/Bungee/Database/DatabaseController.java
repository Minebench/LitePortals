package io.github.apfelcreme.LitePortals.Bungee.Database;

import io.github.apfelcreme.LitePortals.Bungee.Portal.Portal;
import net.md_5.bungee.api.connection.Server;

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
public interface DatabaseController {

    /**
     * loads all Portals
     *
     * @return all Portals in a List
     */
    List<Portal> loadPortals();

    /**
     * saves a portal
     *
     * @param portal a portal
     */
    void savePortal(Portal portal);

    /**
     * deletes a portal and resets the target of all portals it was connected to
     *
     * @param portal a portal
     */
    void deletePortal(Portal portal);

    /**
     * sets the portals target
     *
     * @param portal1 the source portal
     * @param portal2 the target the portal shall be connected to
     */
    void link(Portal portal1, Portal portal2);

    /**
     * locks a portal and makes it available to only the builder
     *
     * @param portal a portal
     */
    void lock(Portal portal);

    /**
     * unlocks a portal and makes it available to all players
     *
     * @param portal a portal
     */
    void unlock(Portal portal);

    /**
     * disabled all portals on a given world
     *
     * @param worldName the name of the world
     * @param server    the server the sender is on
     * @return the number of portals that were disabled
     */
    int disableWorld(String worldName, Server server);

    /**
     * enables all portals on a given world if they are disabled
     *
     * @param worldName the name of the world
     * @param server    currentServer the server the sender is on
     * @return the number of portals that were disabled
     */
    int enableWorld(String worldName, Server server);

}
