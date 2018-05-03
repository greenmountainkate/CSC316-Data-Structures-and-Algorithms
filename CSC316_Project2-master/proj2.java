import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class proj2 {

	public static void main(String[] args) throws IOException {
		// TODO implement as prompt, not command line!
		// String inputFileName = args[0];
		// String outputFileName = args[1];

		//Open Scanner for System.in
		Scanner scan = new Scanner(System.in);
		//Print prompt for input file name
		System.out.println("Please enter a valid input filename: ");
		//Capture input file name
		String inputFileName = scan.nextLine();
		//Print prompt for output file name
		System.out.println("Please enter a valid output filename: ");
		//Capture output file name
		String outputFileName = scan.nextLine();
		
		processFiles(inputFileName, outputFileName);
		scan.close();
	}

	private static void processFiles(String inputFileName, String outputFileName) throws IOException {

		// open input file
		File iFile = new File(inputFileName);

		if (iFile.canRead()) {

			// open Scanner to read file
			Scanner fileReader = new Scanner(iFile);

			// Strings to hold preorder and postorder traversals
			String preOrderTraversal = new String();
			String postOrderTraversal = new String();

			// get first line of input file
			if (fileReader.hasNextLine()) {

				String preLine = fileReader.nextLine();

				// check for valid initial character '<' in preorder traversal
				char preTest = preLine.charAt(0);
				if (preTest != '<') {
					System.out.println("Invalid File Format");
					fileReader.close();
					throw new IllegalArgumentException();
				}

				for (int i = 1; i < preLine.length(); i++) { // index at 1 since 0 is '<'
					if (preLine.charAt(i) != ',' && preLine.charAt(i) != ' ' && preLine.charAt(i) != '.') {
						preOrderTraversal = preOrderTraversal + preLine.charAt(i);
					}
				}

			} else {
				System.out.println("Invalid File Format: no preorder traversal");
				fileReader.close();
				throw new IllegalArgumentException();
			}

			// get second line of input file
			if (fileReader.hasNextLine()) {

				String postLine = fileReader.nextLine();

				// check for valid initial character '>' in postorder traversal
				char postTest = postLine.charAt(0);
				if (postTest != '>') {
					System.out.println("Invalid File Format");
					fileReader.close();
					throw new IllegalArgumentException();
				}

				for (int i = 1; i < postLine.length(); i++) { // index at 1 since 0 is '<'
					if (postLine.charAt(i) != ',' && postLine.charAt(i) != ' ' && postLine.charAt(i) != '.') {
						postOrderTraversal = postOrderTraversal + postLine.charAt(i);
					}
				}

			} else {
				System.out.println("Invalid File Format: no postorder traversal");
				fileReader.close();
				throw new IllegalArgumentException();
			}

			// build tree
			FamilyTree famTree = new FamilyTree();

			famTree = famTree.buildTree(preOrderTraversal, postOrderTraversal);

			// open output file
			File oFile = new File(outputFileName);
			if (oFile.canWrite()) {
				PrintStream fileWriter = new PrintStream(oFile);
				// intake relationships to test
				while (fileReader.hasNext()) {
					String relLine = fileReader.nextLine();
					// check for valid line
					if (relLine.charAt(0) != '?') {
						System.out.println("Invalid File Format: no relationship query");
						fileReader.close();
						fileWriter.close();
						throw new IllegalArgumentException();
					}
					String relationshipLine = new String();
					// read in relationship String
					for (int i = 1; i < relLine.length(); i++) { // index at 1 since 0 is '<'
						if (relLine.charAt(i) != ',' && relLine.charAt(i) != ' ' && relLine.charAt(i) != '.') {
							relationshipLine = relationshipLine + relLine.charAt(i);
						}
					}

					String descendentA = relationshipLine.charAt(0) + "";
					String descendentB = relationshipLine.charAt(1) + "";

					// print relationships
					String relString = famTree.findRelationship(descendentA, descendentB);

					System.out.println(relString);
					fileWriter.println(relString);

				}

				// do traversal
				String inOrderTraversal = famTree.inOrderTraversal();

				// print traversal
				System.out.println(inOrderTraversal);
				fileWriter.println(inOrderTraversal);

				fileWriter.close();

			}

			fileReader.close();

		}
	}

}
