package linkedwith;
/**
 * Takes care of housekeeping for LW methods
 * @author Harry Nelken - hrn10@case.edu
 * For EECS 293 - Vincenzo Liberatore
 */
public class Utilities {
	
	/**
	 * Checks for null pointer exceptions
	 * @param args - the arguments being checked for validity
	 */
	public static void checkNullInput(Object... args) {
		for (Object o : args) {
			if (o == null) {
				throw new NullPointerException();
			}
		}
	}
}
