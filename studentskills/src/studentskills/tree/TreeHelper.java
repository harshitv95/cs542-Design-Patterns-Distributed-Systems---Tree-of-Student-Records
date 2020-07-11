package studentskills.tree;

import java.util.HashMap;
import java.util.Map;

import studentskills.factory.StudentFactory;
import studentskills.store.StoreHelperI;
import studentskills.store.StudentStoreI;
import studentskills.store.factory.StudentStoreFactory;
import studentskills.tree.StudentRecord.Keys;

public class TreeHelper implements StoreHelperI<StudentRecord> {
	protected final Map<Integer, StudentStoreI<StudentRecord>> trees;
	protected final StudentFactory studentFactory;
	protected final StudentStoreFactory<StudentRecord> treeFactory;
	protected final int replicaCount;

	public TreeHelper(int replicaCount, StudentStoreFactory<StudentRecord> treeFactory, StudentFactory studentFactory) {
		this.replicaCount = replicaCount;
		this.studentFactory = studentFactory;
		this.treeFactory = treeFactory;
		this.trees = new HashMap<>();
		for (int i = 0; i < replicaCount; i++)
			trees.put(i, this.treeFactory.create(i));
	}

	@Override
	public void store(Map<Keys, Object> studentParams) {
		if (this.replicaCount == 0)
			return;
		StudentRecord[] records = new StudentRecord[this.replicaCount];
		records[0] = this.studentFactory.create(studentParams);

		try {
			// Creating all clones first, since if cloning fails at any point,
			// will not be inserting any of the previously cloned records in any trees,
			// since the trees would then be inconsistent
			for (int i = 1; i < this.replicaCount; i++) {
				records[i] = this.studentFactory.create(records[0]);

				for (int j = 0; j <= i; j++) {
					records[j].registerObserver(records[i]);
					records[i].registerObserver(records[j]);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to insert Student Records into tree", e);
		}

		for (int i = 0; i < this.replicaCount; i++)
			this.trees.get(i).store(records[i]);
	}

	@Override
	public void modify(int storeId, Map<Object, Object> modifyParams) {
		if (storeId < 0 || storeId > this.replicaCount)
			throw new IndexOutOfBoundsException(
					"Invalid replica ID [" + storeId + "], Replica numbers range from 0-" + (this.replicaCount - 1));
		if (!modifyParams.containsKey(Keys.B_NUMBER))
			throw new RuntimeException(Keys.B_NUMBER + " is a required param for modifying student record");
		int bNumber = (int) modifyParams.get(Keys.B_NUMBER);
		this.trees.get(storeId).retrieve(bNumber).replaceValue(modifyParams.get("replaceValue"),
				modifyParams.get("replacement"));
	}

	@Override
	public StudentRecord retrieve(int storeId, int studentId) {
		// TODO Auto-generated method stub
		return null;
	}

}
