
public class Vertex {
	
	int vertexNumber;
	AdjacencyList adj;
	public Vertex(int uVert) {
		vertexNumber = uVert;
		adj = new AdjacencyList(uVert);
	}

}
