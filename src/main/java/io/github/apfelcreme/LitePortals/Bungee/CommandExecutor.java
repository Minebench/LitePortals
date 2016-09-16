package io.github.apfelcreme.LitePortals.Bungee;

import io.github.apfelcreme.LitePortals.Bungee.Command.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

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
public class CommandExecutor extends Command {

    public CommandExecutor(String name) {
        super(name);
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] strings) {
        LitePortals.getInstance().getProxy().getScheduler().runAsync(LitePortals.getInstance(), new Runnable() {
            public void run() {
                if (commandSender instanceof ProxiedPlayer) {
                    SubCommand subCommand = null;
                    if (strings.length > 0) {
                        Operation operation = Operation.getOperation(strings[0]);
                        if (operation != null) {
                            switch (operation) {
                                case CREATE:
                                    subCommand = new CreateCommand();
                                    break;
                                case DISABLE:
                                    subCommand = new DisableCommand();
                                    break;
                                case ENABLE:
                                    subCommand = new EnableCommand();
                                    break;
                                case INFO:
                                    subCommand = new InfoCommand();
                                    break;
                                case LINK:
                                    subCommand = new LinkCommand();
                                    break;
                                case LIST:
                                    subCommand = new ListCommand();
                                    break;
                                case LOCK:
                                    subCommand = new LockCommand();
                                    break;
                                case RELOAD:
                                    subCommand = new ReloadCommand();
                                    break;
                                case UNLOCK:
                                    subCommand = new UnlockCommand();
                                    break;
                            }
                        } else {
                            LitePortals.sendMessage(((ProxiedPlayer)commandSender).getUniqueId(),
                                    LitePortalsConfig.getInstance().getText("error.unknownCommand")
                                            .replace("{0}", strings[0]));
                        }
                    } else{
                        subCommand = new HelpCommand();
                    }
                    if (subCommand != null) {
                        subCommand.execute(commandSender, strings);
                    }
                }
            }
        });
    }

    /**
     * all possible subcommands
     */
    private enum Operation {
        CREATE, DISABLE, ENABLE, INFO, LINK, LIST, LOCK, RELOAD, UNLOCK;

        /**
         * returns the matching operation with no case sensitivity
         * @param string a string
         * @return an operation
         */
        public static Operation getOperation(String string) {
            for (Operation operation : Operation.values()) {
                if (operation.name().equalsIgnoreCase(string)) {
                    return operation;
                }
            }
            return null;
        }
    }
}
