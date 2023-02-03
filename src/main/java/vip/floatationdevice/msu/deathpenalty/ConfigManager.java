package vip.floatationdevice.msu.deathpenalty;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager
{
    public static final int CONFIG_VERSION = 1;
    static YamlConfiguration cfg;
    static boolean setHp, setFood, setXp, setMoney, hpIsPercent;
    static int hp, food;
    static double hpPercent, xpPercent, moneyPercent;

    static void initialize() throws InvalidConfigurationException
    {
        DeathPenalty.log.info("Loading configurations");
        File cfgFile = new File(DeathPenalty.instance.getDataFolder(), "config.yml");
        if(!cfgFile.exists()) DeathPenalty.instance.saveResource("config.yml", false);
        cfg = YamlConfiguration.loadConfiguration(cfgFile);

        // check config version
        if(cfg.getInt("version", Integer.MIN_VALUE) != CONFIG_VERSION)
            DeathPenalty.log.warning("Incorrect configuration version: expected " + CONFIG_VERSION + " but found " + cfg.getInt("version", Integer.MIN_VALUE));

        // load values from config
        // hp
        setHp = cfg.getBoolean("hp.enabled", false);
        if(setHp)
        {
            String s_hpTo = cfg.getString("hp.setTo", "10");
            if(s_hpTo.charAt(s_hpTo.length() - 1) == '%')
            {
                hpIsPercent = true;
                hpPercent = Double.parseDouble(s_hpTo.substring(0, s_hpTo.length() - 1)) / 100;
                if(hpPercent < 0.01 || hpPercent > 1)
                    throw new InvalidConfigurationException("'hp.setTo' must be between 1% and 100%");
            }
            else
            {
                hpIsPercent = false;
                hp = Integer.parseInt(s_hpTo);
                if(hp < 1)
                    throw new InvalidConfigurationException("'hp.setTo' must be positive");
            }
        }
        // food
        setFood = cfg.getBoolean("food.enabled", false);
        if(setFood)
        {
            food = cfg.getInt("food.setTo", 10);
            if(food < 0 || food > 20)
                throw new InvalidConfigurationException("'food.setTo' must be between 0 and 20");
        }
        // xp
        setXp = cfg.getBoolean("xp.enabled", false);
        if(setXp)
        {
            xpPercent = cfg.getDouble("xp.setToPercent", 50) / 100;
            if(xpPercent < 0 || xpPercent > 1)
                throw new InvalidConfigurationException("'xp.setTo' must be between 0 and 100");
        }
        // money
        setMoney = cfg.getBoolean("money.enabled", false);
        if(setMoney)
        {
            if(EconomyManager.initialize())
            {
                moneyPercent = cfg.getDouble("money.setToPercent", 90) / 100;
                if(moneyPercent < 0 || moneyPercent > 1)
                    throw new InvalidConfigurationException("'money.setTo' must be between 0 and 100");
            }
        }

        DeathPenalty.log.info("Configurations OK");
    }
}
