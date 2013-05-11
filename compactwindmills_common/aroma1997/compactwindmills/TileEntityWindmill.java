/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with a extended GNU General Public License v. 3
* License informations are at:
* https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
/**
 * 
 * @author Aroma1997
 *
 */
public class TileEntityWindmill extends TileEntity implements IEnergySource, INetworkDataProvider, IWrenchable, INetworkUpdateListener, IInventory {
	private Random random = new Random();
	private WindType type;
	private boolean initialized;
	private int tick;
	private boolean compatibilityMode;
	private int output;
	private ItemStack[] inventoryContent;

	public TileEntityWindmill(WindType type) {
		super();
		this.type = type;
		this.tick = random.nextInt(CompactWindmills.updateTick);
		this.inventoryContent = new ItemStack[1];
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
			output = setOutput(worldObj, xCoord, yCoord, zCoord);
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
	
	public ItemStack[] getContents() {
		return inventoryContent;
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
	
	private static int getNonAirBlocks(World world, int x, int y, int z, WindType type) {
		
		int nonAirBlocks = 0;
		
		int radius = type.checkRadius;
		//This is the radius of the Windmill, where it detects, if there is air or not
		
		
		for(int xTest = - radius; xTest <= radius; xTest++) {
			for(int yTest = - radius; yTest <= radius; yTest++) {
				for(int zTest = - radius; zTest <= radius; zTest++) {
					//This will check if the block is air
					if (!world.isAirBlock(x + xTest, y + yTest, z + zTest)) {
						nonAirBlocks++;
					}
				}
			}
		}
		return nonAirBlocks - type.checkRadius - 1;
	}
	
	private int setOutput(World world, int x, int y, int z) {
		
		int nonAirBlocks = getNonAirBlocks(world, x, y, z, type);
		float weather = getWeather(world);
		float totalEfficiency = ((y - 64 - (nonAirBlocks / type.checkRadius)) / 37.5F * weather);
		float energy = (float) (type.output * totalEfficiency);
		if (!CompactWindmills.vanillaIC2Stuff) {
			energy *= (tickRotor());
		}
		else {
			energy *= (0.5F + (this.random.nextFloat() / 2));
		}
		if ((int) energy > type.output) {
			return type.output;
		}
		if ((int) energy <= 0) return 0;
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
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(CompactWindmills.windMill, 1, type.ordinal());
	}

	private static float getWeather(World world) {
		if (world.isThundering()) {
			return 1.5F;
		}
		if (world.isRaining()) {
			return 1.2F;
		}
		return 1.0F;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventoryContent[slot];
	}

	@Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (inventoryContent[par1] != null)
        {
            if (inventoryContent[par1].stackSize <= par2)
            {
                ItemStack itemstack = inventoryContent[par1];
                inventoryContent[par1] = null;
                onInventoryChanged();
                return itemstack;
            }
            else
            {
                ItemStack itemstack1 = inventoryContent[par1].splitStack(par2);

                if (inventoryContent[par1].stackSize == 0)
                {
                    inventoryContent[par1] = null;
                }

                onInventoryChanged();
                return itemstack1;
            }
        }
        else
        {
            return null;
        }
    }

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		inventoryContent[slot] = itemStack;
		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
			itemStack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return this.type.showedName;
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		if (this.inventoryContent[var1] != null)
		{
			ItemStack var2 = this.inventoryContent[var1];
			this.inventoryContent[var1] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {
		return;
	}

	@Override
	public void closeChest() {
		return;
	}

	@Override
	public boolean isStackValidForSlot(int slot, ItemStack itemStack) {
		if (itemStack == null) return false;
		if (itemStack.getItem() instanceof ItemRotor) {
			ItemRotor rotor = (ItemRotor) itemStack.getItem();
			return rotor.doesRotorFitInWindmill(type);
		}
		return false;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nBTTagCompound) {
		super.writeToNBT(nBTTagCompound);
		NBTTagList nBTTagList = new NBTTagList();
		for (int i = 0; i < inventoryContent.length; i++) {
			if (inventoryContent[i] != null) {
				NBTTagCompound nBTTagCompound1 = new NBTTagCompound();
				nBTTagCompound1.setByte("Slot", (byte) i);
				inventoryContent[i].writeToNBT(nBTTagCompound1);
				nBTTagList.appendTag(nBTTagCompound1);
			}
		}
		nBTTagCompound.setTag("Items", nBTTagList);
	}

	@Override
	public void readFromNBT(NBTTagCompound nBTTagCompound) {
		super.readFromNBT(nBTTagCompound);
		NBTTagList nBTTagList = nBTTagCompound.getTagList("Items");
		inventoryContent = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nBTTagList.tagCount(); i++) {
			NBTTagCompound nBTTagCompound1 = (NBTTagCompound) nBTTagList.tagAt(i);
			int j = nBTTagCompound1.getByte("Slot") & 0xff;
			if (j >= 0 && j < inventoryContent.length) {
				inventoryContent[j] = ItemStack.loadItemStackFromNBT(nBTTagCompound1);
			}
		}
	}
	
	private float tickRotor() {
		ItemStack itemStack = inventoryContent[0];
		if (itemStack != null && itemStack.getItem() instanceof ItemRotor) {
			ItemRotor rotor = (ItemRotor) itemStack.getItem();
			if (rotor.takeDamage) {
				if(itemStack.getItemDamage() + CompactWindmills.updateTick > itemStack.getMaxDamage()) {
					itemStack = null;
				}
				else
				{
					itemStack.setItemDamage(itemStack.getItemDamage() + (CompactWindmills.updateTick));
				}
				inventoryContent[0] = itemStack;
				onInventoryChanged();
			}
			return rotor.getEfficiency();
		}
		return 0.0F;
	}
	
	public int getOutputUntilNexttTick() {
		return this.output;
	}
}
