package tictim.paraglider.fabric;
import net.minecraft.world.item.*;
import tictim.paraglider.contents.Contents;
import tictim.paraglider.contents.recipe.WaterBottleIngredientType;
public final class ParagliderBrewing {
 public static boolean isElixir(ItemStack s){var c=Contents.get();return s.is(c.energizingElixir1())||s.is(c.energizingElixir2())||s.is(c.energizingElixir3())||s.is(c.enduringElixir1())||s.is(c.enduringElixir2())||s.is(c.enduringElixir3());}
 public static boolean isIngredient(ItemStack s){return s.is(Items.CARROT)||s.is(Items.GOLDEN_CARROT)||s.is(Items.GLOWSTONE_DUST);}
 public static ItemStack mix(ItemStack input,ItemStack reagent){var c=Contents.get();Item out=null;
 if(WaterBottleIngredientType.INSTANCE.test(input)&&reagent.is(Items.CARROT))out=c.energizingElixir2();
 else if(input.is(c.energizingElixir2())&&reagent.is(Items.GLOWSTONE_DUST))out=c.energizingElixir3();
 else if(input.is(c.energizingElixir2())&&reagent.is(Items.GOLDEN_CARROT))out=c.enduringElixir2();
 else if(input.is(c.enduringElixir2())&&reagent.is(Items.GLOWSTONE_DUST))out=c.enduringElixir3();
 else if(input.is(c.energizingElixir3())&&reagent.is(Items.GOLDEN_CARROT))out=c.enduringElixir3();
 return out==null?ItemStack.EMPTY:new ItemStack(out);}
}
