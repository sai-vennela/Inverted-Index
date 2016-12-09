 import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Takes the arguments, traverses the directory gets the files list to parse
 * each file and populates the Inverted Index
 */
public class Driver {
	/**
	 * Main method takes the arguments, traverses the directory gets the files
	 * list to parse each file and populates the Inverted Index
	 * 
	 * @param args
	 *            -d represents a directory and next argument has the path -i
	 *            represents the output and next argument has the output file
	 *            name -q represents query file and next argument has the query
	 *            file name -r represents the search output and next argument
	 *            has the output file name
	 * @throws IOException 
	 */
	public static void main(String[] args) {

		ArgumentParser argumentParser = new ArgumentParser(args);
		String output, search;
		InvertedIndex iIndex;
		PartialSearch partialSearch;
		Path queryPath = null;

		if (!argumentParser.hasFlag("-d")) {
			System.out.println("No directory argument found.");
			return;
		}

		if (!ArgumentParser.isValue(argumentParser.getValue("-d"))) {
			System.out.println("No input file given");
			return;
		}
		// Converts argument to Path object
		Path directory = Paths.get(argumentParser.getValue("-d"));

		if (!Files.isDirectory(directory)) {
			System.out.println("Not a vaild directory");
			return;
		}

		// Traverses the directory to get all the text files
		ArrayList<Path> fileList = DirectoryTraverser.getFiles(directory);

		iIndex = new InvertedIndex();

		// Parsing each file to populate InvertedIndex
		for (Path file : fileList) {
			System.out.println("Parsing file:" + file.toString());
			IndexBuilder.parseFile(file, iIndex);
		}

		if (argumentParser.hasFlag("-i")) {
			if (argumentParser.getValue("-i") != null) {
				output = argumentParser.getValue("-i");
			} else {
				output = "invertedindex.txt";
			}
		} else {
			output = "invertedindex.txt";
		}

		// Writes the inverted index to file

		iIndex.writeIndex(Paths.get(output));

		// Starts QueryParser when -q has the query file path
		if (!argumentParser.hasFlag("-q")) {
			System.out
					.println("No query file is given, hence no search results");
			return;
		} else {

			queryPath = Paths.get(argumentParser.getValue("-q"));

		}
		
		// Build partial search
		partialSearch = new PartialSearch();
		partialSearch.buildPartialSearch(queryPath, iIndex);
		
		// Gets the output file name
		if (argumentParser.hasFlag("-r")) {
			if (argumentParser.getValue("-r") != null) {
				search = argumentParser.getValue("-r");
			} else {
				search = "searchresults.txt";
			}
		} else {
			search = "searchresults.txt";
		}

		
		// Saves the search output to the destination file
		partialSearch.writeSearch(Paths.get(search));
	}
}