/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package aroma1997.compactwindmills;

import ic2.api.item.Items;

import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import aroma1997.compactwindmills.helpers.LogHelper;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = Reference.ModID, name = Reference.ModName, version = Reference.Version)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
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

	private static int blockID;
	public static Block windMill;
	public static int updateTick;
	public static final CreativeTabs creativeTabCompactWindmills = new CreativeTabCompactWindmills(
			"creativeTabCW");
	public static boolean vanillaIC2Stuff;
	public static boolean debugMode;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		LogHelper.init();
		if (Reference.Version == "@VERSION" + "@") {
			debugMode = true;
			LogHelper.log(Level.INFO, "Turning on debug mode.");
		}
		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		config.load();
		Property block = config.getBlock("CompactWindmill", 2790);
		block.comment = "This is the id of the Compact Windmill Blocks.";
		blockID = block.getInt(2790);
		Property ticks = config.get(Configuration.CATEGORY_GENERAL,
				"WaitTicks", 64);
		ticks.comment = "This is the amount of ticks, the windmills will wait, until they update their efficiency count."
				+ "Note: The lower the number is, the more lag it will cause."
				+ "Also note, that they always produce their EU-Count per tick, not only, when they update their efficiency count.";
		updateTick = ticks.getInt(64);
		RotorType.getConfigs(config);
		Property vanillaIC2 = config.get(Configuration.CATEGORY_GENERAL,
				"useIC2Stuff", false);
		vanillaIC2.comment = "This defines if this mod just acts as a compact version of the vanilla IC2 Windmill or not."
				+ "'true' means IC2 version, 'false' means, the mod will change some stuff in its own windmills (recommended)"
				+ "This changes for example if the windmills require a rotor or if the wind strength is variable. This will also change the recipe.";
		vanillaIC2Stuff = vanillaIC2.getBoolean(false);

		config.save();

		RotorType.initRotors();

		windMill = new BlockCompactWindmill(blockID);
	}

	@Init
	public void load(FMLInitializationEvent event) {

		GameRegistry.registerBlock(windMill, ItemCompactWindMill.class,
				"blockCompactWindmill");
		for (WindType typ : WindType.values()) {
			GameRegistry.registerTileEntity(typ.claSS, typ.tileEntityName());
		}
		if (!vanillaIC2Stuff) {
			GameRegistry.addShapedRecipe(new ItemStack(windMill, 1, 0), " W ",
					"WTW", " W ", 'W', Items.getItem("windMill"), 'T',
					Items.getItem("lvTransformer"));
		}
		GameRegistry.addShapedRecipe(new ItemStack(windMill, 1, 1), " W ",
				"WTW", " W ", 'W', new ItemStack(windMill, 1, 0), 'T',
				Items.getItem("transformerUpgrade"));
		if (vanillaIC2Stuff) {
			GameRegistry.addShapedRecipe(new ItemStack(windMill, 1, 0), " W ",
					"WTW", " W ", 'W', Items.getItem("windMill"), 'T',
					Items.getItem("transformerUpgrade"));
		}
		GameRegistry.addShapedRecipe(new ItemStack(windMill, 1, 2), " W ",
				"WTW", " W ", 'W', new ItemStack(windMill, 1, 1), 'T',
				Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(windMill, 1, 3), " W ",
				"WTW", " W ", 'W', new ItemStack(windMill, 1, 2), 'T',
				Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(windMill, 1, 4), " W ",
				"WTW", " W ", 'W', new ItemStack(windMill, 1, 3), 'T',
				Items.getItem("transformerUpgrade"));

		for (RotorType rotor : RotorType.values()) {
			LanguageRegistry.addName(rotor.getItem(), rotor.showedName);
		}
		if (!vanillaIC2Stuff) {
			GameRegistry.addRecipe(new ItemStack(RotorType.CARBON.getItem()),
					"CCC", "CMC", "CCC", 'C', Items.getItem("carbonPlate"),
					'M', Items.getItem("machine"));
			GameRegistry.addRecipe(new ItemStack(RotorType.WOOD.getItem()),
					" S ", "SIS", " S ", 'S', new ItemStack(Item.stick), 'I',
					Items.getItem("refinedIronIngot"));
			GameRegistry.addRecipe(new ItemStack(RotorType.IRIDIUM.getItem()),
					" I ", "IRI", " I ", 'I', Items.getItem("iridiumPlate"),
					'R', new ItemStack(RotorType.CARBON.getItem()));
		}

		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		LanguageRegistry.instance().addStringLocalization(
				"itemGroup.creativeTabCW", "en_US", "Compact Windmills");

	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
	}
}