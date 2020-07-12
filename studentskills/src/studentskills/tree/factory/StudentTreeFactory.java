package studentskills.tree.factory;

import studentskills.store.StudentStoreI;
import studentskills.store.factory.StudentStoreFactoryI;
import studentskills.tree.StudentRecord;
import studentskills.tree.bst.StudentsBST;

/**
 * If storing the Student Records in a tree, this implementation of
 * {@link StudentStoreFactoryI} can provide you with a Tree. It currently returns
 * an instance of a Simple Binary Search Tree implementation, {@link StudentsBST}.
 * This can be changed to instantiate any other implementation of StudentStoreI
 * 
 * @author Harshit Vadodaria
 *
 */
public class StudentTreeFactory implements StudentStoreFactoryI<StudentRecord> {

	@Override
	public StudentStoreI<StudentRecord> create(int storeId) {
		return new StudentsBST(storeId);
	}

}
