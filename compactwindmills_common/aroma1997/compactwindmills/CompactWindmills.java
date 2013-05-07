/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with a extended GNU General Public License v. 3
* License informations are at:
* https://www.github.com/Aroma1997/CompactWindmills/license.txt
******************************************************************************/

package aroma1997.compactwindmills;

import ic2.api.item.Items;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
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

@Mod(modid=Reference.ModID, name=Reference.ModName, version=Reference.Version)
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
/**
 * 
 * @author Aroma1997
 *
 */
public class CompactWindmills {
        @Instance(Reference.ModID)
        public static CompactWindmills instance;
       
        @SidedProxy(clientSide="aroma1997.compactwindmills.ClientProxy", serverSide="aroma1997.compactwindmills.CommonProxy")
        public static CommonProxy proxy;
       
        private static int blockID;
        public static Block windMill;
        public static Item rotor;
        public static Item rotorWood;
        public static Item rotorIridium;
        public static int updateTick;
        public static CreativeTabs creativeTabCompactWindmills = new CreativeTabCompactWindmills("creativeTabCW");
        @PreInit
        public void preInit(FMLPreInitializationEvent event) {
        	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        	config.load();
        	Property block = config.getBlock("CompactWindmill", 2790);
        		block.comment = "This is the id of the Compact Windmill Blocks.";
        		blockID = block.getInt(2790);
        	Property ticks = config.get(Configuration.CATEGORY_GENERAL, "WaitTicks", 64);
        		ticks.comment = "This is the amount of ticks, the windmills will wait, until they update their efficiency count." +
        					"Note: The lower the number is, the more lag it will cause." +
        					"Also note, that they always produce their EU-Count per tick, not only, when they update their efficiency count.";
        		updateTick = ticks.getInt(64);
        	RotorType.getConfigs(config);
        	config.save();
        	
        	RotorType.initRotors();
        	
            windMill = new BlockCompactWindmill(blockID);
        }
       
        @Init
        public void load(FMLInitializationEvent event) {
        	
        	GameRegistry.registerBlock(windMill, ItemCompactWindMill.class, "blockCompactWindmill");
        	for (WindType typ : WindType.values()) {
        		LanguageRegistry.instance().addStringLocalization(typ.name() + ".name", typ.showedName);
        		GameRegistry.registerTileEntity(typ.claSS, typ.tileEntityName());
        	}
    		GameRegistry.addShapedRecipe(new ItemStack(windMill, 1, 0), " W ", "WTW", " W ", 'W', Items.getItem("windMill"), 'T', Items.getItem("lvTransformer"));
    		GameRegistry.addShapedRecipe(new ItemStack(windMill, 1, 1), " W ", "WTW", " W ", 'W', new ItemStack(windMill, 1, 0), 'T', Items.getItem("transformerUpgrade"));
    		GameRegistry.addShapedRecipe(new ItemStack(windMill, 1, 2), " W ", "WTW", " W ", 'W', new ItemStack(windMill, 1, 1), 'T', Items.getItem("transformerUpgrade"));
    		GameRegistry.addShapedRecipe(new ItemStack(windMill, 1, 3), " W ", "WTW", " W ", 'W', new ItemStack(windMill, 1, 2), 'T', Items.getItem("transformerUpgrade"));
    		GameRegistry.addShapedRecipe(new ItemStack(windMill, 1, 4), " W ", "WTW", " W ", 'W', new ItemStack(windMill, 1, 3), 'T', Items.getItem("transformerUpgrade"));
    		
    		for(RotorType rotor : RotorType.values()) {
    			LanguageRegistry.addName(rotor.getItem(), rotor.showedName);
    		}
    		
        	GameRegistry.addRecipe(new ItemStack(rotor), "CCC", "CMC", "CCC", 'C', Items.getItem("carbonPlate"), 'M', Items.getItem("machine"));
        	GameRegistry.addRecipe(new ItemStack(rotorWood), " S ", "SIS", " S ", 'S', new ItemStack(Item.stick), 'I', Items.getItem("refinedIronIngot"));
        	GameRegistry.addRecipe(new ItemStack(rotorIridium), " I ", "IRI", " I ", 'I', Items.getItem("iridiumPlate"), 'R', new ItemStack(rotor));
        	
        	NetworkRegistry.instance().registerGuiHandler(this, proxy);
        	LanguageRegistry.instance().addStringLocalization("itemGroup.creativeTabCW", "en_US", "CompactWindmills");
        	
        }
       
        @PostInit
        public void postInit(FMLPostInitializationEvent event) {
        }
}