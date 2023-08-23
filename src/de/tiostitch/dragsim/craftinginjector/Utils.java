 package de.tiostitch.dragsim.craftinginjector;

 import com.mojang.authlib.GameProfile;
 import com.mojang.authlib.properties.Property;
 import java.lang.reflect.Field;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.HashMap;
 import org.bukkit.Material;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.meta.ItemMeta;
 import org.bukkit.inventory.meta.SkullMeta;



 public class Utils
 {
   public static char getChar(int i) {
     if (i == 0) return '0';
     if (i == 1) return '1';
     if (i == 2) return '2';
     if (i == 3) return '3';
     if (i == 4) return '4';
     if (i == 5) return '5';
     if (i == 6) return '6';
     if (i == 7) return '7';
     if (i == 8) return '8';
     if (i == 9) return '9';
     return '0';
   }

   public static String ingredientsMapToString(HashMap<Integer, ItemStack> ings) {
     return ingredientsMapToString(ings, false, true);
   }

   public static String ingredientsMapToStringIgnoreSlot(HashMap<Integer, ItemStack> ings, boolean ignoreSlot) {
     return ingredientsMapToString(ings, ignoreSlot, true);
   }



   private static String ingredientsMapToString(HashMap<Integer, ItemStack> ings, boolean ignoreSlot, boolean ignoreAmount) {
     StringBuilder test = new StringBuilder();
     for (int i = 1; i <= 9; i++) {
       ItemStack is = ings.get(Integer.valueOf(i));
       if (is != null)
       {
         if (is.hasItemMeta()) {
           if (ignoreSlot) {
             test.append("[t=" + is.getType().toString());
           } else {
             test.append(String.valueOf(i) + "=[t=" + is.getType().toString());
           }
           if (!ignoreAmount) {
             test.append(",amt=" + is.getAmount());
           }
           test.append(",d=" + is.getData().getData());
           ItemMeta im = is.getItemMeta();
           if (im.hasDisplayName()) test.append(",dm=" + im.getDisplayName());
           if (im.hasLore()) {
             test.append(",l=[");
             im.getLore().forEach(lore -> {
                 }); test.append("]");
           }
           if (is.getType().name().toLowerCase().contains("skull_item")) {
             String tex = null;
             try {
               tex = getTexure(is);
             }
             catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException e1) {
               System.out.println("Error while converting item to string");
               e1.printStackTrace();
             }
             if (tex != null) test.append(",tx=" + tex);
           }
           test.append("];");
         } else {
           if (!ignoreAmount) {
             test.append(",amt=" + is.getAmount());
           }
           if (ignoreSlot) {
             test.append("[t=" + is.getType().toString() + ",d=" + is.getData().getData() + "];");
           } else {
             test.append(String.valueOf(i) + "=[t=" + is.getType().toString() + ",d=" + is.getData().getData() + "];");
           }
         }
       }
     }

     String t = test.toString();
     t = t.replace(";]", "]");
     return t;
   }


   public static ArrayList<HashMap<Integer, ItemStack>> getShaped(HashMap<Integer, ItemStack> ings, ItemStack res) {
     ArrayList<HashMap<Integer, ItemStack>> rs = new ArrayList<>();
     ItemStack[][] test = new ItemStack[3][3];
     ItemStack[] line1 = new ItemStack[3];
     ItemStack[] line2 = new ItemStack[3];
     ItemStack[] line3 = new ItemStack[3]; int i;
     for (i = 0; i < 3; ) { line1[i] = ings.get(Integer.valueOf(i + 1)); i++; }
      for (i = 0; i < 3; ) { line2[i] = ings.get(Integer.valueOf(i + 4)); i++; }
      for (i = 0; i < 3; ) { line3[i] = ings.get(Integer.valueOf(i + 7)); i++; }
      test[0] = line1;
     test[1] = line2;
     test[2] = line3;
     ItemStack[] ete = new ItemStack[9]; int j;
     for (j = 0; j < 3; ) { ete[j] = test[0][j]; j++; }
      for (j = 0; j < 3; ) { ete[j + 3] = test[1][j]; j++; }
      for (j = 0; j < 3; ) { ete[j + 6] = test[2][j]; j++; }

     RecipeArrangementFormatting[] coords = new RecipeArrangementFormatting[9];

     for (int k = 0; k < 9; k++) {
       RecipeArrangementFormatting c = new RecipeArrangementFormatting(ete[k], (k == 0 || k == 3 || k == 6) ? 0 : ((k == 1 || k == 4 || k == 7) ? 1 : 2), (k < 3) ? 0 : ((k < 6) ? 1 : 2));
       coords[k] = c;
     }

     ArrayList<ArrayList<RecipeArrangementFormatting>> allPossibilities = new ArrayList<>();



     for (int x = 0; x < 3; x++) {
       for (int y = 0; y < 3; y++) {
         ArrayList<RecipeArrangementFormatting> cods = new ArrayList<>();

         boolean valid = true;
         for (int c = 0; c < 9; c++) {
           if (coords[c] != null && valid && coords[c].getItemStack() != null) {
             RecipeArrangementFormatting newCoords = new RecipeArrangementFormatting(coords[c].getItemStack(), coords[c].getX() + x, coords[c].getY() + y);
             if (newCoords.isField()) {
               cods.add(newCoords);
             } else {
               valid = false;
             }
           }
         }
         if (valid) {
           allPossibilities.add(cods);
         }
       }
     }

     for (ArrayList<RecipeArrangementFormatting> cods : allPossibilities) {
       rs.add(coordsToMap(cods));
     }

     return rs;
   }


   public static HashMap<Integer, ItemStack> coordsToMap(ArrayList<RecipeArrangementFormatting> recipeArrangementFormatting) {
     HashMap<Integer, ItemStack> map = new HashMap<>();
     if (recipeArrangementFormatting.isEmpty()) return map;
     for (RecipeArrangementFormatting c : recipeArrangementFormatting) {
       int slot = 0;
       if (c.getY() < 1) {
         if (c.getX() == 0) slot = 1;
         if (c.getX() == 1) slot = 2;
         if (c.getX() == 2) slot = 3;

       } else if (c.getY() < 2) {
         if (c.getX() == 0) slot = 4;
         if (c.getX() == 1) slot = 5;
         if (c.getX() == 2) slot = 6;

       } else if (c.getY() < 3) {
         if (c.getX() == 0) slot = 7;
         if (c.getX() == 1) slot = 8;
         if (c.getX() == 2) slot = 9;

       }

       map.put(Integer.valueOf(slot), c.getItemStack());
     }
     return map;
   }



   public static String itemStackToString(ItemStack is) {
     StringBuilder test = new StringBuilder();

     if (is == null || is.getType().equals(Material.AIR)) return "";

     if (is.hasItemMeta()) {
       test.append("[t=" + is.getType().toString());
       test.append("d=" + is.getData().getData());
       ItemMeta im = is.getItemMeta();
       if (im.hasDisplayName()) test.append(",dm=" + im.getDisplayName());
       if (im.hasLore()) {
         test.append(",l=[");
         im.getLore().forEach(lore -> {
             }); test.append("]");
       }
       if (is.getType().equals(Material.valueOf("SKULL_ITEM"))) {
         String tex = null;
         try {
           tex = getTexure(is);
         }
         catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException e1) {
           System.out.println("Error while converting item to string");
           e1.printStackTrace();
         }
         if (tex != null) test.append(",tx=" + tex);
       }
       test.append("]");
     } else {
       test.append("[t=" + is.getType().toString() + ",d=" + is.getData().getData() + "]");
     }
     String t = test.toString();
     t = t.replace(";]", "]");
     return t;
   }

   public static String getTexure(ItemStack pet) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
     String texture = null;
     if (!pet.getType().name().toLowerCase().contains("skull_item")) return null;
     SkullMeta sm = (SkullMeta)pet.getItemMeta();
     Field profileField = sm.getClass().getDeclaredField("profile");
     profileField.setAccessible(true);
     GameProfile profile = (GameProfile)profileField.get(sm);
     Collection<Property> textures = profile.getProperties().get("textures");
     for (Property p : textures) {
       texture = p.getValue();
     }
     return texture;
   }
 }