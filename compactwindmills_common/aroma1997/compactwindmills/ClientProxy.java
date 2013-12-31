/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package aroma1997.compactwindmills;


import aroma1997.compactwindmills.rotors.WindmillRenderer;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Aroma1997
 * 
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRotorRenderer(Configuration config) {
		config.load();
		Property specialRenderer = config.get(Configuration.CATEGORY_GENERAL, "specialRender", true);
		specialRenderer.comment = "If rotors will be rendered in-game.";
		boolean specialRender = specialRenderer.getBoolean(true);
		config.save();
		if (! specialRender) {
			return;
		}
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmill.class, new WindmillRenderer());
	}
	
}
