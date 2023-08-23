package de.tiostitch.dragsim.armors;

import de.tiostitch.dragsim.DragonSimulator;
import de.tiostitch.dragsim.LightingStrikeControllPhasse;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LightningRunnable extends BukkitRunnable {

    private final JavaPlugin plugin;
    int cooldown = DragonSimulator.getCfg().getInt("armor-abilities.unstable-cooldown");
    int damage = DragonSimulator.getCfg().getInt("armor-abilities.unstable-damage");
    String itemName = DragonSimulator.getCfg().getString("armor-abilities.unstable-checkName");

    public LightningRunnable(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (hasUnstableBloodSet(p)) {
                List<Entity> entities = p.getNearbyEntities(5, 5, 5);
                if (entities.size() == 0) {
                    return;
                }
                LivingEntity entity = (LivingEntity) entities.get(0);
                if (!(entity != null || entity.getType().equals(EntityType.PLAYER) ||
                                entity.getType().equals(EntityType.DROPPED_ITEM) ||
                                entity.getType().equals(EntityType.ARMOR_STAND) ||
                                entity.getType().equals(EntityType.ITEM_FRAME))) {
                    return;
                }

                LightingStrikeControllPhasse.sendLightning(p, entity.getLocation());
                entity.damage(15000);
            }
        }
    }

    public void start() {
        this.runTaskTimer(plugin, 0, cooldown * 20L);
    }

    private boolean hasUnstableBloodSet(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        if (hasUnstableBloodName(helmet) && hasUnstableBloodName(chestplate) &&
                hasUnstableBloodName(leggings) && hasUnstableBloodName(boots)) {
            return true;
        }

        return false;
    }

    private boolean hasUnstableBloodName(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                && item.getItemMeta().getDisplayName() != null) {
            return item.getItemMeta().getDisplayName().contains(itemName);
        }
        return false;
    }
}