/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with the GNU Lesser General Public License v. 3
* License informations are at:
* http://www.gnu.org/licenses/lgpl.html
******************************************************************************/
package aroma1997.compactwindmills;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
/**
 * 
 * @author Aroma1997
 *
 */
public class ItemRotor extends Item {
	
	private int tier;
	
	public ItemRotor(int id) {
		super (id + Reference.ItemIDDifference);
		setCreativeTab(CompactWindmills.creativeTabCompactWindmills);
		setMaxStackSize(1);
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		int leftOverTicks = par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage();
		par3List.add(leftOverTicks + " ticks left over on this Rotor.");
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {

        itemIcon = iconRegister.registerIcon(Reference.ModID + ":" + this.getUnlocalizedName());
    }
    
    public int getTier() {
    	return this.tier;
    }
    
    public ItemRotor setMaxTier(WindType type) {
    	this.tier = type.ordinal();
    	return this;
    }

}
