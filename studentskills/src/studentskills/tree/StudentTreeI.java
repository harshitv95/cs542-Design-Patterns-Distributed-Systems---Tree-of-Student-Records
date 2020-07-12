package studentskills.tree;

import studentskills.store.StudentStoreI;

/**
 * A simple interface created with the sole purpose of type restricting some
 * implementations of {@code StudentStoreI} to be a Tree-based implementation.
 * 
 * 
 * @author Harshit Vadodaria
 *
 * @param <S>
 */
public interface StudentTreeI<S extends StudentRecord> extends StudentStoreI<S> {
	/**
	 * @return Unique Tree ID Associated with this Tree (In most cases, the store
	 *         ID)
	 */
	int getTreeId();
}
