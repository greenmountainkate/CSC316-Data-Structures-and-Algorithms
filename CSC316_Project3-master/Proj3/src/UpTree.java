
public class UpTree {
	upTreeNode[] vertices;
	int size = 0;

	public UpTree(int numVertices) {
		vertices = new upTreeNode[numVertices];
	}

	void makeSet(int vertexPosition) {
		vertices[vertexPosition] = new upTreeNode(vertexPosition, -1);
		size++;
	}

	int pcFind(int target) {
		
		int root = find(target);
		int placeholder = target;
		while(placeholder != root) {
			int temp = placeholder;
			placeholder = vertices[temp].parent;
			vertices[temp].parent = root;	
		}
		
		return root;
	}

	int find(int position) {
		while (vertices[position].parent > 0) {
			position = vertices[position].parent;
		}
		return position;
	}

	int balUnion(int s, int t) {

		if (vertices[s].parent < vertices[t].parent) {
			vertices[s].parent += vertices[t].parent;
			vertices[t].parent = s;
			return s;
		} else {
			vertices[t].parent += vertices[s].parent;
			vertices[s].parent = t;
			return t;
		}
	}

	private class upTreeNode {
		int keyVertex;
		//int valueNumber;
		int parent;

		upTreeNode(int vertex, int parentLocation) {
			this.keyVertex = vertex;
			// this.valueNumber = number;
			this.parent = parentLocation;

		}
	}

}
