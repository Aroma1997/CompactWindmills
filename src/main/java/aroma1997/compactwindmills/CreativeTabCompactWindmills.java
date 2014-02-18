/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package aroma1997.compactwindmills;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Aroma1997
 * 
 */
public class CreativeTabCompactWindmills extends CreativeTabs {
	
	public CreativeTabCompactWindmills(String name) {
		super(name);
	}
	
	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(CompactWindmills.windMill);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public String getTranslatedTabLabel()
    {
        return "creativetab.compactwindmills:creativetab.name";
    }
	
}
