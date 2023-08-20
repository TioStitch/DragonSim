 package de.tiostitch.dragsim.craftinginjector;

 import java.util.ArrayList;
 import org.bukkit.Material;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.Listener;
 import org.bukkit.event.inventory.PrepareItemCraftEvent;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.meta.ItemMeta;


 public class RecipeInjector
   implements Listener
 {
   private static boolean isMetaEquals(ItemStack is1, ItemStack is2) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
     if (!is1.getType().equals(Material.SKULL_ITEM)) return is1.getItemMeta().equals(is2.getItemMeta());
     if (Utils.getTexure(is1) != null && Utils.getTexure(is2) != null) {
       ItemMeta im1 = is1.getItemMeta();
       ItemMeta im2 = is2.getItemMeta();
       if ((im1.getDisplayName() == null && im2.getDisplayName() != null) || (im1.getLore() == null && im2.getLore() != null)) return false;
       return Utils.getTexure(is1).equals(Utils.getTexure(is2));
     }
     return is1.getItemMeta().equals(is2.getItemMeta());
   }


   @EventHandler
   public void onItemPrepare(PrepareItemCraftEvent e) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
     if (e.getRecipe() == null) {
       return;
     }






























     ItemStack result = e.getRecipe().getResult();
     if (result == null) {
       return;
     }
     if (result.hasItemMeta()) {
       IRecipe r = IRecipe.getRecipe(result.getItemMeta().getDisplayName().replace("IRecipe: ", ""));
       if (r == null) {
         boolean hasMeta = e.getInventory().getContents()[0].hasItemMeta();
         for (int j = 1; j <= 9; j++) {
           if (!e.getInventory().getContents()[j].hasItemMeta() && hasMeta) e.getInventory().setResult(null);
         }
         return;
       }
       ArrayList<ItemStack> test = new ArrayList<>();
       ArrayList<ItemStack> test2 = new ArrayList<>(); int i;
       for (i = 0; i < 9; i++) {
         if (!e.getInventory().getContents()[i + 1].getType().equals(Material.AIR)) test.add(e.getInventory().getContents()[i + 1]);
         if (r.getShapedIngs().containsKey(Integer.valueOf(i)) && r.getShapedIngs().get(Integer.valueOf(i)) != null && !((ItemStack)r.getShapedIngs().get(Integer.valueOf(i))).getType().equals(Material.AIR)) test2.add(r.getShapedIngs().get(Integer.valueOf(i)));
       }
       if (r.isShaped()) {

         for (i = 0; i < test.size(); i++) {
           if (test2.size() > i && !Utils.itemStackToString(test.get(i)).equals(Utils.itemStackToString(test2.get(i)))) {
             e.getInventory().setResult(null);
             return;
           }
         }
         e.getInventory().setResult(r.getResult());
       } else {
         boolean matches = true;
         for (int j = 0; j < test.size(); j++) {
           ItemStack is1 = test.get(j);
           if (is1 != null && !is1.getType().equals(Material.AIR))
             for (int t = 0; t < test2.size(); t++) {
               ItemStack is2 = test2.get(t);
               boolean found = false;
               if (!found &&
                 is2 != null && !is2.getType().equals(Material.AIR))
                 if (Utils.itemStackToString(is2).equals(Utils.itemStackToString(is1)))
                 { found = true;
                   test2.remove(t);
                   t = test2.size() - 1;
                    }

                 else if (t == test2.size() - 1) { matches = false; }

             }
         }
         if (!matches) {
           e.getInventory().setResult(null);
         } else {
           e.getInventory().setResult(r.getResult());
         }
       }
     }
   }
 }