/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with the GNU Lesser General Public License v. 3
* License informations are at:
* http://www.gnu.org/licenses/lgpl.html
******************************************************************************/

package aroma1997.compactwindmills;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.network.NetworkHelper;
import ic2.api.tile.IWrenchable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
/**
 * 
 * @author Aroma1997
 *
 */
public class TileEntityWindmill extends TileEntity implements IEnergySource, INetworkDataProvider, IWrenchable, INetworkUpdateListener{
	private static Random random = new Random();
	private WindType type;
	private boolean initialized;
	private int tick;
	private boolean compatibilityMode;
	private int output;

	public TileEntityWindmill() {
		this(WindType.ELV);
	}

	public TileEntityWindmill(WindType type) {
		super();
		this.type = type;
		this.tick = random.nextInt(CompactWindmills.updateTick);
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
		return true;
	}
	
	@Override
	public void updateEntity() {
		if (compatibilityMode) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, type.ordinal(), 0);
			compatibilityMode=false;
		}
		if (!initialized && worldObj != null) {
			if (worldObj.isRemote) {
				NetworkHelper.requestInitialData(this);
			} else {
				EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
				MinecraftForge.EVENT_BUS.post(loadEvent);
			}
			initialized = true;
		}
		if (tick-- == 0) {
			output = setOutput(worldObj, xCoord, yCoord, zCoord, type);
			tick = CompactWindmills.updateTick;
		}
		if (output > 0) {
			EnergyTileSourceEvent sourceEvent = new EnergyTileSourceEvent(this, output);
			MinecraftForge.EVENT_BUS.post(sourceEvent);
		}
	}

	@Override
	public boolean isAddedToEnergyNet() {
		return initialized;
	}

	@Override
	public void onNetworkUpdate(String field) {

	}

	private static List<String> fields=Arrays.asList(new String[0]);
	@Override
	public List<String> getNetworkedFields() {
		return fields;
	}

	@Override
	public int getMaxEnergyOutput() {
		return type.output;
	}

	public WindType getType() {
		return type;
	}

	@Override
	public void invalidate() {
		EnergyTileUnloadEvent unloadEvent = new EnergyTileUnloadEvent(this);
		MinecraftForge.EVENT_BUS.post(unloadEvent);
	}
	
	private static float getSpace(World world, int x, int y, int z, WindType type) {
		
		int airBlocks = 0;
		
		int radius = type.checkRadius;
		//This is the radius of the Windmill, where it detects, if there is air or not
		
		
		for(int xTest = - radius; xTest <= radius; xTest++) {
			for(int yTest = - radius; yTest <= radius; yTest++) {
				for(int zTest = - radius; zTest <= radius; zTest++) {
					//This will check if the block is air
					if (world.isAirBlock(x + xTest, y + yTest, z + zTest)) {
						airBlocks++;
					}
				}
			}
		}
		float efficiency = (float)airBlocks / (float)(Math.pow(radius * 2 + 1, 3) - 1 - radius);
		return efficiency;
	}
	
	private static int setOutput(World world, int x, int y, int z, WindType type) {
		
		float space = getSpace(world, x, y, z, type) * 0.6F;
		float height = getHeight(world, x, y, z) * 0.4F;
		float weather = getWeather(world) * 0.2F;
		float totalEfficiency = space + height + weather;
		float energy = (float)type.output * totalEfficiency + 0.5F;
		if ((int) energy > type.output) {
			return type.output;
		}
		return (int)energy;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return false;
	}

	@Override
	public short getFacing() {
		return 0;
	}

	@Override
	public void setFacing(short facing) {
		
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public float getWrenchDropRate() {
		return 0.99F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(this.blockType, 1, type.ordinal());
	}

	private static float getHeight(World world, int x, int y, int z) {
		float heightEfficiency = (y - 63) / (63);
		if (heightEfficiency > 1.0F) {
			return 1.0F;
		}
		return heightEfficiency;
	}

	private static float getWeather(World world) {
		if (world.isThundering()) {
			return 1.0F;
		}
		if (world.isRaining()) {
			return 0.5F;
		}
		return 0.0F;
	}
}
