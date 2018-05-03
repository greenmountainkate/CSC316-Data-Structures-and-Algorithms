import java.io.PrintStream;

public class AdjacencyList {

	/** dummy that points to front of the list */
	private EdgeRecord head;
	int size;
	int vertex;
	
	AdjacencyList(int vertex){
		head = new EdgeRecord(0,0,0);
		head.next = null;
		size = 0;
		this.vertex = vertex;
	}

	void addToList(EdgeRecord f) {
		
		EdgeRecord e = new EdgeRecord(f.vertex1, f.vertex2, f.weight);
				
		// insert sorted and update e pointer to next
		if (size == 0) {// emptyList
			head.next = e;//point head at e
			e.next = null;
			size++;
			return;
		} else {
			//insert at front of list
			if(head.next != null && e.vertex2 < head.next.vertex2) {
				e.next = head.next;
				head.next = e;
				size++;
				return;
			}
			
			// find position to insert - sorted by v2
			EdgeRecord previous = findPosition(e);

			if (previous.next != null) {// in middle of list
				
				e.next = previous.next;
				previous.next = e;
				size++;
				return;
			} else {// at end of list
				
				previous.next = e; 
				e.next = null;
				size++;
				return;				
			}			
		}
	}

	private EdgeRecord findPosition(EdgeRecord e) {
		EdgeRecord pointer = new EdgeRecord(0,0,0);
		EdgeRecord trailer = new EdgeRecord(0,0,0);
		pointer.next = head.next;
		trailer.next = head.next;
		//search for position of v2 - while v2 > element
		while(pointer.next != null && pointer.next.vertex2 < e.vertex2) {
			trailer.next = pointer.next;
			pointer.next = pointer.next.next;
		}
		return trailer.next;
	}

	public void printList(PrintStream fileWriter) {
		EdgeRecord pointer = new EdgeRecord(0, 0, 0);
		pointer.next = this.head.next;
		String adjString = "";
		for(int i = 0; i<this.size; i++) {
			EdgeRecord e = pointer.next;
			fileWriter.print(String.format("%4d", e.vertex2));
			System.out.print(String.format("%4d", e.vertex2));
			if(i != size-1) {
				fileWriter.print(" ");
				System.out.print(" ");
			}
			pointer.next = pointer.next.next; //advance pointer
		}
		
		fileWriter.println(adjString);
		System.out.println(adjString);
		
	}

}