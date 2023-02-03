package vip.floatationdevice.msu.deathpenalty;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class DeathPenalty extends JavaPlugin
{
    static DeathPenalty instance;
    static Logger log;

    @Override
    public void onEnable()
    {
        instance = this;
        log = getLogger();
        try
        {
            ConfigManager.initialize();
            getServer().getPluginManager().registerEvents(new RespawnEventListener(), this);
            log.info("DeathPenalty is ready");
        }
        catch(Exception e)
        {
            log.severe("Failed to initialize DeathPenalty");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable()
    {
        // nothing to do
    }
}
