/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with the GNU Lesser General Public License v. 3
* License informations are at:
* http://www.gnu.org/licenses/lgpl.html
******************************************************************************/

package aroma1997.compactwindmills;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
/**
 * 
 * @author Aroma1997
 *
 */
public class BlockCompactWindmill extends BlockContainer {
	
	@SideOnly(Side.CLIENT)
	private Icon[][] textures;
	
	public BlockCompactWindmill(int id) {
		super(id, Material.iron);
		setUnlocalizedName("compactWindmill");
		setHardness(2.0F);
		setCreativeTab(CompactWindmills.creativeTabCompactWindmills);
		
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return WindType.makeTileEntity(metadata);
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List itemList) {
		for (WindType type : WindType.values()) {
			itemList.add(new ItemStack(this,1,type.ordinal()));
		}
	}
	

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		textures = new Icon[WindType.values().length][5];
		for(WindType type: WindType.values()) {
			for(int side = 0; side < 4; side++) {
				String sideName = side == 0 ? "bottom" : side == 1 ? "top" : side == 2 ? "front" : "side";
				textures[type.ordinal()][side] = par1IconRegister.registerIcon(Reference.ModID + ":" + type.name() + "_" + sideName);
			}
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {
		return textures[metadata][side == 3 ? 2 : side > 3 ? 3 : side];
	}
}
