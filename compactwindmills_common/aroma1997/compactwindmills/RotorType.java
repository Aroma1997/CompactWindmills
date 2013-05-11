/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with a extended GNU General Public License v. 3
* License informations are at:
* https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
******************************************************************************/
package aroma1997.compactwindmills;

import java.util.logging.Level;

import aroma1997.compactwindmills.helpers.LogHelper;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
/**
 * 
 * @author Aroma1997
 *
 */
public enum RotorType {

	WOOD(27900, 36000, WindType.ELV, WindType.LV, 0.5F, "Wooden Rotor", "rotorWood", ItemRotor.class),
	CARBON(27901, 3456000, WindType.MV, WindType.EV, 0.8F, "Carbon Rotor", "rotorCarbon", ItemRotor.class),
	IRIDIUM(27902, 0, WindType.HV, WindType.EV, 1.0F, "Iridium Rotor", "rotorIridium", ItemRotor.class);
	
	private int defaultId;
	private int maxDamage;
	private WindType typeMin;
	private WindType typeMax;
	private float efficiency;
	public String showedName;
	private String unlocalizedName;
	private Class<? extends ItemRotor> claSS;

	private int id;
	private ItemRotor rotor;
	
	private RotorType(int defaultId, int maxDamage, WindType typeMin, WindType typeMax, float efficiency, String showedName, String unlocalizedName, Class<? extends ItemRotor> claSS) {
		this.defaultId = defaultId;
		this.maxDamage = maxDamage;
		this.typeMin = typeMin;
		this.typeMax = typeMax;
		this.efficiency = efficiency;
		this.showedName = showedName;
		this.unlocalizedName = unlocalizedName;
		this.claSS = claSS;
	}
	
	public Item getItem() {
		return (Item) this.rotor;
	}
	
	public ItemRotor getItemRotor() {
		return this.rotor;
	}
	
	private void getConfig(Configuration config) {
		Property rotorId = config.getItem(this.unlocalizedName, this.defaultId);
		rotorId.comment = "This is the id, of the " + this.showedName + " Item.";
		this.id = rotorId.getInt(this.defaultId);
	}
	
	private void initRotor() {
		try {
			this.rotor = (ItemRotor) claSS.getConstructor(int.class).newInstance(this.id).setMinMaxTier(this.typeMin, this.typeMax).setEfficiency(this.efficiency).setMaxDamage(this.maxDamage).setUnlocalizedName(this.unlocalizedName);
			if (this.maxDamage == 0) this.rotor.setNotGetDamage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void initRotors() {
		for(RotorType type : RotorType.values()) {
			type.initRotor();
		}
	}
	
	public static void getConfigs(Configuration config) {
		for(RotorType type : RotorType.values()) {
			type.getConfig(config);
		}
	}

}
