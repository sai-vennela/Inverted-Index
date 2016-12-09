import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Class that returns an array of type Files. The input is the directory location
 * and output is an array of all the text files in the directory.
 */
public class DirectoryTraverser {

	/**
	 * A recursive method to get all the text files in the given directory
	 * returns the array of files
	 * 
	 * @param filename
	 *            is the directory to traverse and get the files
	 * @param fileList
	 *            is an array list of all the text files
	 */
	private static void getFiles(Path filename, ArrayList<Path> fileList) {

		try (DirectoryStream<Path> listing = Files.newDirectoryStream(filename)) {
			for (Path file : listing) {
				if (Files.isDirectory(file)) {
					getFiles(file, fileList);
				} else if (file.toString().toLowerCase().endsWith(".txt")) {
					fileList.add(file);
				}
			}

		} catch (NotDirectoryException ex) {
			System.out.println(filename.toString() + " is not a directory");

		} catch (IOException ex) {
			System.out.println("Failed to traverse: "+ filename.toString());
		}
	}

	/**
	 * Returns an array of all the text files
	 * 
	 * @param path
	 *            is the directory path which is to be traversed
	 * @return the fileList which is an array of all the text files
	 */
	public static ArrayList<Path> getFiles(Path path) {
		ArrayList<Path> fileList = new ArrayList<Path>();
		getFiles(path, fileList);
		return fileList;
	}
}