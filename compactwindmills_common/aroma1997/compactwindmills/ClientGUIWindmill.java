/*******************************************************************************
* Copyright (c) 2013 Aroma1997.
* All rights reserved. This program and other files related to this program are
* licensed with a extended GNU General Public License v. 3
* License informations are at:
* https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
******************************************************************************/

package aroma1997.compactwindmills;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
/**
 * 
 * @author Aroma1997
 *
 */
public class ClientGUIWindmill extends GuiContainer {

	public enum GUI {
		ELV(WindType.ELV),
		LV(WindType.LV),
		MV(WindType.MV),
		HV(WindType.HV),
		EV(WindType.EV);
		
		private WindType windType;
		
		private GUI(WindType windType) {
			this.windType = windType;
		}
		
		protected Container makeContainer(IInventory playerInventory, TileEntityWindmill windmill) {
			return new ContainerCompactWindmills(playerInventory, windmill, windType);
		}
		
		public static ClientGUIWindmill makeGUI(WindType type, IInventory inventory, TileEntityWindmill tileEntityCW) {
			for(GUI gui : values()) {
				if (tileEntityCW.getType() == gui.windType) {
					return new ClientGUIWindmill(gui, inventory, tileEntityCW);
				}
			}
			return null;
		}
	}
	
	private GUI type;
	private ContainerCompactWindmills container;
	private ClientGUIWindmill(GUI type, IInventory inventory, TileEntityWindmill tileEntityCW) {
		super(type.makeContainer(inventory, tileEntityCW));
		this.type = type;
		this.allowUserInput = false;
		this.container = (ContainerCompactWindmills) type.makeContainer(inventory, tileEntityCW);
	}


	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString(type.windType.showedName, 8, 6, 0x404040);
		fontRenderer.drawString("Rotor:", 44, 30, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
        fontRenderer.drawString("Current Output: " + container.tileEntity.getOutputUntilNexttTick() + "EU/t", 8, 50, 0x404040);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture("/mods/" + Reference.ModID + "/textures/gui/GUIWindmill.png");
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
}
	

}
