import java.io.PrintStream;

public class HeapArray {
	// make Array
	HeapNode[] heapArray = new HeapNode[5000];
	// make size counter
	int size = 0;

	void insert(double weight, EdgeRecord e, HeapNode[] h) {
		h[size] = new HeapNode(weight, e);
		size++;
		upHeap(h, size - 1);
	}

	private void upHeap(HeapNode[] h, int i) {
		int target = (i - 1) / 2;
		if (i > 0 && h[target].key > h[i].key) {
			swap(target, i, h);
			upHeap(h, target);
		}

	}

	private void swap(int target, int i, HeapNode[] h) {
		HeapNode temp = new HeapNode(h[target].key, h[target].edgeRec);
		h[target] = h[i];
		h[i] = temp;

	}
	
	public EdgeRecord deleteMin() {
		EdgeRecord e = heapArray[0].edgeRec;
		size--;
		if(size > 0) {
		swap(0, size, heapArray);
		downHeap(heapArray, 0);
		}
		return e;
		
	}

	private void downHeap(HeapNode[] h, int m) {
		int i = 0;
		int rChild = 2 * m + 2;
		int lChild = 2 * m + 1;
		
		if (rChild < size) {
			if (h[rChild].key <= h[lChild].key) {
				i = rChild;
			} else {
				i = lChild;
			}
		} else if (lChild < size) {
			i = lChild;
		}
		
		if (i>0 && h[m].key > h[i].key ) {
			swap(m, i, h);
			downHeap(h, i);
		}
		
	}
	
	void printHeap(PrintStream fileWriter) {
		//open file for appended printing
		String heapLine = "";
		for(int i = 0; i< size; i++ ) {
			int v1 = heapArray[i].edgeRec.vertex1;
			int v2 = heapArray[i].edgeRec.vertex2;
			if(v1 < v2) {
				heapLine = String.format("%4d %4d", v1, v2);
			} else {
				heapLine = String.format("%4d %4d", v2, v1);
			}
			fileWriter.println(heapLine);
			System.out.println(heapLine);
			
	
		}
		
	}

	private class HeapNode {

		double key = 0;
		EdgeRecord edgeRec = null;

		public HeapNode(double weight, EdgeRecord e) {
			this.key = weight;
			this.edgeRec = e;
		}

	}

}
