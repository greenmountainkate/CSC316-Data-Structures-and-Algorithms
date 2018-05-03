import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class proj3 {

	public static void main(String[] args) throws FileNotFoundException {

		// Prompt User for input and output file names
		// Open Scanner for System.in
		Scanner scan = new Scanner(System.in);
		// Print prompt for input file name
		System.out.println("Please enter a valid input filename: ");
		// Capture input file name
		String inputFileName = scan.nextLine();
		// Print prompt for output file name
		System.out.println("Please enter a valid output filename: ");
		// Capture output file name
		String outputFileName = scan.nextLine();
		
		scan.close();

		// Make Heap
		HeapArray heap = new HeapArray();
		
		// Make vertex counter Array
		ArrayList<Integer> vertexList = new ArrayList<Integer>();
		
		// Make Vertex Array
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		

		
		// open input file
		File iFile = new File(inputFileName);
		
		// Read in File
		if (iFile.canRead()) {
			
			// open Scanner to read file
			Scanner fileReader = new Scanner(iFile);
			// Process each Line
			while (fileReader.hasNextLine()) {
				
				int uVert = fileReader.nextInt();
				
				if(uVert == -1) {
					break;
				}
				
				int vVert = fileReader.nextInt();
								
				if (uVert > vVert) { // sort so initial EdgeRecord added to heap has sorted vertices
					
					int temp = uVert;
					uVert = vVert;
					vVert = temp;
					
				}
				float weight = fileReader.nextFloat();
				
				// Make EdgeRecord and insert in heap, such that v1 < v2
				EdgeRecord e = new EdgeRecord(uVert, vVert, weight);
				heap.insert(weight, e, heap.heapArray);
				
				// Make EdgeRecord2, such that v2 >= v1 - this facilitates later sorting
				EdgeRecord e2 = new EdgeRecord(vVert, uVert, weight);
				
				
				
				
				// Attempt to add vertex1 to vertexList
				if (!vertexList.contains(uVert)) {
					vertexList.add(uVert);
					Vertex u = new Vertex(uVert);
					vertices.add(u);
					u.adj.addToList(e);
					
				} else { // vertex1 already there
					// add edgeRecord to vertex1 adjacencyList
					vertices.get(vertexList.indexOf(uVert)).adj.addToList(e);
				}

				// Attempt to add vertex2 to vertexList
				if (!vertexList.contains(vVert)) {
					vertexList.add(vVert);
					Vertex v = new Vertex(vVert);
					vertices.add(v);
					v.adj.addToList(e2);
				} else { // vertex2 already there
					// add edgeRecord2 to vertex2 adjacencyList
					vertices.get(vertexList.indexOf(vVert)).adj.addToList(e2);
				}
			}
			
			fileReader.close();

		}

		
		// Make UpTree
		int numVertices = vertexList.size();
		UpTree vertexTree = new UpTree(numVertices);
		// Add all Vertices as individually connected elements
		for (int i = 0; i < numVertices; i++) {
			vertexTree.makeSet(i);
		}

		// Open OutputFile for writing and create fileWriter
		File oFile = new File(outputFileName);
		if (oFile.canWrite()) {
			PrintStream fileWriter = new PrintStream(oFile);
			
			// Output Heap
			heap.printHeap(fileWriter);
			
			// Make MST
			EdgeRecordList minSpanTree = new EdgeRecordList();
			int numV = numVertices;
			
			// Run Kruskals Algorithm
			while (numV > 1) {
				//System.out.println("Kruskal Executing: " + numV);
				EdgeRecord e = heap.deleteMin();
				int u = e.vertex1;
				int v = e.vertex2;
				int uPosition = vertexTree.pcFind(u);
				int vPosition = vertexTree.pcFind(v);

				if (uPosition != vPosition) {
					vertexTree.balUnion(uPosition, vPosition);
					minSpanTree.addToList(e); // adds sorted by v1 and v2
					numV--;
				}
			}

			// Output MST
			minSpanTree.printList(fileWriter);
			
			// Output Adjacency List
			for (int i = 0; i < numVertices; i++) {
				// find location in vertexList
				vertices.get(vertexList.indexOf(i)).adj.printList(fileWriter);// print that adjacencyList in vertices[location]
			}

			fileWriter.close();
		}

	}

}
