
package aroma1997.compactwindmills.helpers;


import java.util.logging.Level;

import aroma1997.compactwindmills.CompactWindmills;
import aroma1997.core.log.AromaLog;

public class LogHelper {
	
	private static AromaLog WindmillLog = aroma1997.core.log.LogHelper.genNewLogger("CompactWindmills");
	
	public static void debugLog(Level level, String message) {
		if (! CompactWindmills.debugMode) {
			return;
		}
		log(level, message);
	}
	
	public static void log(Level level, String message) {
		WindmillLog.log(level, message);
	}
	
	public static AromaLog getLogger() {
		return WindmillLog;
	}
	
}
