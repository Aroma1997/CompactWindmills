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
	
}
