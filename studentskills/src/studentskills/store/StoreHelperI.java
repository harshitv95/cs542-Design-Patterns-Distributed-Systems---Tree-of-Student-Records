package studentskills.store;

import java.util.Map;

import studentskills.tree.StudentRecord;
import studentskills.tree.StudentRecord.Keys;

public interface StoreHelperI<S extends StudentRecord> {
	/**
	 * Saves a student record in the current Store
	 * 
	 * @param studentParams int
	 */
	void store(Map<Keys, Object> studentParams);

	/**
	 * Modifies 1 or more properties of existing record
	 * 
	 * @param storeId int
	 * @param modifyParams Map<Keys, Object>
	 */
	void modify(int storeId, Map<Object, Object> modifyParams);

	/**
	 * @param storeId int
	 * @param studentId int - the StudentID of the student to search in the store
	 * @return
	 */
	S retrieve(int storeId, int studentId);
}
