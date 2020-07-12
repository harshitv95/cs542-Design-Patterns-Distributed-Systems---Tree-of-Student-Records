package studentskills.tree.bst;

import studentskills.tree.StudentRecord;
import studentskills.tree.StudentTreeI;
import studentskills.util.Logger;
import studentskills.util.Results;

public class StudentsBST implements StudentTreeI<StudentRecord> {

	protected final int treeId;

	public StudentsBST(int treeId) {
		this.treeId = treeId;
	}

	protected static class StudentNode implements Comparable<StudentNode> {
		protected final StudentRecord student;
		protected StudentNode left, right;

		protected StudentNode(StudentRecord student) {
			this.student = student;
		}

		@Override
		public boolean equals(Object obj) {
			return (obj instanceof StudentNode) && (this.student.equals(((StudentNode) obj).student));
		}

		@Override
		public int compareTo(StudentNode o) {
			return o.student.equals(this.student) ? 0 : (this.student.getbNumber() < o.student.getbNumber() ? -1 : 1);
		}
	}

	protected StudentNode root;

	@Override
	public void store(StudentRecord student) {
		if (this.root == null)
			this.root = new StudentNode(student);
		else
			this.insert(this.root, new StudentNode(student));
	}

	protected void insert(StudentNode node, StudentNode insert) {
		if (node.student.equals(insert)) {
			node.student.replaceValues(insert.student);
		} else if (insert.compareTo(node) < 0) {
			if (node.left == null)
				node.left = insert;
			else
				insert(node.left, insert);
		} else {
			if (node.right == null)
				node.right = insert;
			else
				insert(node.right, insert);
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

	@Override
	public int getTreeId() {
		return this.treeId;
	}

	@Override
	public void printAll(Results res) {
		Logger.debugLow("Printing Tree " + this.getTreeId());
		this.printNodes(this.root, res);
	}

	protected void printNodes(StudentNode rootNode, Results res) {
		if (rootNode == null)
			return;
		this.printNodes(rootNode.left, res);
		res.printLn(rootNode.student.toString());
		this.printNodes(rootNode.right, res);
	}

}
