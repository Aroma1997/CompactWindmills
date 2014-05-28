/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package aroma1997.compactwindmills;


import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Aroma1997
 * 
 */
public class ItemRotor extends Item implements IItemRotor {
	
	private int tierMin;
	
	private int tierMax;
	
	private float efficiency;
	
	private boolean isInfinite;
	
	private RotorType type;
	
	public ItemRotor(RotorType type) {
		super();
		setMinMaxTier(type.typeMin, type.typeMax);
		setEfficiency(type.efficiency);
		setMaxDamage(type.maxDamage);
		setUnlocalizedName(type.getUnlocalizedName());
		setTextureName(type.getTextureName());
		setCreativeTab(CompactWindmills.creativeTabCompactWindmills);
		
		setMaxStackSize(1);
		setNoRepair();
		this.type = type;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
		List par3List, boolean par4) {
		if (! isInfinite) {
			int leftOverTicks = par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage();
			par3List.add(StatCollector.translateToLocalFormatted("info.compactwindmills:tooltip.ticksleft", "" + leftOverTicks));
			
			
			int hours = leftOverTicks / 72000;
			int minutes = leftOverTicks % 72000 / 1200;
			int seconds = leftOverTicks % 1200 / 20;
			par3List.add("(" + StatCollector.translateToLocalFormatted("info.compactwindmills:tooltip.timeleft", "" + hours, "" + minutes, "" + seconds) + ")");
		}
		else {
			par3List.add(StatCollector.translateToLocal("info.compactwindmills:tooltip.infinite"));
		}
		par3List.add(StatCollector.translateToLocalFormatted("info.compactwindmills:tooltip.efficiency", (int) (((IItemRotor) par1ItemStack.getItem()).getEfficiency() * 100) + " %"));
	}
	
	@Override
	public boolean doesRotorFitInWindmill(WindType type) {
		return type.ordinal() <= tierMax && type.ordinal() >= tierMin;
	}
	
	@Override
	public float getEfficiency() {
		return efficiency;
	}
	
	@Override
	public int getMaxTier() {
		return tierMax;
	}
	
	@Override
	public int getMinTier() {
		return tierMin;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		
		super.registerIcons(iconRegister);
	}
	
	public ItemRotor setEfficiency(float efficiency) {
		this.efficiency = efficiency;
		return this;
	}
	
	public ItemRotor setMinMaxTier(WindType type) {
		tierMin = type.ordinal();
		tierMax = type.ordinal();
		return this;
	}
	
	public ItemRotor setMinMaxTier(WindType typeMin, WindType typeMax) {
		tierMin = typeMin.ordinal();
		tierMax = typeMax.ordinal();
		return this;
	}
	
	@Override
	public boolean isInfinite() {
		return isInfinite;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public ResourceLocation getRenderTexture() {
		return new ResourceLocation(type.getRenderTexture());
	}
	
	@Override
	public ItemRotor setMaxDamage(int maxDamage) {
		super.setMaxDamage(maxDamage);
		if (maxDamage == 0) {
			isInfinite = true;
		}
		return this;
	}
	
	@Override
	public void tickRotor(ItemStack rotor, TileEntityWindmill tileEntity, World worldObj) {
		return;
	}
	
}
