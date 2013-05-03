/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with the GNU Lesser General Public License v. 3
* License informations are at:
* http://www.gnu.org/licenses/lgpl.html
******************************************************************************/
package aroma1997.compactwindmills;

import com.google.common.base.Throwables;
/**
 * 
 * @author Aroma1997
 *
 */
public enum WindType {
	ELV(8, "Ultra Low Voltage Windmill", TileEntityWindmill.class, 3),
	LV(32, "Low Voltage Windmill", TileEntityWindmillLV.class, 6),
	MV(128, "Medium Voltage Windmill", TileEntityWindmillMV.class, 8),
	HV(512, "High Voltage Windmill", TileEntityWindmillHV.class, 10),
	EV(2048, "Extreme Voltage Windmill", TileEntityWindmillEV.class, 12);

	public int output;
	public Class<? extends TileEntityWindmill> claSS;
	public String showedName;
	public int checkRadius;

	private WindType(int output, String showedName, Class<? extends TileEntityWindmill> claSS, int checkRadius) {
		this.output = output;
		this.showedName = showedName;
		this.claSS = claSS;
		this.checkRadius = checkRadius;
	}

	public static TileEntityWindmill makeTileEntity(int metadata) {
		int windtype = metadata;
		try {
			TileEntityWindmill tileEntity = values()[windtype].claSS.newInstance();
			return tileEntity;
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	public String tileEntityName() {
		return "WindType." + name();
	}

}
