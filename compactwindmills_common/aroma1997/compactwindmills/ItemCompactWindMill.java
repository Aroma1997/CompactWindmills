/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with a extended GNU General Public License v. 3
* License informations are at:
* https://www.github.com/Aroma1997/CompactWindmills/license.txt
******************************************************************************/

package aroma1997.compactwindmills;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
/**
 * 
 * @author Aroma1997
 *
 */
public class ItemCompactWindMill extends ItemBlock {
	public ItemCompactWindMill(int id) {
		super(id);
		setMaxDamage(0);
        setHasSubtypes(true);
	}

	public int getMetadata(int i) {
		if (i  < WindType.values().length) {
			return i;
		} else {
			return 0;
		}
	}
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return WindType.values()[itemstack.getItemDamage()].name();
	}
	
	@Override
    public String getItemDisplayName(ItemStack par1ItemStack)
    {
        return WindType.values()[par1ItemStack.getItemDamage()].showedName;
    }
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add("Max. Output: " + WindType.values()[par1ItemStack.getItemDamage()].output + "EU/t");
	}

}
