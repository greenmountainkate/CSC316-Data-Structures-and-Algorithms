
public class FamilyTree {

	private TreeList familyTree = new TreeList();

	FamilyTree buildTree(String preOrderTraversal, String postOrderTraversal) {

		familyTree.buildTree(preOrderTraversal, postOrderTraversal, familyTree.getRoot());

		return this;

	}

	void markAncestors(String a) {

		// call lookup to find node to start at and pass to markNodes to change mark
		// from node to root
		familyTree.markNodes(familyTree.lookup(a, familyTree.getRoot()));

	}

	void unmarkAncestors(String a) {

		// call lookup to locate starting node and pass to unmark to change marks from
		// node to root
		familyTree.unmarkNodes(familyTree.lookup(a, familyTree.getRoot()));

	}

	String findLeastCommon(String b) {

		return familyTree.findLeastCommon(b, familyTree.lookup(b, familyTree.getRoot()));
	}

	int distanceToLCA(String descendent, String ancestor) {
		return familyTree.distanceToLCA(familyTree.lookup(descendent, familyTree.getRoot()), ancestor);
	}

	String findRelationship(String a, String b) {

		// mark tree from a to ancestor
		this.markAncestors(a);

		// find least common ancestor
		String ancestor = findLeastCommon(b);

		this.unmarkAncestors(a); // make sure to unmark

		if(ancestor.equals("")){
			return "There is no relationship";
		}

		// calc distance to lca from each
		int aToAncestor = distanceToLCA(a, ancestor);
		int bToAncestor = distanceToLCA(b, ancestor);

		String rel = relationshipToString(aToAncestor, bToAncestor);

		String relFormat = a + " is " + b + rel + ".";

		return relFormat;
	}

	private String relationshipToString(int aToAncestor, int bToAncestor) {

		String relString = "";

		if (aToAncestor == 0) {
			if (bToAncestor == 0) {
				return relString;
			} else if (bToAncestor == 1) {
				relString = "'s parent";
				return relString;
			} else if (bToAncestor == 2) {
				relString = "'s grandparent";
				return relString;
			} else if (bToAncestor > 2) {
				String great = "great";
				bToAncestor -= 3;
				while (bToAncestor > 0) {
					great = great + "-great";
					bToAncestor--;
				}
				relString = "'s " + great + "-grandparent";
				return relString;
			}

		}

		if (bToAncestor == 0) {
			if (aToAncestor == 1) {
				relString = "'s child";
				return relString;
			} else if (aToAncestor == 2) {
				relString = "'s grandchild";
				return relString;
			} else if (aToAncestor >= 3) {
				String great = "great";
				aToAncestor -= 3;
				while (aToAncestor > 0) {
					great = great + "-great";
					aToAncestor--;
				}
				relString = "'s " + great + "-grandchild";
				return relString;

			}
		}

		if (aToAncestor == 1) {
			if (bToAncestor == 1) {
				relString = "'s sibling";
				return relString;
			} else if (bToAncestor == 2) {
				relString = "'s aunt/uncle";
				return relString;
			} else if (bToAncestor >= 3) {
				String great = "great";
				bToAncestor -= 3;
				while (bToAncestor > 0) {
					great = great + "-great";
					bToAncestor--;
				}
				relString = "'s " + great + "-aunt/uncle";
				return relString;
			}
		}

		if (bToAncestor == 1) {
			if (aToAncestor == 2) {
				relString = "'s niece/nephew";
				return relString;
			} else if (aToAncestor >= 3) {
				String great = "great";
				aToAncestor -= 3;
				while (aToAncestor > 0) {
					great = great + "-great";
					aToAncestor--;
				}
				relString = "'s " + great + "-niece/nephew";
				return relString;

			}

		}

		int cousinValue = Math.min(aToAncestor, bToAncestor) - 1;
		int cousinDistance = Math.abs(aToAncestor - bToAncestor);
		relString = "'s " + cousinValue + "th cousin " + cousinDistance + " times removed";
		return relString;

	}

	String inOrderTraversal() {

		String inOrder = familyTree.inorderTraversal();

		// process inOrder
		String formatInOrder = "" + inOrder.charAt(0); // read in first letter
		for (int i = 1; i < inOrder.length(); i++) {
			formatInOrder = formatInOrder + ", " + inOrder.charAt(i);
		}
		formatInOrder = formatInOrder + ".";
		return formatInOrder;
	}

}
