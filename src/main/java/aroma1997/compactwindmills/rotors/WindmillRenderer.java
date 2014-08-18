/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package aroma1997.compactwindmills.rotors;


import aroma1997.compactwindmills.CompactWindmills;
import aroma1997.compactwindmills.TileEntityWindmill;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Aroma1997
 * 
 */
@SideOnly(Side.CLIENT)
public class WindmillRenderer extends TileEntitySpecialRenderer {
	
	private ModelRotor model;
	
	public void renderBlockRotor(TileEntityWindmill tileEntity, World world, int posX, int posY,
		int posZ, Block block) {
		if (tileEntity.getRotor() == null) {
			return;
		}
		
		//Reason new Models get createt every Tick and that useing up Infinite Amount of Resources which will end up in Crash (Out of ram)
		if(model == null || model.getCurrentSize() != tileEntity.getType().checkRadius)
		{
			model = new ModelRotor(tileEntity.getType().checkRadius);
		}
		Tessellator tessellator = Tessellator.instance;
		float brightness = world.getBlockLightValue(posX, posY, posZ);
		int skyBrightness = world.getLightBrightnessForSkyBlocks(posX, posY + 1,
			posZ, 0);
		int skyBrightness1 = skyBrightness % 65536;
		int skyBrightness2 = skyBrightness / 65536;
		tessellator.setColorOpaque_F(brightness, brightness, brightness);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, skyBrightness1,
			skyBrightness2);
		
		short facing = tileEntity.getFacing();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		if (facing == 2 || facing == 3 || facing == 4 || facing == 5) {
			
			int dir = facing == 4 ? 0 : facing == 2 ? 1 : facing == 5 ? 2
				: facing;
			GL11.glRotatef(dir * - 90F, 0F, 1F, 0F);
		}
		else if (facing == 1) {
			GL11.glRotatef(- 90.0F, 0.0F, 0.0F, 1.0F);
		}
		GL11.glRotatef(360 - System.currentTimeMillis() / 30 % 360, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(- 0.25F, 0.0F, 0.0F);
		
		bindTexture(tileEntity.getRotor().getRenderTexture());
		
		model.render(null, 0.0F, 0.0F, - 0.1F, 0.0F, 0.0F, 0.0625F);
		
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double posX,
		double posY, double posZ, float f) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) posX, (float) posY, (float) posZ);
		TileEntityWindmill tileEntityWindmill = (TileEntityWindmill) tileEntity;
		renderBlockRotor(tileEntityWindmill, tileEntity.getWorldObj(), tileEntity.xCoord,
			tileEntity.yCoord, tileEntity.zCoord, CompactWindmills.windMill);
		GL11.glPopMatrix();
	}
	
}
