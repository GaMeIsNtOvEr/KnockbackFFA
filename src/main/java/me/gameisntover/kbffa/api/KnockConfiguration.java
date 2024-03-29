package me.gameisntover.kbffa.api;

import lombok.SneakyThrows;
import me.gameisntover.kbffa.KnockbackFFA;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public abstract class KnockConfiguration {

    public File getFile;
    public FileConfiguration getConfig;

    @SneakyThrows
    public KnockConfiguration() {
        getFile = new File(KnockbackFFA.getInstance().getDataFolder(), getName() + ".yml");
        if (!KnockbackFFA.getInstance().getDataFolder().exists()) KnockbackFFA.getInstance().getDataFolder().mkdir();
        if (!getFile.exists()) {
            getFile.canExecute();
            if (getResourceName() != null) KnockbackFFA.getInstance().saveResource(getResourceName(), false);
        }
        getConfig = YamlConfiguration.loadConfiguration(getFile);

    }

    public abstract String getName();

    public abstract String getResourceName();

    public void reload() {
        getConfig = YamlConfiguration.loadConfiguration(getFile);
    }


    @SneakyThrows
    public void save() {
        getConfig.save(getFile);
    }
}
