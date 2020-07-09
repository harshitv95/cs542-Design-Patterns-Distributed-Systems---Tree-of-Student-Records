package studentskills.tree.bst;

import studentskills.tree.StudentRecord;
import studentskills.util.StudentStoreI;

public class StudentsBST implements StudentStoreI<StudentRecord> {
	
	protected static class StudentNode {
		protected final StudentRecord student;
		protected StudentRecord left, right;
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
//		if (node.student.getbNumber() == student.getbNumber())
//			node.student
	}

	@Override
	public StudentRecord retrieve(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
