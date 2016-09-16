package io.github.apfelcreme.LitePortals.Bungee.Command;

import io.github.apfelcreme.LitePortals.Bungee.LitePortals;
import io.github.apfelcreme.LitePortals.Bungee.LitePortalsConfig;
import io.github.apfelcreme.LitePortals.Bungee.Portal.Portal;
import io.github.apfelcreme.LitePortals.Bungee.PortalManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
public class CreateCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.hasPermission("LitePortals.user")) {
            Portal portal = PortalManager.getInstance().getLatestConstructions().get(player.getUniqueId());
            if (portal != null) {

                //check for duplicate name and duplicate location
                boolean nameInUse = false;
                for (Portal p : PortalManager.getInstance().getPortals()) {
                    if (p.getBuilder().equals(player.getUniqueId()) && p.getName().equals(args[1])) {
                        nameInUse = true;
                    }
                }
                if (!nameInUse) {
                    if (args.length > 1) {
                        if (LitePortalsConfig.getInstance().getConfiguration().getInt("portalLimit") < 0
                                || PortalManager.getInstance().getPlayerPortals(player.getUniqueId()).size()
                                < LitePortalsConfig.getInstance().getConfiguration().getInt("portalLimit")) {
                            portal.setName(args[1]);
                            LitePortals.getInstance().getDatabaseController().savePortal(portal);
                            PortalManager.getInstance().getLatestConstructions().remove(portal.getBuilder());
                            LitePortals.sendMessage(player.getUniqueId(),
                                    LitePortalsConfig.getInstance().getText("info.create.created")
                                            .replace("{0}", args[1]));
                        } else {
                            LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                                    .getText("error.portalLimit")
                                    .replace("{0}", Integer.toString(LitePortalsConfig.getInstance()
                                            .getConfiguration().getInt("portalLimit"))));
                        }
                    } else {
                        LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                                .getText("error.wrongUsage").replace("{0}", "/portal create <name>"));
                    }
                } else {
                    LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                            .getText("error.nameAlreadyInUse"));
                }
            } else {
                LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                        .getText("error.noPortalBuilt"));
            }
        } else {
            LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                    .getText("error.noPermission"));
        }
    }
}
