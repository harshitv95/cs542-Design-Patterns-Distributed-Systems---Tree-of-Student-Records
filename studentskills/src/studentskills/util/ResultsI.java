package studentskills.util;

/**
 * A generic results interface, expecting the implementation to support printing
 * a string to the desired destinations.
 * 
 * @author Harshit Vadodaria
 *
 */
public interface ResultsI {

	/**
	 * Similar to {@link #print(String)}}, but moves cursor to new line after
	 * printing to all available OutputStream Writers, i.e. all the preconfigured
	 * Write destinations
	 * 
	 * @param printStr
	 */
	void printLn(String printStr);

	/**
	 * Prints {@code printStr} to all available OutputStream Writers
	 * 
	 * @param printStr
	 */
	public void print(String printStr);

}
