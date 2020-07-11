package studentskills.tree.bst;

import studentskills.store.StudentStoreI;
import studentskills.tree.StudentRecord;

public class StudentsBST implements StudentStoreI<StudentRecord> {

	protected final int treeId;

	public StudentsBST(int treeId) {
		this.treeId = treeId;
	}

	protected static class StudentNode {
		protected final StudentRecord student;
		protected StudentNode left, right;

		protected StudentNode(StudentRecord student) {
			this.student = student;
		}
	}

	protected StudentNode root;

	@Override
	public void store(StudentRecord student) {
		if (this.root == null)
			this.root = new StudentNode(student);
		else
			this.insert(this.root, student);
	}

	protected void insert(StudentNode node, StudentRecord student) {
		if (node.student.getbNumber() == student.getbNumber())
			node.student.addSkills(student.getSkills());
		else if (student.getbNumber() < node.student.getbNumber()) {
			if (node.left == null)
				node.left = new StudentNode(student);
			else
				insert(node.left, student);
		} else if (node.student.getbNumber() < student.getbNumber()) {
			if (node.right == null)
				node.right = new StudentNode(student);
			else
				insert(node.right, student);
		}
	}

	@Override
	public StudentRecord retrieve(int id) {
		StudentNode node = this.root;
		while (node != null) {
			if (node.student.getbNumber() == id)
				return node.student;

			if (id < node.student.getbNumber())
				node = node.left;
			else
				node = node.right;
		}
		return null;
	}

	@Override
	public int hashCode() {
		return this.treeId;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof StudentsBST) && this.hashCode() == obj.hashCode()
				&& this.root.equals(((StudentsBST) obj).root);
	}

}
