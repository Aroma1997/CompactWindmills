/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package aroma1997.compactwindmills;


import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.network.NetworkHelper;
import ic2.api.tile.IWrenchable;

import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;

import aroma1997.compactwindmills.helpers.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

/**
 * 
 * @author Aroma1997
 * 
 */
public class TileEntityWindmill extends TileEntity implements IEnergySource, INetworkDataProvider, IWrenchable, INetworkUpdateListener, IInventory {
	
	public TileEntityWindmill() {
		this(WindType.ELV);
	}
	
	public TileEntityWindmill(WindType type) {
		super();
		this.type = type;
		tick = random.nextInt(CompactWindmills.updateTick);
		inventoryContent = new ItemStack[1];
	}
	
	private static int getNonAirBlocks(World world, int x, int y, int z, WindType type) {
		
		int nonAirBlocks = 0;
		
		int radius = type.checkRadius;
		// This is the radius of the Windmill, where it detects, if there is air
		// or not
		
		for (int xTest = - radius; xTest <= radius; xTest++) {
			for (int yTest = - radius; yTest <= radius; yTest++) {
				for (int zTest = - radius; zTest <= radius; zTest++) {
					// This will check if the block is air
					if (! world.isAirBlock(x + xTest, y + yTest, z + zTest)) {
						nonAirBlocks++;
					}
				}
			}
		}
		return nonAirBlocks - type.checkRadius - 1;
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
	
	private Random random = new Random();
	
	private WindType type;
	
	private boolean initialized;
	
	private boolean compatibilityMode;
	
	private int output;
	
	private ItemStack[] inventoryContent;
	
	private short facing = 2;
	
	private short prevFacing = 2;
	
	private long lastUpdate = System.currentTimeMillis();
	
	@Override
	public void closeChest() {
		NetworkHelper.updateTileEntityField(this, "inventoryContent");
		return;
	}
	
	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (inventoryContent[par1] != null) {
			ItemStack itemstack;
			
			if (inventoryContent[par1].stackSize <= par2) {
				itemstack = inventoryContent[par1];
				inventoryContent[par1] = null;
				onInventoryChanged();
				return itemstack;
			}
			else {
				itemstack = inventoryContent[par1].splitStack(par2);
				
				if (inventoryContent[par1].stackSize == 0) {
					inventoryContent[par1] = null;
				}
				
				onInventoryChanged();
				return itemstack;
			}
		}
		else {
			return null;
		}
	}
	
	public ItemStack[] getContents() {
		return inventoryContent;
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		writeToNBT(nbtTag);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbtTag);
	}
	
	@Override
	public short getFacing() {
		return facing;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}
	
	@Override
	public String getInvName() {
		return type.showedName;
	}
	
	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public List getNetworkedFields() {
		List list = new Vector(2);
		list.add("facing");
		
		return list;
	}
	
	public int getOutputUntilNexttTick() {
		return output;
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
	public ItemStack getStackInSlotOnClosing(int var1) {
		if (inventoryContent[var1] != null) {
			ItemStack var2 = inventoryContent[var1];
			inventoryContent[var1] = null;
			return var2;
		}
		else {
			return null;
		}
	}
	
	public WindType getType() {
		return type;
	}
	
	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(CompactWindmills.windMill, 1, type.ordinal());
	}
	
	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}
	
	@Override
	public void invalidate() {
		if (!worldObj.isRemote) {
			EnergyTileUnloadEvent unloadEvent = new EnergyTileUnloadEvent(this);
			MinecraftForge.EVENT_BUS.post(unloadEvent);
		}
		super.invalidate();
	}
	
	@Override
	public boolean isInvNameLocalized() {
		return false;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		if (itemStack == null) {
			return false;
		}
		if (itemStack.getItem() instanceof IItemRotor) {
			IItemRotor rotor = (IItemRotor) itemStack.getItem();
			return rotor.doesRotorFitInWindmill(type);
		}
		return false;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}
	
	@Override
	public void onNetworkUpdate(String field) {
		if (field.equals("facing") && prevFacing != facing) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			prevFacing = facing;
		}
	}
	
	@Override
	public void openChest() {
		return;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nBTTagCompound) {
		super.readFromNBT(nBTTagCompound);
		NBTTagList nBTTagList = nBTTagCompound.getTagList("Items");
		inventoryContent = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nBTTagList.tagCount(); i++) {
			NBTTagCompound nBTTagCompoundTemp = (NBTTagCompound) nBTTagList.tagAt(i);
			int slotNumb = nBTTagCompoundTemp.getByte("Slot") & 0xff;
			if (slotNumb >= 0 && slotNumb < inventoryContent.length) {
				inventoryContent[slotNumb] = ItemStack.loadItemStackFromNBT(nBTTagCompoundTemp);
			}
		}
		
		prevFacing = facing = nBTTagCompound.getShort("facing");
	}
	
	@Override
	public void setFacing(short facing) {
		if (facing == 0) {
			return;
		}
		this.facing = facing;
		LogHelper.debugLog(Level.INFO, "Setting Windmill to facing:" + facing);
		
		if (prevFacing != facing) {
			NetworkHelper.updateTileEntityField(this, "facing");
		}
		
		prevFacing = facing;
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventoryContent[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}
	
	private boolean damageRotor = true;
	
	private int setOutput(World world, int x, int y, int z) {
		
		int nonAirBlocks = getNonAirBlocks(world, x, y, z, type);
		float weather = getWeather(world);
		float totalEfficiency = (y - 64 - nonAirBlocks / type.checkRadius) / 37.5F * weather;
		float energy = type.output * totalEfficiency;
		if (! CompactWindmills.vanillaIC2Stuff) {
			energy *= tickRotor();
		}
		else {
			energy *= 0.5F + random.nextFloat() / 2;
		}
		if ((int) energy > type.output) {
			return type.output;
		}
		if ((int) energy <= 0) {
			return 0;
		}
		return (int) energy;
	}
	
	private float tickRotor() {
		if (inventoryContent[0] != null && inventoryContent[0].getItem() instanceof IItemRotor) {
			IItemRotor rotor = (IItemRotor) inventoryContent[0].getItem();
			if (worldObj.isRemote) {
				return rotor.getEfficiency();
			}
			rotor.tickRotor(inventoryContent[0], this, worldObj);
			if (!rotor.isInfinite() && damageRotor) {
				if (inventoryContent[0].getItemDamage() + CompactWindmills.updateTick > inventoryContent[0].getMaxDamage()) {
					inventoryContent[0] = null;
				}
				else {
					int damage = inventoryContent[0].getItemDamage() + CompactWindmills.updateTick;
					inventoryContent[0].setItemDamage(damage);
				}
				onInventoryChanged();
			}

			damageRotor = false;
			return rotor.getEfficiency();
		}
		return 0.0F;
	}
	
	@Override
	public void updateEntity() {
		if (compatibilityMode) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord,
				type.ordinal(), 0);
			compatibilityMode = false;
		}
		if (! initialized && worldObj != null) {
			if (worldObj.isRemote) {
				NetworkHelper.updateTileEntityField(this, "facing");
			}
			else {
				EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
				MinecraftForge.EVENT_BUS.post(loadEvent);
			}
			initialized = true;
		}
		if (System.currentTimeMillis() >= CompactWindmills.updateTick / 20 + lastUpdate) {
			output = setOutput(worldObj, xCoord, yCoord, zCoord);
			lastUpdate += CompactWindmills.updateTick / 20;
		}
		if (worldObj.isRemote) return;
		if (output > 0) {
//			EnergyTileSourceEvent sourceEvent = new EnergyTileSourceEvent(this,
//				output);
//			MinecraftForge.EVENT_BUS.post(sourceEvent);
//			if (sourceEvent.amount == output) {
//				damageRotor = false;
//			}
		}
	}
	
	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return true;
	}
	
	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return facing != side && side != 0;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nBTTagCompound) {
		super.writeToNBT(nBTTagCompound);
		NBTTagList nBTTagList = new NBTTagList();
		for (int i = 0; i < inventoryContent.length; i++) {
			if (inventoryContent[i] != null) {
				NBTTagCompound nBTTagCompoundTemp = new NBTTagCompound();
				nBTTagCompoundTemp.setByte("Slot", (byte) i);
				inventoryContent[i].writeToNBT(nBTTagCompoundTemp);
				nBTTagList.appendTag(nBTTagCompoundTemp);
			}
		}
		
		nBTTagCompound.setTag("Items", nBTTagList);
		
		nBTTagCompound.setShort("facing", facing);
	}
	
	public IItemRotor getRotor() {
		if (inventoryContent[0] != null
			&& inventoryContent[0].getItem() instanceof IItemRotor) {
			return (IItemRotor) inventoryContent[0].getItem();
		}
		return null;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return true;
	}

	@Override
	public double getOfferedEnergy() {
		return this.getOutputUntilNexttTick();
	}

	@Override
	public void drawEnergy(double amount) {
		if (amount != 0) {
			damageRotor = true;
		}
	}
}
