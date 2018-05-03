import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class proj4 {

	static int dictWordCount;
	static int textWordCount;
	static int missWordCount;
	static int lookupCount;

	
	static final int alpha = 3042;
	static final int beta = 17343;
	static final int primeN = 26053;
	static final int bucketN = 26000;

	
	static ArrayList<String> dictArray = new ArrayList<String>(26000);
	static ArrayList<Integer> hashArray = new ArrayList<Integer>(26000);
	static ArrayList<Integer> compArray = new ArrayList<Integer>(26000);

	public static void main(String[] args) {

		String dictName = args[0];
		String textName = args[1];
		String outputName = args[2];
		
		HashTable hashTable = new HashTable(bucketN);

		processDict(dictName, hashTable);

		try {

			FileReader fr = new FileReader(textName);
			FileWriter fw = new FileWriter(outputName);
			BufferedWriter buffer = new BufferedWriter(fw);
			PrintWriter outfile = new PrintWriter(buffer);

			processTextFile(fr, outfile, hashTable);
			processOutput(outfile, hashTable);

			outfile.close();

		} catch (FileNotFoundException e) {
			System.err.println("Bad File Name");
		} catch (IOException e) {
			System.err.println("Error in manipulating file");
		}
	}

	private static void processDict(String fileName, HashTable hashTable) {

		try {

			FileReader fr = new FileReader(fileName);
			BufferedReader input = new BufferedReader(fr);

			String line = new String();
			String word = new String();

			StringTokenizer st;
			while ((line = input.readLine()) != null) {
				st = new StringTokenizer(line);
				while (st.hasMoreTokens()) {
					word = st.nextToken();
					dictWordCount++;
					hashInsert(word, hashTable);
				}
			}

			input.close();
		} catch (FileNotFoundException e) {
			System.err.println("Bad File Name: " + fileName);
		} catch (IOException e) {
			System.err.println("Error in reading from file" + fileName);
		}

	}

	private static void hashInsert(String word, HashTable hashTable) {
		
		System.out.println(word);		
		
		int hashVal = getHashVal(word);
		System.out.println("Hash Value of " + word + " is " + hashVal);

		int hashPosition = compressHash(hashVal);
		System.out.println("The comp hashPosition of " + word + " is " + hashPosition);
		
		hashTable.addToTable(word, hashPosition);		

	}

	private static int compressHash(int hashVal) {
		int compVal = (((alpha * hashVal) + beta) % primeN) % bucketN;
		return Math.abs(compVal);
		
		
	}

	private static int getHashVal(String word) {
		int hashVal = 0;
		for(int i=0; i<word.length(); i++) {
			hashVal = (hashVal << 5) | (hashVal >>> 27);
			hashVal += (int)word.charAt(i);
		}
		
		return hashVal;
	}

	private static void processOutput(PrintWriter outfile, HashTable hashTable) {
		
		outfile.println("Number of words in Dictionary: " + dictWordCount);
		outfile.println("Number of words in Text: " + textWordCount);
		outfile.println("Number of misspelled words: " + missWordCount);
		outfile.println("Total number of probes: " + hashTable.probeCount);
		
		double probesPerWord = (double)hashTable.probeCount/(double)textWordCount;
		
		outfile.println("Average probes per word: " + probesPerWord);
		
		double probesPerLookup = (double)hashTable.probeCount/(double)lookupCount;
		
		outfile.println("Average probes per lookup: " + probesPerLookup);
	}

	private static void processTextFile(FileReader fr, PrintWriter outfile, HashTable hashTable) {

		boolean misspelled = false;
		try {

			BufferedReader input = new BufferedReader(fr);

			String line = new String();

			while ((line = input.readLine()) != null) {
				/*
				 * if (line.equals("") || line.equals("\n")|| line.equals("\r") ||
				 * line.equals("\f")) { continue; }
				 */
				String[] words = line.split("[^A-Za-z0-9']+");
				for (int i = 0; i < words.length; i++) {
					if (words[i].equals("") || words[i].equals("\n") || words[i].equals("\r")
							|| words[i].equals("\f") || isNumeric(words[i])) {
						continue;
					}
					misspelled = checkSpelling(words[i], hashTable);
					textWordCount++;
					if (!misspelled) {
						outfile.println("Misspelled word: " + words[i]);
						missWordCount++;
					} else {
						outfile.println("Correctly spelled word: " + words[i]);
					}
				}
			}

			input.close();

		} catch (IOException e) {
			System.err.println("Error in reading from text file");
		}

		System.out.println("There are " + textWordCount + " words in the text file.");

	}

	private static boolean isNumeric(String string) {
		char c;
		for(int i=0; i< string.length(); i++) {
			c = string.charAt(i);
			if(!Character.isDigit(c)) {
				return false;
			}
		}
		
		return true;
	}

	private static boolean checkSpelling(String word, HashTable hashTable) { // TODO consider ways to refactor to prevent weedersed from
														// being marked correct

		System.out.println(word);

		// base case - word is in dictionary => return true
		boolean wordFound = hashTable.checkTable(word, compressHash(getHashVal(word)));
		lookupCount++;
		if (wordFound) {
			return wordFound;
		}

		// if first letter is capitalized, make first letter lowercase and try again
		if (word.length() == 1) {
			wordFound = checkSpelling(word.toLowerCase(), hashTable);
			lookupCount++;
			if (wordFound) {
				return wordFound;
			}
		}
			
		if (!word.substring(0, 1).equals(word.substring(0, 1).toLowerCase())) {
			
			if (word.length() == 2){
				wordFound = checkSpelling(word.substring(0, 1).toLowerCase() + word.charAt(1), hashTable);
				lookupCount++;
				if (wordFound) {
					return wordFound;
				}
				
			} else {
				wordFound = checkSpelling(word.substring(0, 1).toLowerCase() + word.substring(1, word.length()), hashTable);
				lookupCount++;
				if (wordFound) {
					return wordFound;
				}

			}
		}

		// any word less than 2 letters doesn't fit rules and if !found in previous
		// check => misspelled
		if (word.length() > 2) {

			String lastLetter = word.substring(word.length() - 1, word.length());
			String last2Letters = word.substring(word.length() - 2, word.length());
			String last3Letters = word.substring(word.length() - 3, word.length());

			// check S and flow through to check 'S or ES
			if (lastLetter.equals("s")) {

				wordFound = checkSpelling(word.substring(0, word.length() - 1), hashTable);
				lookupCount++;

				if (wordFound) {
					return wordFound;
				} else if (last2Letters.equals("'s") || last2Letters.equals("es")) {

					wordFound = checkSpelling(word.substring(0, word.length() - 2), hashTable);
					lookupCount++;

					if (wordFound) {
						return wordFound;
					}
				}
			}
			// check ED or ER and flow through to check add E
			if (last2Letters.equals("ed") || last2Letters.equals("er")) {

				wordFound = checkSpelling(word.substring(0, word.length() - 2), hashTable);
				lookupCount++;

				if (wordFound) {
					return wordFound;
				} else {
					wordFound = checkSpelling(word.substring(0, word.length() - 1), hashTable); // check with e
					lookupCount++;

					if (wordFound) {
						return wordFound;
					}
				}
			}

			// check ING and flow through to check add E
			if (last3Letters.equals("ing")) {
				wordFound = checkSpelling(word.substring(0, word.length() - 3), hashTable);
				lookupCount++;

				if (wordFound) {
					return wordFound;
				} else {
					wordFound = checkSpelling(word.substring(0, word.length() - 3) + "e", hashTable); // check with e
					lookupCount++;

					if (wordFound) {
						return wordFound;
					}
				}

			}

			// check LY
			if (last2Letters.equals("ly")) {

				wordFound = checkSpelling(word.substring(0, word.length() - 2), hashTable);
				lookupCount++;

				if (wordFound) {
					return wordFound;
				}
			}
		}

		// if reach here, word was not in dictionary
		return false;

	}


}


