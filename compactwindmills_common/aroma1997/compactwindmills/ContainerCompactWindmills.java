/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/
package aroma1997.compactwindmills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author Aroma1997
 * 
 */
public class ContainerCompactWindmills extends Container {

	public TileEntityWindmill tileEntity;

	public ContainerCompactWindmills(IInventory inventory,
			TileEntityWindmill tileEntityCW, WindType windType) {
		tileEntity = tileEntityCW;
		layoutContainer(inventory, tileEntityCW, windType);
	}

	private void layoutContainer(IInventory playerInventory,
			IInventory inventory, WindType type) {
		addSlotToContainer(new SlotWindmill(inventory, 0, 80, 26, type));
		for (int inventoryRow = 0; inventoryRow < 3; inventoryRow++) {
			for (int inventoryColumn = 0; inventoryColumn < 9; inventoryColumn++) {
				addSlotToContainer(new Slot(playerInventory, inventoryColumn
						+ inventoryRow * 9 + 9, 8 + inventoryColumn * 18,
						84 + inventoryRow * 18));
			}
		}

		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			addSlotToContainer(new Slot(playerInventory, hotbarSlot,
					8 + hotbarSlot * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(par2);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 == 0) {
				if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (par2 != 0) {
				if (((SlotWindmill) inventorySlots.get(0))
						.isItemValid(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
						return null;
					}
				} else if (par2 >= 1 && par2 < 28) {
					if (!this.mergeItemStack(itemstack1, 28, 37, false)) {
						return null;
					}
				} else if (par2 >= 28 && par2 < 37
						&& !this.mergeItemStack(itemstack1, 1, 27, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 1, 28, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		return itemstack;
	}

}
