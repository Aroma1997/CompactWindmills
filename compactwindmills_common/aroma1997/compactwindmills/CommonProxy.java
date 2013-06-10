/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package aroma1997.compactwindmills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

/**
 * 
 * @author Aroma1997
 * 
 */
public class CommonProxy implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer thePlayer,
			World world, int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityWindmill) {
			TileEntityWindmill tileEntityCW = (TileEntityWindmill) tileEntity;
			return new ContainerCompactWindmills(thePlayer.inventory,
					tileEntityCW, tileEntityCW.getType());
		} else {
			return null;
		}
	}

	public void registerRotorRenderer() {

	}

}
