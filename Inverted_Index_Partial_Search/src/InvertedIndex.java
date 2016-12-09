import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;

/**
 * Inverted Index class is a database that stores the word, the file it contains
 * and the position of the word.
*/
public class InvertedIndex {

	private static final Charset cs = Charset.forName("UTF-8");
	
	private final TreeMap<String, TreeMap<String, ArrayList<Integer>>> invertedIndex;

	public InvertedIndex() {
		invertedIndex = new TreeMap<>();
	}
	
	/**
	 * Method to check if the word is in the InvertedIndex
	 * 
	 * @param word
	 *            is the word alphabets or digits or both
	 * @return <code>true</code> if word is present in the Inverted Index
	 */
	public boolean hasWord(String word) {

		return invertedIndex.get(word) != null;
	}

	/**
	 * Method to check if the word has filename in the InvertedIndex
	 * 
	 * @param word
	 *            is either alphabets or digits or both
	 * @param filename
	 *            is the text file where the word is present
	 * @return <code>true</code> if the Inverted Index has the filename
	 */
	public boolean hasFile(String word, String filename) {

		return invertedIndex.get(word).get(filename) != null;
	}

	/**
	 * Adds a new word to the inverted Index
	 * 
	 * @param word
	 *            is the word either alphabets or digits or both
	 * @param filename
	 *            is the text file where the word is present
	 * @param position
	 *            is the position of the word in the given file
	 */
	public void addWord(String word, String filename, Integer position) {

		if (!this.hasWord(word)) {
			invertedIndex.put(word, new TreeMap<String, ArrayList<Integer>>());
			this.addFile(word, filename, position);

		} else {
			this.addFile(word, filename, position);
		}
	}

	/**
	 * Adds a new file for the new word
	 * 
	 * @param word
	 *            is either alphabets or digits or both
	 * @param filename
	 *            is the text file whose word is present
	 * @param position
	 *            is the position of the word in the given file
	 */
	private void addFile(String word, String filename, Integer position) {

		if (!this.hasFile(word, filename)) {
			invertedIndex.get(word).put(filename, new ArrayList<Integer>());
			this.addCount(word, filename, position);

		} else {
			this.addCount(word, filename, position);
		}
	}

	/**
	 * Adds the word count to inverted index
	 * 
	 * @param word
	 *            is either alphabets or digits or both
	 * @param filename
	 *            is the text file whose word is present
	 * @param position
	 *            is the position of the word in the given file
	 */
	private void addCount(String word, String filename, Integer position) {

		invertedIndex.get(word).get(filename).add(position);
	}

	/**
	 * Writes the InvertedIndex to output file
	 * 
	 * @param path
	 *            is the path for the text file to save the output
	 */
	public void writeIndex(Path path) {

		try (PrintWriter out = new PrintWriter(
				Files.newBufferedWriter(path, cs), true)) {

			for (String key : invertedIndex.keySet()) {
				String buffer = String.format("%s", key);
				out.println(buffer);

				for (String filename : invertedIndex.get(key).keySet()) {
					buffer = String.format("\"%s\"", filename.toString());

					for (Integer positions : invertedIndex.get(key).get(filename)) {
						buffer = buffer + String.format(", %d", positions);
					}
					out.print(buffer);
					out.println();
				}
				out.println();
			}
			
		} catch (IOException e) {
			System.out.println("Unable to open the output file");
		}
	}
	
	/**
	 * Traverses the inverted index and collects all the words which are
	 * greater than the query word. Updates the search Tree with the
	 * SearchBuilder object 
	 * @param words
	 * @return
	 */
	public ArrayList<SearchResult> search(String[] words) {
		
		TreeMap<String, SearchResult> searchTree = new TreeMap<String, SearchResult>();
		for (String query : words) {
			
			Set<String> partialWord = invertedIndex.tailMap(query).keySet();
			
			for (String key : partialWord) {
				//breaks if the word doesn't start with the query word
				//this is useful for efficiency
				if (!key.startsWith(query)) {
					break;
				}
				
				//For each file the frequency and first positions are updated to the searchTree
				for (String path : invertedIndex.get(key).keySet()) {
					ArrayList<Integer> positions = invertedIndex.get(key).get(path);
					 
					if (!searchTree.containsKey(path)) {
						searchTree.put(path, new SearchResult(path, positions.size(), positions.get(0)));

					} else {
						SearchResult result = searchTree.get(path);
						result.addFrequency(positions.size());
						result.setFirst(positions.get(0));
					}
				}
			}
		}
		//New array list is to sort the results
		ArrayList<SearchResult> searchList = new ArrayList<>();
		searchList.addAll(searchTree.values());
		Collections.sort(searchList);
		return searchList;
	}

}