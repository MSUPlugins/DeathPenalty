package vip.floatationdevice.msu.deathpenalty;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class DeathPenalty extends JavaPlugin
{
    static DeathPenalty instance;
    static Logger log;
    static DPConfigManager cm;

    @Override
    public void onEnable()
    {
        instance = this;
        log = getLogger();
        cm = new DPConfigManager(this, 1).initialize();

        getServer().getPluginManager().registerEvents(new RespawnEventListener(), this);

        log.info("DeathPenalty loaded");
    }

    @Override
    public void onDisable()
    {
        log.info("DeathPenalty is being disabled");
    }
}
