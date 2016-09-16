package io.github.apfelcreme.LitePortals.Bungee.Database;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import io.github.apfelcreme.LitePortals.Bungee.Portal.Location;
import io.github.apfelcreme.LitePortals.Bungee.Portal.Portal;
import io.github.apfelcreme.LitePortals.Bungee.PortalManager;
import net.md_5.bungee.api.connection.Server;

import java.util.ArrayList;
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
public class MongoController implements DatabaseController {

    /**
     * loads all Portals
     *
     * @return all Portals in a List
     */
    public List<Portal> loadPortals() {
        List<Portal> portals = new ArrayList<Portal>();
        DBCollection collection = MongoConnector.getInstance().getCollection();
        BasicDBObject query = new BasicDBObject("enabled", true);
        DBCursor dbCursor = collection.find(query);
        while (dbCursor.hasNext()) {
            DBObject portalObject = dbCursor.next();
            Portal portal = createPortal(portalObject);
            portals.add(portal);
        }
        return portals;
    }

    /**
     * saves a portal
     *
     * @param portal a portal
     */
    public void savePortal(Portal portal) {
        DBCollection collection = MongoConnector.getInstance().getCollection();
        BasicDBObject portalObject = new BasicDBObject();
        portalObject.put("portal_id", portal.getId().toString());
        portalObject.put("server", portal.getLocation().getServer());
        portalObject.put("builder", portal.getBuilder().toString());
        portalObject.put("name", portal.getName());
        portalObject.put("enabled", portal.isEnabled());
        portalObject.put("locked", portal.isLocked());
        portalObject.put("world", portal.getLocation().getWorld());
        portalObject.put("loc_x", portal.getLocation().getX());
        portalObject.put("loc_y", portal.getLocation().getY());
        portalObject.put("loc_z", portal.getLocation().getZ());
        portalObject.put("yaw", portal.getLocation().getYaw());
        if (portal.getTarget() != null) {
            portalObject.put("target", portal.getTarget().toString());
        }
        collection.insert(portalObject);

        PortalManager.getInstance().getPortals().add(portal);
    }

    /**
     * deletes a portal and resets the target of all portals it was connected to
     *
     * @param portal a portal
     */
    public void deletePortal(Portal portal) {
        DBCollection collection = MongoConnector.getInstance().getCollection();

        // find all portals who have this portal as target
        BasicDBObject query = new BasicDBObject("target", portal.getId().toString());
        DBCursor dbCursor = collection.find(query);
        while (dbCursor.hasNext()) {
            DBObject portalObject = dbCursor.next();
            portalObject.removeField("target");
            UUID targetId = UUID.fromString((String) portalObject.get("portal_id"));
            collection.update(query, portalObject);
            Portal p = PortalManager.getInstance().getPortal(targetId);
            if (p != null) {
                p.setTarget(null);
            }
        }

        // remove the object itself
        BasicDBObject portalObject = new BasicDBObject("portal_id", portal.getId().toString());
        collection.remove(portalObject);

        PortalManager.getInstance().getPortals().remove(portal);
    }

    /**
     * sets the portals target
     *
     * @param portal1 the source portal
     * @param portal2 the target the portal shall be connected to
     */
    public void link(Portal portal1, Portal portal2) {
        DBCollection collection = MongoConnector.getInstance().getCollection();
        BasicDBObject query = new BasicDBObject("portal_id", portal1.getId().toString());
        DBCursor dbCursor = collection.find(query);
        if (dbCursor.hasNext()) {
            DBObject portalObject = dbCursor.next();
            portalObject.put("target", portal2.getId().toString());
            collection.update(query, portalObject);
            portal1.setTarget(portal2.getId());
        }
    }

    /**
     * locks a portal and makes it available to only the builder
     *
     * @param portal a portal
     */
    @Override
    public void lock(Portal portal) {
        DBCollection collection = MongoConnector.getInstance().getCollection();
        BasicDBObject query = new BasicDBObject("portal_id", portal.getId().toString());
        DBCursor dbCursor = collection.find(query);
        if (dbCursor.hasNext()) {
            DBObject portalObject = dbCursor.next();
            portalObject.put("locked", true);
            portal.setLocked(true);
            collection.update(query, portalObject);
        }
    }

    /**
     * unlocks a portal and makes it available to all players
     *
     * @param portal a portal
     */
    @Override
    public void unlock(Portal portal) {
        DBCollection collection = MongoConnector.getInstance().getCollection();
        BasicDBObject query = new BasicDBObject("portal_id", portal.getId().toString());
        DBCursor dbCursor = collection.find(query);
        if (dbCursor.hasNext()) {
            DBObject portalObject = dbCursor.next();
            portalObject.put("locked", false);
            portal.setLocked(false);
            collection.update(query, portalObject);
        }
    }

    /**
     * disabled all portals on a given world
     *
     * @param worldName the name of the world
     * @param server    the server the sender is on
     * @return the number of portals that were disabled
     */
    public int disableWorld(String worldName, Server server) {
        DBCollection collection = MongoConnector.getInstance().getCollection();
        BasicDBObject query = new BasicDBObject("world", worldName);
        query.append("server", server.getAddress().getHostName() + "."
                + server.getAddress().getPort());
        query.append("enabled", true);
        DBCursor dbCursor = collection.find(query);
        int i = 0;
        while (dbCursor.hasNext()) {
            i++;
            DBObject portalObject = dbCursor.next();
            portalObject.put("enabled", false);
            PortalManager.getInstance().removePortal(UUID.fromString(portalObject.get("portal_id").toString()));
            collection.update(query, portalObject);
        }
        return i;
    }

    /**
     * enables all portals on a given world if they are disabled
     *
     * @param worldName the name of the world
     * @param server    the server the sender is on
     * @return the number of portals that were disabled
     */
    public int enableWorld(String worldName, Server server) {
        DBCollection collection = MongoConnector.getInstance().getCollection();
        BasicDBObject query = new BasicDBObject("world", worldName);
        query.append("server", server.getAddress().getHostName() + "."
                + server.getAddress().getPort());
        query.append("enabled", false);
        DBCursor dbCursor = collection.find(query);
        int i = 0;
        while (dbCursor.hasNext()) {
            i++;
            DBObject portalObject = dbCursor.next();
            portalObject.put("enabled", true);
            collection.update(query, portalObject);
            PortalManager.getInstance().getPortals().add(createPortal(portalObject));
        }
        return i;
    }

    /**
     * creates a portal from a dbObject
     *
     * @param portalObject a mongo object
     * @return a portal object
     */
    private Portal createPortal(DBObject portalObject) {
        return new Portal(
                UUID.fromString((String) portalObject.get("portal_id")),
                UUID.fromString((String) portalObject.get("builder")),
                (String) portalObject.get("name"),
                (Boolean) portalObject.get("enabled"),
                (Boolean) portalObject.get("locked"),
                new Location(
                        (String) portalObject.get("server"),
                        (String) portalObject.get("world"),
                        (Double) portalObject.get("loc_x"),
                        (Double) portalObject.get("loc_y"),
                        (Double) portalObject.get("loc_z"),
                        (Double) portalObject.get("yaw")),
                portalObject.get("target") != null ? UUID.fromString((String) portalObject.get("target")) : null);
    }
}
