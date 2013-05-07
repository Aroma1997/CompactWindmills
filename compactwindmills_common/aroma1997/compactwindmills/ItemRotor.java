/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with a extended GNU General Public License v. 3
* License informations are at:
* https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
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
	
	private int tierMin;
	private int tierMax;
	private float efficiency;
	public boolean takeDamage = true;
	
	public ItemRotor(int id) {
		super (id + Reference.ItemIDDifference);
		setCreativeTab(CompactWindmills.creativeTabCompactWindmills);
		setMaxStackSize(1);
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (this.takeDamage) {
			int leftOverTicks = par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage();
			par3List.add(leftOverTicks + " ticks left over on this Rotor.");
		}
		else
		{
			par3List.add("Infinite");
		}
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {

        itemIcon = iconRegister.registerIcon(Reference.ModID + ":" + this.getUnlocalizedName());
    }
    
    public int getMinTier() {
    	return this.tierMin;
    }
    
    public int getMaxTier() {
    	return this.tierMax;
    }
    
    public ItemRotor setMinMaxTier(WindType typeMin, WindType typeMax) {
    	this.tierMin = typeMin.ordinal();
    	this.tierMax = typeMax.ordinal();
    	return this;
    }
    
    public ItemRotor setMinMaxTier(WindType type) {
    	this.tierMin = type.ordinal();
    	this.tierMax = type.ordinal();
    	return this;
    }
    
    public boolean doesRotorFitInWindmill(WindType type) {
    	if(type.ordinal() <= this.tierMax && type.ordinal() >= this.tierMin) return true;
    	return false;
    }
    
    public ItemRotor setEfficiency(float efficiency) {
    	this.efficiency = efficiency;
    	return this;
    }
    
    public float getEfficiency() {
    	return this.efficiency;
    }
    
    public ItemRotor setNotGetDamage() {
    	this.takeDamage = false;
    	return this;
    }
    
    public ItemRotor setGetDamage(boolean damage) {
    	this.takeDamage = damage;
    	return this;
    }
}
