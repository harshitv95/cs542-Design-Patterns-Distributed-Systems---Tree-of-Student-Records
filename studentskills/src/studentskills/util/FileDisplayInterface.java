package studentskills.util;

import java.io.Writer;

public interface FileDisplayInterface {

	/**
	 * Prints {@code printStr} to a file
	 * 
	 * @param printStr : String to print to a file
	 */
	void printToFile(String printStr);

	/**
	 * Initialize a Writer instance that writes to the file with name
	 * {@code filename}
	 * 
	 * @param filename
	 * @return {@link Writer}
	 */
	Writer initFileWriter(String filename);

}
