/*******************************************************************************
 * Copyright (c) 2013 Aroma1997.
 * All rights reserved. This program and other files related to this program are
 * licensed with a extended GNU General Public License v. 3
 * License informations are at:
 * https://github.com/Aroma1997/CompactWindmills/blob/master/license.txt
 ******************************************************************************/

package aroma1997.compactwindmills;

import java.io.InputStream;
import java.util.Properties;

import com.google.common.base.Throwables;


/**
 * 
 * @author Aroma1997
 * 
 */
public class Reference {
	
	static {
		Properties prop = new Properties();

        try
        {
            InputStream stream = Reference.class.getResourceAsStream("reference.properties");
            prop.load(stream);
            stream.close();
        }
        catch (Exception e)
        {
            Throwables.propagate(e);
        }

        VERSION = prop.getProperty("version");
	}
	public static final String ModID = "CompactWindmills";
	
	public static final String ModName = "Compact Windmills";
	
	public static final String VERSION;
	
	public static final int ItemIDDifference = - 256;
	
}
