 package de.tiostitch.dragsim.craftinginjector;

 import de.tr7zw.nbtapi.NBTItem;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.UUID;
 import org.bukkit.Bukkit;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.Recipe;
 import org.bukkit.inventory.ShapedRecipe;
 import org.bukkit.inventory.ShapelessRecipe;
 import org.bukkit.inventory.meta.ItemMeta;
 import org.bukkit.material.MaterialData;



 public class IRecipe
 {
   private static Map<String, IRecipe> recipes = new HashMap<>();
   public static Map<String, IRecipe> recipesWithTexture = new HashMap<>();

   private final ItemStack result;
   private final ArrayList<ItemStack> shapelessIngs;
   private final HashMap<Integer, ItemStack> shapedIngs;
   private final boolean shaped;
   private ArrayList<Recipe> rs = new ArrayList<>();
   private UUID uuid;
   private final String name;
   private ArrayList<HashMap<Integer, ItemStack>> shapedPossiblities = new ArrayList<>();

   public static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

   public IRecipe(ItemStack result, ArrayList<ItemStack> shapeless, String name) {
     this.result = result;
     this.shapedIngs = null;
     this.shapelessIngs = shapeless;
     this.shaped = false;
     this.uuid = UUID.randomUUID();
     this.name = name;

     ItemStack is = result.clone();
     ItemMeta im = is.getItemMeta();
     im.setDisplayName("IRecipe: " + name);
     is.setItemMeta(im);
     ShapelessRecipe sr = new ShapelessRecipe(is);
     boolean texture = false;
     for (ItemStack ing : shapeless) {
       sr.addIngredient(new MaterialData(ing.getType(), ing.getData().getData()));
       try {
         if (Utils.getTexure(ing) != null) texture = true;

       } catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException e) {
         e.printStackTrace();
       }
     }
     if (!texture) {
       this.rs.add(sr);
       recipes.put(name, this);
       try { Bukkit.getServer().addRecipe((Recipe)sr); } catch (IllegalStateException illegalStateException) {}
     } else {
       System.out.println("Didn't register Recipe " + name + " (Ingridients have texture)");
     }
   }

   public IRecipe(ItemStack result, HashMap<Integer, ItemStack> shapedIngs, String name) {
     this.result = result;
     this.shapedIngs = shapedIngs;
     this.shapelessIngs = null;
     this.shaped = true;
     this.uuid = UUID.randomUUID();
     this.name = name;

     ItemStack is = result.clone();
     ItemMeta im = is.getItemMeta();
     im.setDisplayName("IRecipe: " + name);
     is.setItemMeta(im);
     boolean texture = false;
     for (HashMap<Integer, ItemStack> map : Utils.getShaped(shapedIngs, null)) {

       ShapedRecipe sr = new ShapedRecipe(is);
       sr.shape(new String[] { "123", "456", "789" });
       for (Iterator<Integer> iterator = map.keySet().iterator(); iterator.hasNext(); ) { int i = ((Integer)iterator.next()).intValue();
         ItemStack t = map.get(Integer.valueOf(i));
         NBTItem nbt = new NBTItem(t);
         nbt.setString("irecipe", Utils.ingredientsMapToStringIgnoreSlot(shapedIngs, true));
         map.put(Integer.valueOf(i), nbt.getItem());

         sr.setIngredient(Utils.getChar(i), ((ItemStack)map.get(Integer.valueOf(i))).getType());
         try {
           if (Utils.getTexure(map.get(Integer.valueOf(i))) != null) texture = true;

         } catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException e) {
           e.printStackTrace();
         }  }

       if (!texture) {
         this.rs.add(sr);
         this.shapedPossiblities.add(map);
         try { Bukkit.getServer().addRecipe((Recipe)sr); } catch (IllegalStateException illegalStateException) {}
       }
     }
     if (!texture) {
       recipes.put(name, this);
     } else {
       System.out.println("Didn't register Recipe " + name + " (Ingridients have texture)");
     }
   }



   public ArrayList<HashMap<Integer, ItemStack>> getShapedPossibilities() {
     return this.shapedPossiblities;
   }

   public String getName() {
     return this.name;
   }

   public ArrayList<Recipe> getRecipe() {
     return this.rs;
   }

   public boolean isShaped() {
     return this.shaped;
   }

   public ItemStack getResult() {
     return this.result;
   }

   public ArrayList<ItemStack> getShapelessIngs() {
     return this.shapelessIngs;
   }

   public HashMap<Integer, ItemStack> getShapedIngs() {
     return this.shapedIngs;
   }

   public static IRecipe getRecipe(String id) {
     return recipes.get(id);
   }

   public UUID getUuid() {
     return this.uuid;
   }
 }