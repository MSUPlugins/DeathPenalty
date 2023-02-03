package vip.floatationdevice.msu.deathpenalty;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static vip.floatationdevice.msu.deathpenalty.ConfigManager.*;

public class RespawnEventListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        Player p = e.getPlayer();
        DeathPenalty.instance.getServer().getScheduler().runTask(DeathPenalty.instance, new Runnable()
        {
            @Override
            public void run()
            {
                if(setHp && !p.hasPermission("deathpenalty.bypass.hp"))
                    if(hpIsPercent)
                        p.setHealth(Math.max(p.getMaxHealth() * hpPercent, 1));
                    else
                        p.setHealth(Math.min(hp, p.getMaxHealth()));
                if(setFood && !p.hasPermission("deathpenalty.bypass.food"))
                {
                    p.setSaturation(0f);
                    p.setFoodLevel(food);
                }
                if(setXp && !p.hasPermission("deathpenalty.bypass.xp"))
                {
                    p.setTotalExperience((int) (p.getTotalExperience() * xpPercent));
                    p.setLevel((int) (p.getLevel() * xpPercent));
                }
                if(setMoney && EconomyManager.available && !p.hasPermission("deathpenalty.bypass.money"))
                    EconomyManager.setBalance(p, EconomyManager.getBalance(p) * moneyPercent);
            }
        });
    }
}
