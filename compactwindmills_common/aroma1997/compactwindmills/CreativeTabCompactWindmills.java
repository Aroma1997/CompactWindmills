/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with the GNU Lesser General Public License v. 3
* License informations are at:
* http://www.gnu.org/licenses/lgpl.html
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
