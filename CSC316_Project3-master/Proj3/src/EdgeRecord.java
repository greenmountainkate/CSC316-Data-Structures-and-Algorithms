
public class EdgeRecord {
	int vertex1;
	int vertex2;
	float weight;
	EdgeRecord next;
	
	EdgeRecord(int v1, int v2, float d){
		this.vertex1 = v1;
		this.vertex2 = v2;
		this.weight = d;
		this.next = null;
	}
}
