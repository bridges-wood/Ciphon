import java.util.TreeMap;

public class NGramAnalyser {

	/**
	 * Creates a map of ngrams in target text.
	 * 
	 * @param n
	 *            Integer length of the ngrams to be found in the text.
	 * @param text
	 *            String to be analysed.
	 * @param isSpaced
	 *            Boolean whether or not the text contains spaces already.
	 * @return TreeMap of all ngrams and fraction of text they represent.
	 */
	public static TreeMap<String, Float> NgramAnalysis(int n, String text, boolean isSpaced) {
		TreeMap<String, Float> ngrams = new TreeMap<String, Float>();
		// Breakdown by words or not (this is not neccessary as the code works on
		// unspaced inputs equally well)
		String[] words = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+"); // Takes every word in text and
																						// removes all punctuation
																						// before adding it to the
																						// array.
		for (String word : words) {
			String temp = word;
			word = " " + temp + " "; // Adds spaces to the end of every word (Allows for word end and start
									 // analysis).

			if (n <= word.length()) {
				char[] chars = word.toCharArray();
				// Generate ngrams for each word.
				for (int i = 0; i < (chars.length - n) + 1; i++) {
					StringBuilder ngram = new StringBuilder();
					for (int j = 0; j < n; j++) {
						ngram.append(chars[i + j]);
					}
					// Add ngrams to tree.
					String finalNgram = ngram.toString();
					if (ngrams.containsKey(finalNgram)) {
						ngrams.put(finalNgram, ngrams.get(finalNgram) + 1);
					} else {
						ngrams.put(finalNgram, 1f);
					}
				}
			}
		}
		if (!isSpaced) {
			ngrams.remove(" " + text.substring(0, n - 1));
			ngrams.remove(text.substring(text.length() - (n - 1)) + " ");
		}
		if (n == 1) {
			ngrams.remove(" ");
		}
		// Set float to fraction of the total the n gram represents.
		float total = 0f;
		for (float f : ngrams.values()) {
			total += f;
		} // Collect total number of ngrams in text.
		for (String key : ngrams.keySet()) {
			ngrams.put(key, ngrams.get(key) / total);
		}
		// Test is to be on past cipher challenge answers to analyse how the author of
		// the puzzles writes.
		return ngrams;
	}

}
