/**
 * Stores path, frequency and first occurrence this also
 * implements Comparator
 */
public class SearchResult implements Comparable<SearchResult> {
	
	private final String path;
	private int frequency;
	private int firstOccurance;

	public SearchResult(String path, int frequency, int first) {
		this.path = path;
		this.frequency = frequency;
		this.firstOccurance = first;
	}

	/**
	 * Returns the path.
	 * 
	 * @return path of this object
	 */
	public String getPath() {
		return path;
	}

	/**
	 * getFrequency returns the number of occurrences
	 * 
	 * @return frequency
	 */
	public int getFrequency() {
		return frequency;
	}
	
	/**
	 * Adds frequency to current frequency
	 * @param frequency
	 */
	public void addFrequency(int frequency){
		this.frequency += frequency;
	}

	/**
	 * getFirst returns the position of first occurrence the word
	 * 
	 * @return position of first occurrence
	 */
	public int getFirst() {
		return firstOccurance;
	}

	/**
	 * setFirst sets the position of first occurrence of the word
	 * 
	 * @param first
	 */
	public void setFirst(int first) {

		if (first < this.firstOccurance)
			this.firstOccurance = first;
	}
	
	/**
	 * Helper method that customizes the 
	 * Path, frequency and first occurrence of the word
	 * to a single string
	 * @return customized string
	 */
	
	
	@Override
	public String toString(){
		String buffer;
		buffer = String.format("\"%s\"", path.toString());
		buffer = buffer + String.format(", %d, %d",frequency, firstOccurance);
		buffer = buffer + "\n";
		return buffer;		
	}
	
    /**
     * Overrides compareTo method.
     * Checks at each level in the order of frequency, first place and path.
     */
	@Override
	public int compareTo(SearchResult fObject) {
		if (frequency != fObject.frequency) {
			return Integer.compare(fObject.frequency, frequency);
		}

		if (firstOccurance != fObject.firstOccurance) {
			return Integer.compare(firstOccurance, fObject.firstOccurance);
		}

		if (!path.equalsIgnoreCase(fObject.path)) {
			return String.CASE_INSENSITIVE_ORDER.compare(path.toString(),
					fObject.path.toString());
		}
		return 0;
	}
}