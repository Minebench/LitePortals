package io.github.apfelcreme.LitePortals.Bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
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
public class LitePortalsConfig {

    /**
     * the singleton instance
     */
    private static LitePortalsConfig instance = null;

    /**
     * the configuration
     */
    private Configuration configuration;
    /**
     * the language configuration
     */
    private Configuration languageConfiguration;

    /**
     * the configuration provider
     */
    private final static ConfigurationProvider yamlProvider = ConfigurationProvider
            .getProvider(YamlConfiguration.class);

    /**
     * constructor
     */
    private LitePortalsConfig() {
        File configurationFile = new File(LitePortals.getInstance().getDataFolder().getAbsoluteFile() + "/config.yml");
        File languageConfigurationFile = new File(LitePortals.getInstance().getDataFolder().getAbsoluteFile() + "/lang.de.yml");
        try {
            if (!LitePortals.getInstance().getDataFolder().exists()) {
                LitePortals.getInstance().getDataFolder().mkdir();
            }
            if (!configurationFile.exists()) {
                createConfigFile("config.yml", configurationFile);
            }
            if (!languageConfigurationFile.exists()) {
                createConfigFile("lang.de.yml", languageConfigurationFile);
            }
            configuration = yamlProvider.load(configurationFile);
            languageConfiguration = yamlProvider.load(languageConfigurationFile);

            for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) {
                if (configuration.get("disabledWorlds." + serverInfo.getAddress().getHostName() + "."
                        + serverInfo.getAddress().getPort()) == null) {
                    configuration.set("disabledWorlds." + serverInfo.getAddress().getHostName() + "."
                            + serverInfo.getAddress().getPort(), "");
                }
            }
            yamlProvider.save(configuration, configurationFile);
            yamlProvider.save(languageConfiguration, languageConfigurationFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reloads both configs
     */
    public void reload() {
        instance = null;
        getInstance();
    }

    /**
     * saves both configs
     */
    public void save() {
        File configurationFile = new File(LitePortals.getInstance().getDataFolder().getAbsoluteFile() + "/config.yml");
        File languageConfigurationFile = new File(LitePortals.getInstance().getDataFolder().getAbsoluteFile() + "/lang.de.yml");
        try {
            yamlProvider.save(configuration, configurationFile);
            yamlProvider.save(languageConfiguration, languageConfigurationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * copies a resource to the data folder
     *
     * @param source the resource file name
     * @param dest   the destination file
     * @throws IOException
     */
    private void createConfigFile(String source, File dest) throws IOException {
        Configuration configuration = yamlProvider.load(new InputStreamReader(LitePortals.getInstance().getResourceAsStream(source)));
        yamlProvider.save(configuration, dest);
    }

    /**
     * returns the configuration
     *
     * @return the configuration
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * returns the languageConfiguration
     *
     * @return the languageConfiguration
     */
    public Configuration getLanguageConfiguration() {
        return languageConfiguration;
    }


    /**
     * returns the list of disabled worlds on a server
     *
     * @return the list of disabled worlds on a server
     */
    public List<String> getDisabledWorlds(String serverIp) {
        return configuration.getStringList("disabledWorlds." + serverIp);
    }

    /**
     * checks if a world is on the list of blocked worlds
     *
     * @param worldName a world name
     * @param serverIp  a server ip
     * @return true or false
     */
    public boolean isWorldDisabled(String worldName, String serverIp) {
        List<String> disabledWorlds = getDisabledWorlds(serverIp);
        return disabledWorlds.contains(worldName);
    }

    /**
     * returns a text from the language config
     *
     * @param key the key
     * @return a text
     */
    public String getText(String key) {
        String ret = languageConfiguration.getString("texts." + key);
        if (ret != null) {
            ret = ChatColor.translateAlternateColorCodes('&', ret);
            ret = ChatColor.translateAlternateColorCodes('§', ret);
            return Normalizer.normalize(ret, Normalizer.Form.NFKC);
        }
        return ChatColor.GRAY + "Missing text key: " + key;

    }

    /**
     * returns the singleton instance
     *
     * @return the singleton instance
     */
    public static LitePortalsConfig getInstance() {
        if (instance == null) {
            instance = new LitePortalsConfig();
        }
        return instance;
    }
}
