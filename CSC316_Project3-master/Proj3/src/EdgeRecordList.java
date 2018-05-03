import java.io.PrintStream;

public class EdgeRecordList {
	
	/** dummy that points to front of the list */
	private EdgeRecord head;
	int size;
	
	EdgeRecordList(){
		head = new EdgeRecord(0,0,0);
		head.next = null;
		size = 0;
	}
	
	void addToList(EdgeRecord f) {
		EdgeRecord e = new EdgeRecord(f.vertex1, f.vertex2, f.weight);
		//check for empty list
		if(head.next == null) {
			
			head.next = e; //point head to e, no need to update e pointer
			size++;
			return;
		}
		//add each record such that it is sorted by V1, then V2
		//insert at front of list
		if(head.next != null && e.vertex1 < head.next.vertex1) {
			
			e.next = head.next;
			head.next = e;
			size++;
			return;
		}
		//find where to insert e
		EdgeRecord previous = findPosition(e);
		
		if (previous.next != null) {
			
			e.next = previous.next;
			previous.next = e;
			size++;
			return;
		} else {
			
			previous.next = e; //previous was end of list, no need to update e pointer
			e.next = null;
			size++;
			return;
		}
	}

	private EdgeRecord findPosition(EdgeRecord e) {
		EdgeRecord pointer = new EdgeRecord(0,0,0);
		EdgeRecord trailer = new EdgeRecord(0,0,0);
		pointer.next = head.next;
		trailer.next = head.next;
		
		//search for position of v1 - while v1 > element
		while(pointer.next != null && pointer.next.vertex1 < e.vertex1) {
			
			trailer.next = pointer.next;
			pointer.next = pointer.next.next;
		}

	
		while(pointer.next != null && pointer.next.vertex1 == e.vertex1 && pointer.next.vertex2 < e.vertex2) {
			
			trailer.next = pointer.next;
			pointer.next = pointer.next.next;
		}

		return trailer.next;
	}

	public void printList(PrintStream fileWriter) {
		
		EdgeRecord pointer = new EdgeRecord(0, 0, 0);
		pointer.next = this.head.next;
		String mstString = "";
		for(int i = 0; i<size; i++) {
			EdgeRecord e = pointer.next;
		    mstString = String.format("%4d %4d", e.vertex1, e.vertex2);
			fileWriter.println(mstString);
			System.out.println(mstString);
			pointer.next = pointer.next.next; //advance pointer
		}
		
	}

}
