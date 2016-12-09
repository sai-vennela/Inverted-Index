 import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Takes the path of the text file and Inverted Index object parses
 * each word and adds to Inverted Index
*/
public class IndexBuilder { 

	/**
	 * Class that updates the InvertedIndex with the words of text file from
	 * given path
	 * 
	 * @param path
	 *            is the path of the text file that needs to be parsed
	 * @param invertedIndex
	 *            is the InvertedIndex
	 */
	public static void parseFile(Path path, InvertedIndex invertedIndex) {

		try (BufferedReader reader = Files.newBufferedReader(path,
				Charset.forName("UTF-8"))) {

			int position = 0;
			String line;

			while ((line = reader.readLine()) != null) {
				String[] words = line.split("\\s");

				for (String word : words) {
					word = word.trim().toLowerCase();
					word = word.replaceAll("\\W", "");
					word = word.replaceAll("_", "");

					if (!word.isEmpty()) {
						position++;
						invertedIndex.addWord(word, path.toString(), position);
					}
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println(path.toAbsolutePath().toString()
					+ " is not found");
		} catch (IOException ex) {
			System.out.println("Failed to read the file: "
					+ path.toAbsolutePath().toString());
		}
	}
}