package vip.floatationdevice.msu.deathpenalty;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static vip.floatationdevice.msu.deathpenalty.DeathPenalty.cm;

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
                if(cm.setHp && !p.hasPermission("deathpenalty.bypass.hp"))
                    if(cm.hpIsPercent)
                        p.setHealth(Math.max(p.getMaxHealth() * cm.hpPercent, 1));
                    else
                        p.setHealth(Math.min(cm.hp, p.getMaxHealth()));
                if(cm.setFood && !p.hasPermission("deathpenalty.bypass.food"))
                {
                    p.setSaturation(0f);
                    p.setFoodLevel(cm.food);
                }
                if(cm.setXp && !p.hasPermission("deathpenalty.bypass.xp"))
                {
                    p.setTotalExperience((int) (p.getTotalExperience() * cm.xpPercent));
                    p.setLevel((int) (p.getLevel() * cm.xpPercent));
                }
                if(cm.setMoney && EconomyManager.available && !p.hasPermission("deathpenalty.bypass.money"))
                    EconomyManager.setBalance(p, EconomyManager.getBalance(p) * cm.moneyPercent);
            }
        });
    }
}
