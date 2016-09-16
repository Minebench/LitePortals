package io.github.apfelcreme.LitePortals.Bungee.Command;

import io.github.apfelcreme.LitePortals.Bungee.LitePortals;
import io.github.apfelcreme.LitePortals.Bungee.LitePortalsConfig;
import io.github.apfelcreme.LitePortals.Bungee.Portal.Portal;
import io.github.apfelcreme.LitePortals.Bungee.PortalManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
public class LinkCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.hasPermission("LitePortals.user")) {
            if (args.length > 2) {
                Portal portal1 = PortalManager.getInstance().getPortal(player.getUniqueId(), args[1]);
                if (portal1 != null) {
                    UUID targetPlayer = player.getUniqueId();
                    String portalName = args[2];
                    if (portalName.contains(":")) {
                        // e.g. /portal link Portal1 Lord36:Portal2
                        targetPlayer = LitePortals.getInstance().getUUIDByName(args[2].split(":")[0]);
                        portalName = portalName.split(":")[1];
                        if (targetPlayer == null) {
                            LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                                    .getText("error.unknownPlayer").replace("{0}", args[2].split(":")[0]));
                            return;
                        }
                    }
                    Portal portal2 = PortalManager.getInstance().getPortal(targetPlayer, portalName);
                    if (portal2 != null) {
                        if (player.getUniqueId().equals(portal2.getBuilder()) || !portal2.isLocked()) {
                            LitePortals.getInstance().getDatabaseController().link(portal1, portal2);
                            LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                                    .getText("info.link.linked")
                                    .replace("{0}", args[1])
                                    .replace("{1}", args[2]));
                        } else {
                            LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                                    .getText("error.locked"));
                        }
                    } else {
                        LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                                .getText("error.unknownPortal").replace("{0}", args[2]));
                    }
                } else {
                    LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                            .getText("error.unknownPortal").replace("{0}", args[1]));
                }
            } else {
                LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                        .getText("error.wrongUsage").replace("{0}", "/portal link <Name 1> <Name 2>"));
            }
        } else {
            LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                    .getText("error.noPermission"));
        }
    }
}
