package io.github.apfelcreme.LitePortals.Bungee.Listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.github.apfelcreme.LitePortals.Bungee.BukkitMessenger;
import io.github.apfelcreme.LitePortals.Bungee.LitePortals;
import io.github.apfelcreme.LitePortals.Bungee.LitePortalsConfig;
import io.github.apfelcreme.LitePortals.Bungee.Portal.Location;
import io.github.apfelcreme.LitePortals.Bungee.Portal.Portal;
import io.github.apfelcreme.LitePortals.Bungee.PortalManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
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
public class BukkitMessageListener implements Listener {


    /**
     * listener for the bukkit communication channel
     *
     * @param event an event object
     * @throws IOException
     */
    @EventHandler
    public void onPluginMessageReceived(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equals("LitePortals")) {
            return;
        }
        if (!(event.getSender() instanceof Server)) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();
        if (subChannel.equals("PORTALWASBUILT")) {
            Portal portal = new Portal(UUID.fromString(in.readUTF()), new Location(in.readUTF(),
                    in.readUTF(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble()));
            portalWasBuilt(portal);
        } else if (subChannel.equals("PLAYERBROKEPORTAL")) {
            UUID breaker = UUID.fromString(in.readUTF());
            Location location = new Location(in.readUTF(), in.readUTF(),
                    in.readDouble(), in.readDouble(), in.readDouble(), null);
            portalGotDestroyed(breaker, location);
        } else if (subChannel.equals("PLAYERUSEDPORTAL")) {
            UUID player = UUID.fromString(in.readUTF());
            Location location = new Location(in.readUTF(),
                    in.readUTF(), in.readDouble(), in.readDouble(), in.readDouble(), null);
            Portal portal = PortalManager.getInstance().portalExists(location);
            teleport(player, portal);
        } else if (subChannel.equals("POSITIONANSWER")) {
            UUID player = UUID.fromString(in.readUTF());
            boolean portalExists = in.readBoolean();
            if (portalExists) {
                // there is a portal or a portal structure there
                Location location = new Location(in.readUTF(),
                        in.readUTF(), in.readDouble(), in.readDouble(), in.readDouble(), null);
                printInfo(player, location);
            } else {
                LitePortals.sendMessage(player, LitePortalsConfig.getInstance().getText("error.notStandingInAPortal"));
            }
        }
    }

    /**
     * searches the portal list whether there is a portal at the given location and prints some info
     *
     * @param player   a player uuid
     * @param location a location
     */
    private void printInfo(UUID player, Location location) {
        Portal portal = PortalManager.getInstance().portalExists(location);
        if (portal != null) {
            // there is a portal
            LitePortals.sendMessage(player, portal.getInfo());
        } else {
            // there may be a portal structure that has not been registered
            portal = PortalManager.getInstance().structureExists(location);
            if (portal != null) {
                LitePortals.sendMessage(player, LitePortalsConfig.getInstance().getText("error.unregisteredPortal"));
            } else {
                LitePortals.sendMessage(player, LitePortalsConfig.getInstance().getText("error.notStandingInAPortal"));
            }
        }
    }

    /**
     * a portal was built
     *
     * @param portal a portal
     */
    private void portalWasBuilt(Portal portal) {
        boolean locationInUse = false;
        for (Portal p : PortalManager.getInstance().getPortals()) {
            if (p.getLocation().equals(portal.getLocation())) {
                locationInUse = true;
            }
        }
        if (!locationInUse) {
            if (!LitePortalsConfig.getInstance().isWorldDisabled(portal.getLocation().getWorld(), portal.getLocation().getServer())) {
                PortalManager.getInstance().getLatestConstructions().put(portal.getBuilder(), portal);
                LitePortals.sendMessage(portal.getBuilder(), LitePortalsConfig.getInstance().getText("info.portal.built"));
            } else {
                LitePortals.sendMessage(portal.getBuilder(), LitePortalsConfig.getInstance()
                        .getText("error.worldDisabled"));
            }
        } else {
            LitePortals.sendMessage(portal.getBuilder(), LitePortalsConfig.getInstance()
                    .getText("error.noDoublePortals"));
        }
    }

    /**
     * a portal was destroyed
     *
     * @param breaker  the player who broke the portal
     * @param location a location
     */
    private void portalGotDestroyed(UUID breaker, Location location) {
        Portal portal = PortalManager.getInstance().portalExists(location);
        if (portal != null) {
            LitePortals.getInstance().getLogger().info("Player '" + breaker.toString() + "' broke Portal '" + portal.getName() + "'");
            LitePortals.getInstance().getDatabaseController().deletePortal(portal);
            LitePortals.sendMessage(portal.getBuilder(), LitePortalsConfig.getInstance().getText("info.portal.broken"));
        } else {
            Portal portalStructure = PortalManager.getInstance().getLatestConstruction(location);
            if (portalStructure != null) {
                LitePortals.sendMessage(breaker, LitePortalsConfig.getInstance().getText("info.portal.brokenStructure"));
                PortalManager.getInstance().getLatestConstructions().remove(portalStructure.getBuilder());
            }
        }
    }

    /**
     * teleports a player
     *
     * @param player a players uuid
     * @param source the source the player came from
     */
    private void teleport(UUID player, Portal source) {
        if (source != null) {
            if (!source.isLocked() || player.equals(source.getBuilder())) {
                Portal target = PortalManager.getInstance().getPortal(source.getTarget());
                if (target != null) {
                    if (!target.isLocked() || player.equals(target.getBuilder())) {
                        BukkitMessenger.getInstance().teleportPlayer(player, target.getLocation());
                    } else {
                        LitePortals.sendMessage(player, LitePortalsConfig.getInstance()
                                .getText("error.targetLocked"));
                    }
                } else {
                    LitePortals.sendMessage(player, LitePortalsConfig.getInstance()
                            .getText("error.portalNotLinked"));
                }
            } else {
                LitePortals.sendMessage(player, LitePortalsConfig.getInstance()
                        .getText("error.locked"));
            }
        }
    }
}
