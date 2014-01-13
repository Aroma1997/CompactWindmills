/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package aroma1997.compactwindmills;


import aroma1997.core.inventories.AromaSlot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Aroma1997
 * 
 */
public class SlotWindmill extends AromaSlot {
	
	private WindType type;
	
	public SlotWindmill(IInventory par1iInventory, int par2, int par3, int par4, WindType type) {
		super(par1iInventory, par2, par3, par4);
		this.type = type;
	}
	
	@Override
	public int getSlotStackLimit() {
		return 1;
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		if (itemStack == null) {
			return false;
		}
		if (itemStack.getItem() instanceof IItemRotor) {
			IItemRotor item = (IItemRotor) itemStack.getItem();
			return item.doesRotorFitInWindmill(type);
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isModifyable() {
		return !CompactWindmills.vanillaIC2Stuff;
	}
	
}
