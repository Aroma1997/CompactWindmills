
package aroma1997.compactwindmills;


import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface IItemRotor {
	
	/**
	 * If the rotor fits into the Windmill
	 * 
	 * @param windmill the Windmill
	 * @return
	 */
	public boolean doesRotorFitInWindmill(WindType windmill);
	
	/**
	 * The efficiency of the rotor, when the Windmill calculates its output
	 * 
	 * @return
	 */
	public float getEfficiency();
	
	/**
	 * If the rotor can fit into the Windmill
	 * 
	 * @return higher limit
	 */
	public int getMaxTier();
	
	/**
	 * If the rotor can fit into the Windmill
	 * 
	 * @return lower limit
	 */
	public int getMinTier();
	
	/**
	 * If the rotor can break or if it's infinite
	 */
	public boolean isInfinite();
	
	/**
	 * This is executed, when the windmill gets the efficiency of the Rotor.
	 * 
	 * @param rotor the rotor itself
	 * @param tileEntity the Windmill where the rotor is in
	 * @param worldObj the world, where the Windmill is in
	 */
	public void tickRotor(ItemStack rotor, TileEntityWindmill tileEntity, World worldObj);
	
	/**
	 * To get the texture that is used to display the Rotor Model Texture.
	 * 
	 * @return The path to the Texture
	 */
	public ResourceLocation getRenderTexture();
}
