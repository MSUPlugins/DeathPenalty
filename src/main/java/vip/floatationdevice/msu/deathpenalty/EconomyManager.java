package vip.floatationdevice.msu.deathpenalty;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager
{
    private static Economy eco;
    static boolean available = false;

    static boolean initialize()
    {
        if(DeathPenalty.instance.getServer().getPluginManager().getPlugin("Vault") != null)
        {
            RegisteredServiceProvider<Economy> rsp = DeathPenalty.instance.getServer().getServicesManager().getRegistration(Economy.class);
            if(rsp != null)
            {
                eco = rsp.getProvider();
                if(eco != null) available = true;
            }
        }
        if(!available) DeathPenalty.log.warning("Vault economy not found, setting money will not work");
        return available;
    }

    static double getBalance(Player player)
    {
        if(!available) throw new IllegalStateException("Economy is not available");
        return eco.getBalance(player);
    }

    static double setBalance(Player player, double balance)
    {
        if(!available) throw new IllegalStateException("Economy is not available");
        double d = balance - eco.getBalance(player);
        if(d < 0)
        {
            EconomyResponse resp = eco.withdrawPlayer(player, -d);
            if(resp.transactionSuccess()) return eco.getBalance(player);
            else throw new RuntimeException("Failed to set balance for " + player.getName() + ": " + resp.errorMessage);
        }
        return eco.getBalance(player);
    }
}
