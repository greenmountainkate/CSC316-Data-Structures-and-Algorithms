

public class HashTable {
	
	public int probeCount;
	public WordNode[] hashArray;
	
	public HashTable(int size) {
		hashArray = new WordNode[size];
	}
	
	public void addToTable(String word, int position) {
		
		//TODO handle insertion into hash table
		WordNode node = new WordNode(word);
		//check position of table
		if(hashArray[position]== null) {
			hashArray[position] = node;
			return;
		} else {
			
			WordNode tempNode = new WordNode("");
			tempNode.next = hashArray[position];
			
			while(tempNode.next.next != null) {
				tempNode.next = tempNode.next.next;		
			}
			
			tempNode.next.next = node;
		}
	}
	
	public boolean checkTable(String word, int position) {
		
		WordNode temp = hashArray[position];
		probeCount++;
		if(temp == null) {
			return false;
		}else if(temp.word.equals(word)) {
			return true;
		} else if(temp.next == null) {
			return false;
		}
		
		while(temp.next != null) {
			temp = temp.next;
			probeCount++;
			if(temp.word.equals(word)) {
				return true;
			}
		}
		
		return false;
	}
	
	private class WordNode{
		String word;
		private WordNode next;
		
		public WordNode(String w) {
			word = w;
		}
	}
}
