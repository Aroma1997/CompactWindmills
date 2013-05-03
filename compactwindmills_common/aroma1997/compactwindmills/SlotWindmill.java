/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with the GNU Lesser General Public License v. 3
* License informations are at:
* http://www.gnu.org/licenses/lgpl.html
******************************************************************************/
package aroma1997.compactwindmills;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
/**
 * 
 * @author Aroma1997
 *
 */
public class SlotWindmill extends Slot {

	private WindType type;
	public SlotWindmill(IInventory par1iInventory, int par2, int par3, int par4, WindType type) {
		super(par1iInventory, par2, par3, par4);
		this.type = type;
	}

	public boolean isItemValid(ItemStack itemStack)
	{
		if (itemStack == null) return false;
		if (itemStack.getItem() instanceof ItemRotor) {
			ItemRotor item = (ItemRotor) itemStack.getItem();
			if (item.getTier() >= type.ordinal()) return true;
		}
		return false;
	}

}
