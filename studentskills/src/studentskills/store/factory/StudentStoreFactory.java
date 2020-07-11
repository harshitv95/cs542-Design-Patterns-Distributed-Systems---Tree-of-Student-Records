package studentskills.store.factory;

import studentskills.store.StudentStoreI;
import studentskills.tree.StudentRecord;

/**
 * A factory interface to instantiate a StudentStore, i.e. a Tree, or a Map, or
 * a List, or a DB store class etc., which implements the {@link StudentStoreI}
 * interface
 * 
 * @author Harshit Vadodaria
 *
 * @param <S> extends {@link StudentRecord} - The type of the instance to be
 *            stored
 */
public interface StudentStoreFactory<S extends StudentRecord> {
	/**
	 * Creates and returns an instance of a class implementing the {@link StudentStoreI}
	 * interface. Every store needs a unique store ID, which makes the {@link }
	 * 
	 * @param storeId
	 * @return An instance of a class implementing the {@link StudentStoreI}
	 */
	public StudentStoreI<S> create(int storeId);
}
