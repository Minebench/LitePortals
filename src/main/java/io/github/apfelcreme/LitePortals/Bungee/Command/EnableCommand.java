package io.github.apfelcreme.LitePortals.Bungee.Command;

import io.github.apfelcreme.LitePortals.Bungee.LitePortals;
import io.github.apfelcreme.LitePortals.Bungee.LitePortalsConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
public class EnableCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.hasPermission("LitePortals.mod")) {
            if (args.length > 1) {
                LitePortals.getInstance().getDatabaseController().enableWorld(args[1], player.getServer());
                List<String> disabledWorlds = LitePortalsConfig.getInstance().getDisabledWorlds(
                        player.getServer().getAddress().getHostName() + "." + player.getServer().getAddress().getPort());
                if (disabledWorlds.contains(args[1])) {
                    disabledWorlds.remove(args[1]);
                    LitePortalsConfig.getInstance().getConfiguration().set("disabledWorlds." +
                            player.getServer().getAddress().getHostName() + "." + player.getServer().getAddress().getPort()
                            , disabledWorlds);
                    LitePortalsConfig.getInstance().save();
                }
                LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                        .getText("info.enable.enabled").replace("{0}", args[1]));
            } else {
                LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                        .getText("error.wrongUsage").replace("{0}", "/portal enable <Welt>"));
            }
        } else {
            LitePortals.sendMessage(player.getUniqueId(), LitePortalsConfig.getInstance()
                    .getText("error.noPermission"));
        }
    }
}
