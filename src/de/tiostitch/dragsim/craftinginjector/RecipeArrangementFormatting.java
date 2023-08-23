 package de.tiostitch.dragsim.craftinginjector;

 import java.util.ArrayList;
 import org.bukkit.inventory.ItemStack;


 public class RecipeArrangementFormatting
 {
   private ItemStack is;
   private int x;
   private int y;

   public RecipeArrangementFormatting(ItemStack is, int x, int y) {
     this.is = is;
     this.x = x;
     this.y = y;
   }

   public ItemStack getItemStack() {
     return this.is;
   }

   public int getX() {
     return this.x;
   }

   public int getY() {
     return this.y;
   }

   public boolean isField() {
     return (this.x < 3 && this.y < 3);
   }

   public void normalize() {
     for (; this.x >= 3; this.x -= 3);
     for (; this.y >= 3; this.y -= 3);
   }

   public static void printCoords(ArrayList<RecipeArrangementFormatting> recipeArrangementFormatting) {
     for (int i = 0; i < recipeArrangementFormatting.size(); i++) {
       if (recipeArrangementFormatting.get(i) != null) {
         System.out.println(String.valueOf(((RecipeArrangementFormatting)recipeArrangementFormatting.get(i)).getX()) + "|" + ((RecipeArrangementFormatting)recipeArrangementFormatting.get(i)).getY() + "|" + ((RecipeArrangementFormatting)recipeArrangementFormatting.get(i)).getItemStack());
       } else {
         System.out.println("null");
       }
     }
   }

   public static void printCoords(RecipeArrangementFormatting[] coords) {
     for (int i = 0; i < coords.length; i++) {
       if (coords[i] != null) {
         System.out.println(String.valueOf(coords[i].getX()) + "|" + coords[i].getY() + "|" + coords[i].getItemStack());
       } else {
         System.out.println("null");
       }
     }
   }
 }