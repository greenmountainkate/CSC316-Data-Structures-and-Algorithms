import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

public class TreeList {

	private treeListNode root;

	TreeList() {

		root = new treeListNode("");

	}

	void buildTree(String preOrderTraversal, String postOrderTraversal, treeListNode parentNode) {

		// pull this node from preOrderTraversal
		String nodeValue = preOrderTraversal.charAt(0) + "";
		// check for valid substring being passed - preOrderTraversal[0] should equal
		// postOrderTraversal[lastValue]
		String postValue = "" + postOrderTraversal.charAt(postOrderTraversal.length() - 1);

		if (!nodeValue.equals(postValue)) {
			System.out.println("FAILURE: Substrings invalid!!"); // TODO implement better failure protocol
			return;
		}

		// remove that value from preOrder and postOrder traversals
		preOrderTraversal = preOrderTraversal.substring(1, preOrderTraversal.length());
		postOrderTraversal = postOrderTraversal.substring(0, postOrderTraversal.length() - 1);

		// make new subtree root node
		treeListNode newSubRootNode = addNode(nodeValue, parentNode);

		// make new substrings and call buildTree on them recursively
		while (!preOrderTraversal.equals("")) {

			// base case - all values are leaf children of current subRootNode
			if (preOrderTraversal.equals(postOrderTraversal)) {
				for (int i = 0; i < preOrderTraversal.length(); i++) {
					String nodeVal = preOrderTraversal.charAt(i) + "";
					addNode(nodeVal, newSubRootNode);
				}
				return; // added all leaves to this parent node, return up the chain
			}
			// pull a new substring
			char newRootValue = preOrderTraversal.charAt(0); // get value of root of new subtree
			int locEndOfPostString = postOrderTraversal.indexOf(newRootValue);
			locEndOfPostString++;

			String newPreOrder = preOrderTraversal.substring(0, locEndOfPostString);
			String newPostOrder = postOrderTraversal.substring(0, locEndOfPostString);

			// remove it from preOrderTraversal and postOrderTraversal
			preOrderTraversal = preOrderTraversal.substring(locEndOfPostString, preOrderTraversal.length());
			postOrderTraversal = postOrderTraversal.substring(locEndOfPostString, postOrderTraversal.length());

			// call buildTree on it
			buildTree(newPreOrder, newPostOrder, newSubRootNode);

		}
	}

	// Add Node
	treeListNode addNode(String a, treeListNode parentNode) {

		if (isRoot(parentNode) && parentNode.value.equals("") && parentNode.parent == null) { // This is the first node
																								// to be added
			root.value = a;
			return root;
		}

		treeListNode newNode = new treeListNode(a);
		newNode.parent = parentNode;
		parentNode.childNodes.add(newNode);
		return newNode;

	}

	String preorderTraversal(treeListNode node) { //used for testing buildTree()
		// get value from this node
		String nodeStringPre = node.value;
		// visit children and append their values to string being built
		ListIterator<treeListNode> listIter = node.childNodes.listIterator();
		while (listIter.hasNext()) {
			nodeStringPre += preorderTraversal(listIter.next());
		}
		// return string
		return nodeStringPre;
	}

	String postorderTraversal(treeListNode node) { //used for testing buildTree()

		String nodeStringPost = "";
		// get values from child nodes
		ListIterator<treeListNode> listIter = node.childNodes.listIterator();
		while (listIter.hasNext()) {
			nodeStringPost += postorderTraversal(listIter.next());
		}
		// add value from this node
		nodeStringPost += node.value;
		// return composed String
		return nodeStringPost;
	}

	String inorderTraversal() {
		String inOrder = new String();
		Queue<treeListNode> inOrderTrav = new LinkedList<>(); // included per Dr. Rouskas's messageboard post 3/7/18 @
																// 16:56
		inOrderTrav.add(getRoot());
		while (!inOrderTrav.isEmpty()) {
			treeListNode node = inOrderTrav.remove();
			inOrder = inOrder + node.value;
			// while node has children
			ListIterator<treeListNode> listIter = node.childNodes.listIterator();
			while (listIter.hasNext()) {
				treeListNode childNode = listIter.next();
				inOrderTrav.add(childNode);
			}
		}
		return inOrder;
	}

	// Remove Node unnecessary

	// Find a target node containing a particular String value
	treeListNode lookup(String aString, treeListNode node) {

		if (node.value.equals(aString)) { // found it
			return node;
		} else {
			// continue searching. If incoming node isn't null, return node
			ListIterator<treeListNode> listIter = node.childNodes.listIterator();
			while (listIter.hasNext()) {
				treeListNode checkNode = this.lookup(aString, listIter.next());
				if (checkNode != null) {
					return checkNode;
				}
			}
		}

		return null; // if get to leaf without finding it, send back up to continue search
	}

	// isEmpty
	Boolean isEmpty(treeListNode node) {

		return (node.parent == null && node.childNodes.size() == 0 && node.value.equals(""));
	}

	// size()

	treeListNode getRoot() {
		return this.root;
	}

	Boolean isRoot(treeListNode node) {
		if(node == null){
			return false;
		}
		return (node.parent == null);

	}

	// isLeaf()
	Boolean isLeaf(treeListNode node) {
		if(node == null){
			return false;
		}
		return (node.parent != null && node.childNodes.size() == 0);
	}

	// isInternal()
	Boolean isInternal(treeListNode node) {
		if(node == null){
			return false;
		}
		return (node.parent != null && node.childNodes.size() > 0);

	}

	void markNodes(treeListNode node) {
		if(node == null ){
			return;
		}
		// check if is root -- base case
		if (isRoot(node)) {
			node.marked = true;
			return;
		} else {
			node.marked = true;
			markNodes(node.parent);
		}

	}

	void unmarkNodes(treeListNode node) {
		if(node == null){
			return;
		}
		// check if is root -- base case
		if (isRoot(node)) {
			node.marked = false;
			return;
		} else {
			node.marked = false;
			unmarkNodes(node.parent);
		}

	}

	String findLeastCommon(String bString, treeListNode node) {
		if(node == null){
			return "";
		}
		String lcaString = "";

		// check all nodes starting at node for marked == true
		if (node.marked) { // found the first mark
			return node.value;
		} else {
			lcaString = findLeastCommon(bString, node.parent);
		}
		return lcaString;
	}

	int distanceToLCA(treeListNode descendent, String ancestor) {
		if( descendent == null ){
			return -1;
		}
		int distanceCounter = 0;
		// reached the root
		if (descendent.equals(getRoot())) {
			return distanceCounter;
		}
		// this node is the ancestor
		if (descendent.value.equals(ancestor)) {
			return distanceCounter;
		}
		distanceCounter++; // increment because going up to check parent
		distanceCounter += distanceToLCA(descendent.parent, ancestor);

		return distanceCounter;
	}

	private class treeListNode {

		/** The character value in the node **/
		String value;

		/** Whether the node is marked */
		Boolean marked;

		/** The parent node **/
		private treeListNode parent;

		/** List of children nodes **/
		LinkedList<treeListNode> childNodes = new LinkedList<treeListNode>(); // included per Dr. Rouskas's messageboard
																				// post 3/7/18 @ 16:56

		public treeListNode(String letter) {

			this.value = letter;
			marked = false;

		}

	}

}
