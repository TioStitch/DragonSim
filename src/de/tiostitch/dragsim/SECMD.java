package de.tiostitch.dragsim;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SECMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("dragonsimulator.se")) {
				if (args.length == 0) {
					p.getInventory().addItem(Altar.eyeOfEnder);
					p.sendMessage("§5[DragSim] §aYou gaved a 1§7x §5Eye of Ender!");
					return false;
				}

				int i = Integer.parseInt(args[0]);
				ItemStack eyeOfEnder = new ItemStack(Material.SKULL_ITEM, i, (byte) 3);

				eyeOfEnder = Utils.setTexture(eyeOfEnder, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFhOGZjOGRlNjQxN2I0OGQ0OGM4MGI0NDNjZjUzMjZlM2Q5ZGE0ZGJlOWIyNWZjZDQ5NTQ5ZDk2MTY4ZmMwIn19fQ==");

				ItemMeta eoem = eyeOfEnder.getItemMeta();
				eoem.setDisplayName(DragonSimulator.getCfg().getString("items.summoning-eyer.displayName"));
				eoem.setLore(DragonSimulator.getCfg().getStringList("items.summoning-eyer.lore"));
				eyeOfEnder.setItemMeta(eoem);

				p.getInventory().addItem(eyeOfEnder);
				p.sendMessage("§5[DragSim] §aYou gaved a " + i + "§7x §5Eyes of Ender!");
			}
		}
		return false;
	}
}
