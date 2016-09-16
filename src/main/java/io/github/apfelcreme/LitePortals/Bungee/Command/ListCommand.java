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
public class ListCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.hasPermission("LitePortals.user")) {
            UUID uuid;
            if (args.length > 1) {
                uuid = LitePortals.getInstance().getUUIDByName(args[1]);
            } else {
                uuid = player.getUniqueId();
            }
            if (uuid != null) {
                LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance().getText("info.list.header"));
                int i = 0;
                for (Portal portal : PortalManager.getInstance().getPlayerPortals(uuid)) {
                    i++;
                    Portal target = PortalManager.getInstance().getPortal(portal.getTarget());
                    if (target != null) {
                        LitePortals.sendMessage(player.getUniqueId(),
                                LitePortalsConfig.getInstance().getText("info.list.element")
                                        .replace("{0}", Integer.toString(i))
                                        .replace("{1}", portal.getName())
                                        .replace("{2}", portal.isLocked() ? LitePortalsConfig.getInstance().getText("info.lock.symbol.locked")
                                                : LitePortalsConfig.getInstance().getText("info.lock.symbol.notLocked"))
                                        .replace("{3}", target.getName())
                                        .replace("{4}", target.isLocked() ? LitePortalsConfig.getInstance().getText("info.lock.symbol.locked")
                                                : LitePortalsConfig.getInstance().getText("info.lock.symbol.notLocked"))
                                        .replace("{5}", target.getBuilder().equals(portal.getBuilder()) ? ""
                                                : "(" + LitePortals.getInstance().getNameByUUID(target.getBuilder()) + ")"));
                    } else {
                        LitePortals.sendMessage(player.getUniqueId(),
                                LitePortalsConfig.getInstance().getText("info.list.element")
                                        .replace("{0}", Integer.toString(i))
                                        .replace("{1}", portal.getName())
                                        .replace("{2}", portal.isLocked() ? LitePortalsConfig.getInstance().getText("info.lock.symbol.locked")
                                                : LitePortalsConfig.getInstance().getText("info.lock.symbol.notLocked"))
                                        .replace("{3}", LitePortalsConfig.getInstance().getText("info.list.none"))
                                        .replace("{4}", "")
                                        .replace("{5}", ""));
                    }

                }
            } else {
                LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                        .getText("error.unknownPlayer"));
            }
        } else {
            LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                    .getText("error.noPermission"));
        }
    }
}
