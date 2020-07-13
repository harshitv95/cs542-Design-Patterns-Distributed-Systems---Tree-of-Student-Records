package studentskills.tree;

import java.util.HashMap;
import java.util.Map;

import studentskills.factory.StudentFactory;
import studentskills.store.StoreHelperI;
import studentskills.store.StudentStoreI;
import studentskills.store.factory.StudentStoreFactoryI;
import studentskills.tree.StudentRecord.Keys;
import studentskills.util.Logger;

public class TreeHelper implements StoreHelperI<StudentRecord> {
	protected final Map<Integer, StudentStoreI<StudentRecord>> trees;
	protected final StudentFactory studentFactory;
	protected final StudentStoreFactoryI<StudentRecord> treeFactory;
	protected final int replicaCount;

	/**
	 * Initializes the Treehelper used to construct {@code replicaCount} replicas of
	 * a Tree. The tree is initialized using {@code treeFactory}, and a
	 * StudentRecord is initialized using {@code studentFactory}
	 * 
	 * @param replicaCount
	 * @param treeFactory
	 * @param studentFactory
	 */
	public TreeHelper(int replicaCount, StudentStoreFactoryI<StudentRecord> treeFactory,
			StudentFactory studentFactory) {
		this.replicaCount = replicaCount;
		this.studentFactory = studentFactory;
		this.treeFactory = treeFactory;
		this.trees = new HashMap<>();
		for (int i = 0; i < replicaCount; i++)
			trees.put(i, this.treeFactory.create(i));
		Logger.debugHigh("TreeHelper initialized for [" + replicaCount + "] replicas");
	}

	@Override
	public void store(Map<Keys, Object> studentParams) {
		Logger.debugLow("Attempting to store new StudentRecord", studentParams);
		if (this.replicaCount == 0)
			return;

		StudentRecord[] records = new StudentRecord[this.replicaCount];
		records[0] = this.studentFactory.create(studentParams);
		Logger.debugMed("Created Student record Prototype", records[0]);

		try {
			// Creating all clones first, since if cloning fails at any point,
			// will not be inserting any of the previously cloned records in any trees,
			// since the trees would then be inconsistent
			for (int i = 1; i < this.replicaCount; i++) {
				records[i] = this.studentFactory.create(records[0]);
				Logger.debugMed("Cloned Student record from Prototype", records[i]);

				for (int j = 0; j < i; j++) {
					records[j].registerObserver(records[i]);
					records[i].registerObserver(records[j]);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to insert Student Records into tree", e);
		}

		for (int i = 0; i < this.replicaCount; i++)
			this.getStore(i).store(records[i]);
	}

	@Override
	public void modify(int storeId, Map<Object, Object> modifyParams) {
		if (!modifyParams.containsKey(Keys.B_NUMBER))
			throw new RuntimeException(Keys.B_NUMBER + " is a required param for modifying student record");

		int bNumber = (int) modifyParams.get(Keys.B_NUMBER);
		StudentRecord found = this.retrieve(storeId, bNumber);
		if (found != null)
			found.replaceValue(modifyParams.get("replaceValue"), modifyParams.get("replacement"));
		else
			Logger.warn("Attempted to modify student with B# ["+bNumber+"] that does not exist in records", modifyParams);
	}

	@Override
	public StudentRecord retrieve(int storeId, int studentId) {
		return this.getStore(storeId).retrieve(studentId);
	}

	@Override
	public StudentStoreI<StudentRecord> getStore(int storeId) {
		if (storeId < 0 || storeId > this.replicaCount)
			throw new IndexOutOfBoundsException(
					"Invalid replica ID [" + storeId + "], Replica numbers range from 0-" + (this.replicaCount - 1));
		return this.trees.get(storeId);
	}

}
