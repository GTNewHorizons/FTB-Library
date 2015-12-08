package ftb.lib.mod.client;

import java.util.UUID;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.*;
import ftb.lib.*;
import ftb.lib.api.config.ClientConfigRegistry;
import ftb.lib.api.gui.*;
import ftb.lib.client.FTBLibClient;
import ftb.lib.mod.*;
import latmod.lib.LMColorUtils;
import latmod.lib.config.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class FTBLibModClient extends FTBLibModCommon
{
	public static final ConfigGroup clientConfig = new ConfigGroup("ftbl");
	public static final ConfigEntryBool addOreNames = new ConfigEntryBool("item_ore_names", false);
	public static final ConfigEntryBool addRegistryNames = new ConfigEntryBool("item_reg_names", false);
	public static final ConfigEntryBool displayDebugInfo = new ConfigEntryBool("debug_info", false);
	public static final ConfigEntryBool openHSB = new ConfigEntryBool("open_hsb_cg", false).setHidden();
	public static final ConfigEntryEnum<EnumScreen> notifications = new ConfigEntryEnum<EnumScreen>("notifications", EnumScreen.class, EnumScreen.values(), EnumScreen.SCREEN, false);
	
	public void preInit()
	{
		JsonHelper.initClient();
		EventBusHelper.register(FTBLibClientEventHandler.instance);
		EventBusHelper.register(FTBLibRenderHandler.instance);
		ClientConfigRegistry.init();
		LMGuiHandlerRegistry.add(FTBLibGuiHandler.instance);
		
		if(FTBLibFinals.DEV) clientConfig.add(displayDebugInfo);
		else displayDebugInfo.set(false);
		
		ClientConfigRegistry.add(clientConfig.addAll(FTBLibModClient.class));
	}
	
	public boolean isShiftDown() { return GuiScreen.isShiftKeyDown(); }
	public boolean isCtrlDown() { return GuiScreen.isCtrlKeyDown(); }
	public boolean isTabDown() { return Keyboard.isKeyDown(Keyboard.KEY_TAB); }
	public boolean inGameHasFocus() { return FTBLibClient.mc.inGameHasFocus; }
	
	public EntityPlayer getClientPlayer()
	{ return FMLClientHandler.instance().getClientPlayerEntity(); }
	
	public EntityPlayer getClientPlayer(UUID id)
	{ return FTBLibClient.getPlayerSP(id); }
	
	public World getClientWorld()
	{ return FMLClientHandler.instance().getWorldClient(); }
	
	public double getReachDist(EntityPlayer ep)
	{
		if(ep == null) return 0D;
		else if(ep instanceof EntityPlayerMP) return super.getReachDist(ep);
		PlayerControllerMP c = FTBLibClient.mc.playerController;
		return (c == null) ? 0D : c.getBlockReachDistance();
	}
	
	public void spawnDust(World w, double x, double y, double z, int col)
	{
		EntityReddustFX fx = new EntityReddustFX(w, x, y, z, 0F, 0F, 0F);
		
		float alpha = LMColorUtils.getAlpha(col) / 255F;
		float red = LMColorUtils.getRed(col) / 255F;
		float green = LMColorUtils.getGreen(col) / 255F;
		float blue = LMColorUtils.getBlue(col) / 255F;
		if(alpha == 0F) alpha = 1F;
		
		fx.setRBGColorF(red, green, blue);
		fx.setAlphaF(alpha);
		FTBLibClient.mc.effectRenderer.addEffect(fx);
	}
	
	public boolean openClientGui(EntityPlayer ep, String mod, int id, NBTTagCompound data)
	{
		LMGuiHandler h = LMGuiHandlerRegistry.get(mod);
		
		if(h != null)
		{
			GuiScreen g = h.getGui(ep, id, data);
			
			if(g != null)
			{
				FTBLibClient.mc.displayGuiScreen(g);
				return true;
			}
		}
		
		return false;
	}
	
	public void openClientTileGui(EntityPlayer ep, IGuiTile t, NBTTagCompound data)
	{
		if(ep != null && t != null)
		{
			GuiScreen g = t.getGui(ep, data);
			if(g != null) FTBLibClient.mc.displayGuiScreen(g);
		}
	}
}