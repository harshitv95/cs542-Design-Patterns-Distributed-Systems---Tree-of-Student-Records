package studentskills.tree.bst;

import studentskills.tree.StudentRecord;
import studentskills.util.StudentStoreI;

public class StudentsBST implements StudentStoreI<StudentRecord> {

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

}
