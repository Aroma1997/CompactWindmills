/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with the GNU Lesser General Public License v. 3
* License informations are at:
* http://www.gnu.org/licenses/lgpl.html
******************************************************************************/
package aroma1997.compactwindmills;

import ic2.api.item.Items;
import net.minecraft.item.ItemStack;

import com.google.common.base.Throwables;

import cpw.mods.fml.common.registry.GameRegistry;
/**
 * 
 * @author Aroma1997
 *
 */
public enum WindType {
	ELV(8, "Extreme Low Voltage Windmill", TileEntityWindmill.class),
	LV(32, "Low Voltage Windmill", TileEntityWindmillLV.class),
	MV(128, "Medium Voltage Windmill", TileEntityWindmillMV.class),
	HV(512, "High Voltage Windmill", TileEntityWindmillHV.class),
	EV(2048, "Extreme Voltage Windmill", TileEntityWindmillEV.class);

	private int output;
	public Class<? extends TileEntityWindmill> claSS;
	public String showedName;

	private WindType(int output, String showedName, Class<? extends TileEntityWindmill> claSS) {
		this.output=output;
		this.showedName=showedName;
		this.claSS=claSS;
	}

	public static void generateRecipes(BlockCompactWindmill block) {
		GameRegistry.addShapedRecipe(new ItemStack(block, 1, 0), " W ", "WTW", " W ", 'W', Items.getItem("windMill"), 'T', Items.getItem("lvTransformer"));
		GameRegistry.addShapedRecipe(new ItemStack(block, 1, 1), " W ", "WTW", " W ", 'W', new ItemStack(block, 1, 0), 'T', Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(block, 1, 2), " W ", "WTW", " W ", 'W', new ItemStack(block, 1, 1), 'T', Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(block, 1, 3), " W ", "WTW", " W ", 'W', new ItemStack(block, 1, 2), 'T', Items.getItem("transformerUpgrade"));
		GameRegistry.addShapedRecipe(new ItemStack(block, 1, 4), " W ", "WTW", " W ", 'W', new ItemStack(block, 1, 3), 'T', Items.getItem("transformerUpgrade"));
	}
	public int getOutput() {
		return output;
	}

	public static TileEntityWindmill makeTileEntity(int metadata) {
		int windtype = metadata;
		try {
			TileEntityWindmill te = values()[windtype].claSS.newInstance();
			return te;
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	public String tileEntityName() {
		return "WindType."+name();
	}

}
