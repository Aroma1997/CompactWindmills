/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with a extended GNU General Public License v. 3
* License informations are at:
* https://www.github.com/Aroma1997/CompactWindmills/license.txt
******************************************************************************/
package aroma1997.compactwindmills;

import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
/**
 * 
 * @author Aroma1997
 *
 */
public enum RotorType {
	
	CARBON(27900, 3456000, WindType.MV, WindType.EV, 0.8F, "Carbon Rotor", "rotorCarbon", true, ItemRotor.class),
	WOOD(27901, 72000, WindType.ELV, WindType.LV, 0.5F, "Wooden Rotor", "rotorWood", true, ItemRotor.class),
	IRIDIUM(27902, 0, WindType.ELV, WindType.EV, 1.0F, "Iridium Rotor", "rotorIridium", false, ItemRotor.class);
	
	private int defaultId;
	private int maxDamage;
	private WindType typeMin;
	private WindType typeMax;
	private float efficiency;
	public String showedName;
	private String unlocalizedName;
	private boolean takeDamage;
	private Class<? extends ItemRotor> claSS;

	private int id;
	private ItemRotor rotor;
	
	private RotorType(int defaultId, int maxDamage, WindType typeMin, WindType typeMax, float efficiency, String showedName, String unlocalizedName, boolean takeDamage, Class<? extends ItemRotor> claSS) {
		this.defaultId = defaultId;
		this.maxDamage = maxDamage;
		this.typeMin = typeMin;
		this.typeMax = typeMax;
		this.efficiency = efficiency;
		this.showedName = showedName;
		this.unlocalizedName = unlocalizedName;
		this.takeDamage = takeDamage;
		this.claSS = claSS;
	}
	
	public Item getItem() {
		return (Item) this.rotor;
	}
	
	public ItemRotor getItemRotor() {
		return this.rotor;
	}
	
	public void getConfig(Configuration config) {
		Property rotorId = config.getItem(this.unlocalizedName, this.defaultId);
		rotorId.comment = "This is the id, of the " + this.showedName + " Item.";
		this.id = rotorId.getInt(this.defaultId);
	}
	
	public void initRotor() {
		try {
			this.rotor = (ItemRotor) claSS.getConstructor(int.class).newInstance(this.id).setGetDamage(this.takeDamage).setMinMaxTier(this.typeMin, this.typeMax).setEfficiency(this.efficiency).setMaxDamage(this.maxDamage).setUnlocalizedName(this.unlocalizedName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
