package tictim.paraglider.event;
import net.minecraft.client.Minecraft;
import tictim.paraglider.ParagliderClientMod;
import tictim.paraglider.client.screen.ParagliderSettingsScreen;
public final class ParagliderClientEventHandler {
 public static void onClientTick(){
  var mc=Minecraft.getInstance();
  if(mc.screen==null && ParagliderClientMod.instance().getParagliderSettingsKey().consumeClick() && (org.lwjgl.glfw.GLFW.glfwGetKey(mc.getWindow().handle(),org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL)==org.lwjgl.glfw.GLFW.GLFW_PRESS || org.lwjgl.glfw.GLFW.glfwGetKey(mc.getWindow().handle(),org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL)==org.lwjgl.glfw.GLFW.GLFW_PRESS))mc.setScreen(new ParagliderSettingsScreen());
 }
}
