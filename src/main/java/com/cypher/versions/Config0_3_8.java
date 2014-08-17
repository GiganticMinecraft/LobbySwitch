package com.cypher.versions;

import com.cypher.config.ConfigPaths;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Derek on 8/16/2014.
 * Time: 11:45 AM
 */
public class Config0_3_8 {

    public static FileConfiguration update(FileConfiguration fileConfiguration) {
        for (String string : fileConfiguration.getConfigurationSection(ConfigPaths.SERVER_SLOTS).getKeys(false)) {
            fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_METADATA, Integer.valueOf(string)), 0);
        }
        fileConfiguration.set(ConfigPaths.VERSION, "0.3.8");
        return fileConfiguration;
    }

    public static boolean equals(FileConfiguration fileConfiguration) {
        if (fileConfiguration.contains(ConfigPaths.VERSION)) {
            if (fileConfiguration.getString(ConfigPaths.VERSION).equals("0.3.8")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}