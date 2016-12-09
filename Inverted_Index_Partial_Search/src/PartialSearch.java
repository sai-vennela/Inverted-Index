import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * PartialSearch performs the search operations to update the SearchBuilder
 * objects corresponding to the word
*/
public class PartialSearch {

	private static final Charset cs = Charset.forName("UTF-8");
	private final LinkedHashMap<String, ArrayList<SearchResult>> partialSearch;

	public PartialSearch() {
		partialSearch = new LinkedHashMap<String, ArrayList<SearchResult>>();
	}

	/**
	 * Parses the query file to words and updates partial search hash map
	 * 
	 * @param query
	 *            is the path of the query file
	 * @param index
	 *            is the inverted index
	 */
	public void buildPartialSearch(Path query, InvertedIndex index) {

		String[] queryWord;

		try {

			for (String queryLine : Files.readAllLines(query, cs)) {
				queryWord = queryLine.split("\\s");
				partialSearch.put(queryLine, index.search(queryWord));
			}

		} catch (IOException e) {
			System.out.println("Unable to read " + query.toString());
		}
	}

	/**
	 * Writes the output of the search results to the given file
	 * 
	 * @param path
	 *            is the desired output file path
	 */
	public void writeSearch(Path path) {

		try (PrintWriter out = new PrintWriter(
				Files.newBufferedWriter(path, cs), true)) {

			for (String queryLine : partialSearch.keySet()) {
				// Custom way to write the output to file
				out.print(queryLine);
				out.println();
				ArrayList<SearchResult> search = partialSearch.get(queryLine);

				if (!search.isEmpty()) {
					for (SearchResult element : search) {
						out.print(element);
					}
				}
				out.println();
			}
		} catch (IOException e) {
			System.out.println("Unable to open " + path.toString());
		}
	}
}