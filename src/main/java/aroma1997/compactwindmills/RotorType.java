/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package aroma1997.compactwindmills;


import net.minecraft.util.StatCollector;

import org.apache.logging.log4j.Level;

import aroma1997.core.log.LogHelper;

/**
 * 
 * @author Aroma1997
 * 
 */
public enum RotorType {
	
	WOOD(144000, WindType.ELV, WindType.LV, 0.5F, "Wooden Rotor", "rotorWood", ItemRotor.class),
	WOOL(36000, WindType.ELV, WindType.LV, 0.9F, "Cloth Rotor", "rotorCloth", ItemRotor.class),
	CARBON(1728000, WindType.LV, WindType.HV, 0.75F, "Carbon Rotor", "rotorCarbon", ItemRotor.class),
	ALLOY(432000, WindType.MV, WindType.EV, 0.9F, "Alloy Rotor", "rotorAlloy", ItemRotor.class),
	IRIDIUM(0, WindType.HV, WindType.EV, 1.0F, "Iridium Rotor", "rotorIridium", ItemRotor.class);
	
	public static void initRotors() {
		for (RotorType type : RotorType.values()) {
			type.initRotor();
		}
	}
	
	int maxDamage;
	
	WindType typeMin;
	
	WindType typeMax;
	
	float efficiency;
	
	private String unlocalizedName;
	
	private Class<? extends ItemRotor> claSS;
	
	private ItemRotor rotor;
	
	private RotorType(int maxDamage, WindType typeMin,
		WindType typeMax, float efficiency, String showedName,
		String unlocalizedName, Class<? extends ItemRotor> claSS) {
		this.maxDamage = maxDamage;
		this.typeMin = typeMin;
		this.typeMax = typeMax;
		this.efficiency = efficiency;
		this.unlocalizedName = unlocalizedName;
		this.claSS = claSS;
	}
	
	public ItemRotor getItem() {
		return rotor;
	}
	
	private void initRotor() {
		try {
			rotor = claSS.getConstructor(RotorType.class).newInstance(this);
		}
		catch (Exception e) {
			e.printStackTrace();
			LogHelper.log(Level.FATAL, "Failed to Register Rotor: " + name());
		}
	}
	
	public String getUnlocalizedName() {
		return "compactwindmills:rotor." + name();
	}
	
	public String getTextureName() {
		return "compactwindmills:item." + unlocalizedName;
	}
	
	public String getRenderTexture() {
		return "compactwindmills:textures/renderers/item."
				+ this.unlocalizedName + ".png";
	}
	
}
