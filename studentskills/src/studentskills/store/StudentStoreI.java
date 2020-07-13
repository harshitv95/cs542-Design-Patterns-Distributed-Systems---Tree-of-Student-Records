package studentskills.store;

import studentskills.tree.StudentRecord;
import studentskills.util.Results;

/**
 * A generic interface declaring basic methods to store and retrieve a student
 * record, which any Student Store (tree, list etc.) must use
 * 
 * @author Harshit Vadodaria
 *
 */
public interface StudentStoreI<S extends StudentRecord> {

	/**
	 * Saves the student in the current Store
	 * 
	 * @param student
	 */
	void store(S student);

	/**
	 * Retrieves the record of the student with ID {@code recordId}
	 * 
	 * @param recordId
	 * @return
	 */
	S retrieve(int recordId);

	/**
	 * Prints all records
	 * 
	 * @param res
	 */
	void printAll(Results res);

	/**
	 * Returns the number of distinct students currently in the store
	 * 
	 * @return the count of distinct students currently in the store
	 */
	int size();

	/**
	 * Checks if the student with ID {@code recordId} exists in the store
	 * 
	 * @param recordId
	 * @return true if the student exists, false otherwise
	 */
	default boolean exists(int recordId) {
		return this.retrieve(recordId) != null;
	}

}
