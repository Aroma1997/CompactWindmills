/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package aroma1997.compactwindmills;


import ic2.api.item.IC2Items;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import aroma1997.core.log.LogHelper;
import aroma1997.core.util.AromaRegistry;
import aroma1997.core.version.VersionCheck;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.ModID, name = Reference.ModName, dependencies = "required-after:IC2;required-after:Aroma1997Core")
/**
 * 
 * @author Aroma1997
 *
 */
public class CompactWindmills {
	
	@Instance(Reference.ModID)
	public static CompactWindmills instance;
	
	@SidedProxy(clientSide = "aroma1997.compactwindmills.ClientProxy", serverSide = "aroma1997.compactwindmills.CommonProxy")
	public static CommonProxy proxy;
	
	public static Block windMill;
	
	public Logger windMillLog;
	
	public static final CreativeTabs creativeTabCompactWindmills = new CreativeTabCompactWindmills();
	
	public static final int updateTick = 64;
	
	public static boolean vanillaIC2Stuff;
	
	public static boolean debugMode;
	
	private Configuration config;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		windMillLog = LogHelper.genNewLogger(Reference.ModID);
		File tmpFile = new File(event.getModConfigurationDirectory(), "CompactWindmills.cfg");
		File configFile = new File(new File(event.getModConfigurationDirectory(), "aroma1997"), "CompactWindmills.cfg");
		if (tmpFile.exists()) {
			LogHelper.log(Level.INFO, "Moving old config file to aroma1997 folder.");
			tmpFile.renameTo(configFile);
		}
		
		Configuration config = new Configuration(
			configFile);
		this.config = config;
		config.load();
		Property vanillaIC2 = config.get(Configuration.CATEGORY_GENERAL, "useIC2Stuff", false);
		vanillaIC2.comment = "This defines if this mod just acts as a compact version of the vanilla IC2 Windmill or not."
			+ "'true' means IC2 version, 'false' means, the mod will change some stuff in its own windmills (recommended)"
			+ "This changes for example if the windmills require a rotor or if the wind strength is variable. This will also change the recipe.";
		vanillaIC2Stuff = vanillaIC2.getBoolean(false);
		
		config.save();
		
		windMill = new BlockCompactWindmill();
		
		RotorType.initRotors();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.registerBlock(windMill, ItemCompactWindMill.class, "blockCompactWindmill");
		
		for (RotorType rotor : RotorType.values()) {
			GameRegistry.registerItem(rotor.getItem(), rotor.name());
		}
		for (WindType typ : WindType.values()) {
			GameRegistry.registerTileEntity(typ.claSS, typ.tileEntityName());
		}
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(windMill, 1, WindType.ELV.ordinal()), false, " W ", "WTW", " W ", 'W',
			IC2Items.getItem("windMill"), 'T', IC2Items.getItem("lvTransformer"));
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(windMill, 1, WindType.LV.ordinal()), false, " W ", "WTW", " W ", 'W',
			new ItemStack(windMill, 1, 0), 'T', IC2Items.getItem("transformerUpgrade"));
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(windMill, 1, WindType.MV.ordinal()), false, " W ", "WTW", " W ", 'W',
			new ItemStack(windMill, 1, 1), 'T', IC2Items.getItem("transformerUpgrade"));
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(windMill, 1, WindType.HV.ordinal()), false, " W ", "WTW", " W ", 'W',
			new ItemStack(windMill, 1, 2), 'T', IC2Items.getItem("transformerUpgrade"));
		AromaRegistry.registerShapedAromicRecipe(new ItemStack(windMill, 1, WindType.EV.ordinal()), false, " W ", "WTW", " W ", 'W',
			new ItemStack(windMill, 1, 3), 'T', IC2Items.getItem("transformerUpgrade"));
		if (! vanillaIC2Stuff) {
			AromaRegistry.registerShapedAromicRecipe(new ItemStack(RotorType.CARBON.getItem()), false, "CCC", "CMC", "CCC",
				'C', IC2Items.getItem("carbonPlate"), 'M', IC2Items.getItem("machine"));
			AromaRegistry.registerShapedAromicRecipe(new ItemStack(RotorType.ALLOY.getItem()), false, "AAA", "AMA", "AAA",
				'A', IC2Items.getItem("advancedAlloy"), 'M', IC2Items.getItem("machine"));
			AromaRegistry.registerShapedAromicRecipe(new ItemStack(RotorType.WOOD.getItem()), false, "PSP", "SIS", "PSP",
				'S', "stickWood", 'I', "plateIron", 'p',
				"plankWood");
			AromaRegistry.registerShapedAromicRecipe(new ItemStack(RotorType.IRIDIUM.getItem()), false, " I ", "IRI", " I ",
				'I', IC2Items.getItem("iridiumPlate"), 'R', new ItemStack(RotorType.CARBON.getItem()));
			AromaRegistry.registerShapedAromicRecipe(new ItemStack(RotorType.WOOL.getItem()), false, "SWS", "WRW", "SWS",
				'S', new ItemStack(Items.string), 'W', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), 'R', new ItemStack(
					RotorType.WOOD.getItem()));
		}
		VersionCheck.registerVersionChecker(Reference.ModID, Reference.VERSION);
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.registerRotorRenderer(config);
	}
}
