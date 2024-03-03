package vip.floatationdevice.msu.deathpenalty;

import org.bukkit.plugin.java.JavaPlugin;
import vip.floatationdevice.msu.ConfigManager;

public class DPConfigManager extends ConfigManager
{
    boolean setHp, setFood, setXp, setMoney, hpIsPercent;
    int hp, food;
    double hpPercent, xpPercent, moneyPercent;

    public DPConfigManager(JavaPlugin plugin, int version)
    {
        super(plugin, version);
    }

    @Override
    public DPConfigManager initialize()
    {
        super.initialize();

        // check if deduct hp is enabled
        if(setHp = get(Boolean.class, "hp.enabled"))
        {
            String hpSetTo;
            // hp.setTo is positive integer
            if(is(Integer.class, "hp.setTo") && ((hp = get(Integer.class, "hp.setTo")) > 0))
            {
                hpIsPercent = false;
            }
            // hp.setTo is String representation between "1%" and "100%"
            else if(is(String.class, "hp.setTo") && (hpSetTo = get(String.class, "hp.setTo")).matches("^(100|[1-9][0-9]?)%$"))
            {
                hpIsPercent = true;
                hpPercent = Integer.parseInt(hpSetTo.substring(0, hpSetTo.length() - 1)) / 100.0; // drop the '%' and assign the value
            }
            else
                throw new RuntimeException("'hp.setTo' must be positive integer or string between \"1%\" and \"100%\"");
        }

        // check if deduct food is enabled
        if(setFood = get(Boolean.class, "food.enabled"))
        {
            food = get(Integer.class, "food.setTo");
            if(food < 0 || food > 20)
                throw new RuntimeException("'food.setTo' must be integer between 0 and 20");
        }

        // check if deduct xp is enabled
        if(setXp = get(Boolean.class, "xp.enabled"))
        {
            xpPercent = get(Number.class, "xp.setToPercent").doubleValue() / 100.0;
            if(xpPercent < 0.0 || xpPercent > 1.0)
                throw new RuntimeException("'xp.setTo' must be between 0 and 100");
        }

        // check if deduct money is enabled. requires Vault
        if(setMoney = get(Boolean.class, "money.enabled"))
        {
            if(EconomyManager.initialize())
            {
                moneyPercent = get(Number.class, "money.setToPercent").doubleValue() / 100.0;
                if(moneyPercent < 0.0 || moneyPercent > 1.0)
                    throw new RuntimeException("'money.setTo' must be between 0 and 100");
            }
        }

        return this;
    }
}
