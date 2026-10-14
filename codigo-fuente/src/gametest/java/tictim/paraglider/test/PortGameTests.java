package tictim.paraglider.test;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.InteractionHand;
import tictim.paraglider.contents.Contents;
import tictim.paraglider.api.movement.Movement;
import tictim.paraglider.api.stamina.Stamina;
import tictim.paraglider.api.vessel.VesselContainer;
import tictim.paraglider.impl.movement.ServerPlayerMovement;
import tictim.paraglider.ParagliderUtils;
import tictim.paraglider.fabric.ParagliderBrewing;
import net.minecraft.world.level.storage.*;
import net.minecraft.util.ProblemReporter;
public class PortGameTests {
 @GameTest public void elixirBrewing(GameTestHelper h){var c=Contents.get();var brewing=PotionBrewing.bootstrap(FeatureFlags.DEFAULT_FLAGS);
 var water=PotionContents.createItemStack(Items.POTION,Potions.WATER);
 h.assertTrue(brewing.hasMix(water,new ItemStack(Items.CARROT)),"Water + carrot recipe");
 var e=brewing.mix(new ItemStack(Items.CARROT),water);h.assertTrue(e.is(c.energizingElixir2()),"Actual brewing output");
 h.assertTrue(brewing.mix(new ItemStack(Items.GLOWSTONE_DUST),e).is(c.energizingElixir3()),"Elixir upgrade");
 h.assertTrue(brewing.mix(new ItemStack(Items.GOLDEN_CARROT),e).is(c.enduringElixir2()),"Enduring conversion");
 h.assertTrue(!ParagliderBrewing.mix(PotionContents.createItemStack(Items.POTION,Potions.AWKWARD),new ItemStack(Items.CARROT)).is(c.energizingElixir2()),"Reject non-water input");h.succeed();}
 @GameTest public void statueRecipes(GameTestHelper h){var c=Contents.get();var recipes=h.getLevel().recipeAccess().recipes.byType(c.bargainRecipeType());
 h.assertTrue(recipes.size()>=6,"All goddess and horned statue bargains loaded");
 var player=h.makeMockServerPlayerInLevel();player.getInventory().add(new ItemStack(c.spiritOrb(),64));
 var buyHeart=recipes.stream().filter(r->r.id().identifier().getPath().equals("goddess_statue/heart_container")).findFirst().orElseThrow();
 int before=VesselContainer.get(player).heartContainer();buyHeart.value().bargain(player,false);
 h.assertTrue(VesselContainer.get(player).heartContainer()==before+1,"Statue heart purchase");h.succeed();}
 @GameTest public void vesselsAndPersistence(GameTestHelper h){var c=Contents.get();var player=h.makeMockServerPlayerInLevel();var m=(ServerPlayerMovement)Movement.get(player);m.update();
 float health=player.getMaxHealth();double stamina=Stamina.get(player).maxStamina();var vessels=VesselContainer.get(player);
 vessels.giveHeartContainers(2,false,false);vessels.giveStaminaVessels(2,false,false);m.update();
 h.assertTrue(player.getMaxHealth()>health,"Heart containers increase max health");
 h.assertTrue(Stamina.get(player).maxStamina()>stamina,"Stamina vessels increase capacity");
 var output=TagValueOutput.createWithContext(ProblemReporter.DISCARDING,h.getLevel().registryAccess());player.saveWithoutId(output);var tag=output.buildResult();
 var restored=h.makeMockServerPlayerInLevel();restored.load(TagValueInput.create(ProblemReporter.DISCARDING,h.getLevel().registryAccess(),tag));
 h.assertTrue(VesselContainer.get(restored).heartContainer()==2,"Saved heart containers reload");
 h.assertTrue(VesselContainer.get(restored).staminaVessel()==2,"Saved stamina vessels reload");h.succeed();}
 @GameTest public void gliderMovement(GameTestHelper h){var player=h.makeMockServerPlayerInLevel();var c=Contents.get();var stack=new ItemStack(c.paraglider());
 player.setItemInHand(InteractionHand.MAIN_HAND,stack);player.setPos(player.getX(),200,player.getZ());player.setOnGround(false);player.setDeltaMovement(0,-1,0);
 var movement=(ServerPlayerMovement)Movement.get(player);movement.update();Stamina.get(player).setStamina(Stamina.get(player).maxStamina(),true);double before=Stamina.get(player).stamina();movement.setParagliding(true);movement.update();
 h.assertTrue(movement.state().paragliding(),"Server activates paraglider state");
 h.assertTrue(ParagliderUtils.getCaps(stack).isParagliding(stack),"Item animation flag active");
 h.assertTrue(player.getDeltaMovement().y > -1,"Fall slowed by planeador");
 h.assertTrue(Stamina.get(player).stamina()<before,"Gliding consumes stamina");h.succeed();}
 @GameTest public void spiritOrbLoot(GameTestHelper h){var params=new LootParams.Builder(h.getLevel()).withParameter(LootContextParams.ORIGIN,Vec3.ZERO).withParameter(LootContextParams.BLOCK_STATE,Blocks.SPAWNER.defaultBlockState()).withParameter(LootContextParams.TOOL,ItemStack.EMPTY).create(LootContextParamSets.BLOCK);
 var drops=LootTable.EMPTY.getRandomItems(params);h.assertTrue(drops.stream().anyMatch(s->s.is(Contents.get().spiritOrb())),"Spawner produces spirit orbs through native Fabric loot hook");h.succeed();}
 @GameTest public void bargainNetworkCodec(GameTestHelper h){var preview=new tictim.paraglider.contents.recipe.preview.VesselPreview(tictim.paraglider.contents.recipe.preview.VesselPreview.VesselType.HEART_CONTAINER,3);
 var buf=new net.minecraft.network.RegistryFriendlyByteBuf(io.netty.buffer.Unpooled.buffer(),h.getLevel().registryAccess());
 try{tictim.paraglider.network.NetUtils.BARGAIN_PREVIEW.encode(buf,preview);var decoded=tictim.paraglider.network.NetUtils.BARGAIN_PREVIEW.decode(buf);h.assertTrue(preview.equals(decoded),"Statue preview network round trip");}finally{buf.release();}h.succeed();}
}
