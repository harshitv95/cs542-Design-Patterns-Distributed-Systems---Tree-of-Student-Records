package studentskills.store;

import studentskills.tree.StudentRecord;

/**
 * A generic interface declaring basic methods to store and retrieve a student
 * record, which any Student Store (tree, list etc.) must use
 * 
 * @author Harshit Vadodaria
 *
 */
public interface StudentStoreI<S extends StudentRecord> {

	void store(S student);

	S retrieve(int recordId);

	default boolean exists(int recordId) {
		return this.retrieve(recordId) != null;
	}

}
