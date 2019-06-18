package cipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.stream.Collectors;

import src.GroupProbabilityPair;

public class DetectEnglish {

	public static Hashtable<Long, String> dictionaryTable;
	public static Hashtable<Long, String> mostLikelyTable;
	public static Hashtable<Long, String> twoGramsTable;
	public static TreeMap<String, Double> firstOrder = new TreeMap<String, Double>();
	public static TreeMap<String, Double> secondOrder = new TreeMap<String, Double>();
	double[] unseenScores = new double[50];

	DetectEnglish() {
		dictionaryTable = new Hashtable<Long, String>();
		mostLikelyTable = new Hashtable<Long, String>();
		twoGramsTable = new Hashtable<Long, String>();
	}

	/**
	 * Returns what fraction of the text can be called English.
	 * 
	 * @param text The text to be analysed.
	 * @return Float representing the fraction of text that can be classified as
	 *         English. Scores above 0.75 for unspaced text and 0.85 for spaced text
	 *         are good indications of English.
	 */
	public float detectEnglish(String text) {
		text = text.toLowerCase();
		boolean spaced = false;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == ' ') {
				spaced = true;
				break;
			}
		}
		if (spaced) {
			ArrayList<String> words = new ArrayList<String>(Arrays.asList(text.replaceAll(
					"[!\\\"\\�\\$\\%\\^\\&\\*\\(\\)\\_\\'\\+\\=\\{\\}\\[\\]\\;\\:\\@\\#\\~\\|\\<\\,\\.\\>\\/]", "")
					.split(" "))); // Removal of all unwanted characters, leaving only hyphens and alphabetical
									// characters and conversion to list..
			ArrayList<String> toRemove = new ArrayList<String>();
			for (String word : words) {
				char[] letters = word.toCharArray();
				for (int i = 0; i < word.length(); i++) {
					if (letters[i] == '-') {
						toRemove.add(word); // If a word contains a '-' it needs to cleaned up because the dictionary
											// does not deal well with compounded words.
					}
				}
			}
			for (String word : toRemove) { // For all the hyphenated words...
				words.remove(word);
				String[] split = word.split("-");
				for (String part : split) { // Split them by the hyphen and add the separate parts back in.
					words.add(part);
				}
			}
			return isEnglish(words.toArray(new String[0]));

		} else {
			return isEnglish(respace(text).split(" "));
		}
	}

	/**
	 * Returns the fraction of words that are English in a given array.
	 * 
	 * @param words An array of words to be analysed for English text.
	 * @return Float representing the fraction of words in the array that are
	 *         English.
	 */
	@SuppressWarnings("unchecked")
	public float isEnglish(String[] words) {
		if (dictionaryTable.isEmpty()) {
			dictionaryTable = (Hashtable<Long, String>) Utilities.readHashTable("dictionary.htb");
		}
		float englishWords = 0f;
		for (String word : words) {
			long hashedWord = Utilities.hash64(word);
			if (dictionaryTable.containsKey(hashedWord)) {
				englishWords += 1;
			}
		}
		return englishWords / words.length;
	}

	/**
	 * Find the first match with the text using the most likely groups of words.
	 * 
	 * @param possibleGroups A list of all the possible groups of words that could
	 *                       be in the text.
	 * @param letters
	 * @return An integer array of length 2, [0] being the length of the first word
	 *         to add and [1] being the position of the space.
	 */
	public int[] firstMatch(ArrayList<String> possibleGroups, ArrayList<Character> letters) {
		for (String pair : possibleGroups) {
			char[] characters = pair.replaceAll(" ", "").toCharArray();
			boolean match = true;
			for (int i = 0; i < characters.length && i < letters.size(); i++) {
				if (characters[i] != letters.get(i)) {
					match = false;
					break;
				}
			}
			if (match) {
				int a = pair.length() - 1;
				int b = pair.indexOf(" ");
				return new int[] { a, b };
			}
		}
		return new int[2];
	}

	/**
	 * Ranks all of the possible word pairs generated by the analysis of the text.
	 * 
	 * @param pair           The GroupProbabilityPair object to be inserted into the
	 *                       list into probability order.
	 * @param possibleGroups The list of currently sorted probability groups.
	 * @return The updated list of possible word groups.
	 */
	public ArrayList<GroupProbabilityPair> rank(GroupProbabilityPair pair,
			ArrayList<GroupProbabilityPair> possibleGroups) {
		for (int i = 0; i < possibleGroups.size(); i++) { // Iterates through the list of possible word groups...
			if (pair.getRank() < possibleGroups.get(i).getRank()) {// When the the chance of getting the group being
																	// compared is higher than that in the position in
																	// the list...
				possibleGroups.add(i, pair); // Insert the group into that position.
				return possibleGroups;
			}
		}
		possibleGroups.add(pair); // If it's not more likely than any other existing group, add it to the end of
									// the list.
		return possibleGroups;
	}

	/**
	 * Finds the English words present in the first characters of a string.
	 * 
	 * @param letters An ArrayList of all letters in a given text.
	 * @return An array containing of size 2 with all words found at [0] and
	 *         probable words stored at [1].
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String>[] findWords(ArrayList<Character> letters) {
		if (dictionaryTable.isEmpty()) {
			dictionaryTable = (Hashtable<Long, String>) Utilities.readHashTable("dictionary.htb");
		}
		if (mostLikelyTable.isEmpty()) {
			mostLikelyTable = (Hashtable<Long, String>) Utilities.readHashTable("mostProbable.htb");
		}
		ArrayList<String> foundWords = new ArrayList<String>();
		ArrayList<String> probableWords = new ArrayList<String>();
		StringBuilder word = new StringBuilder();
		int firstLength = 0;
		for (int i = 0; i < letters.size(); i++) { // Detecting the first word.
			word.append(letters.get(i));
			if (dictionaryTable.containsKey(Utilities.hash64(word.toString()))) {
				foundWords.add(word.toString()); // If the group of characters has a corresponding hash in the
													// dictionary, we've found a word.
				if (mostLikelyTable.containsKey(Utilities.hash64(word.toString()))) {
					probableWords.add(word.toString()); // If it also has a hash in the most likely words hashtable,
														// we've found a likely word.
				}
				firstLength = i; // This allows us to speed up our looking for words, as we can ignore letters
									// before this number, and we can avoid looking at words that are too long.
				break;
			}
		}
		for (int i = firstLength + 1; i < 20 && i < letters.size(); i++) { // Avoid ArrayIndexOutOfBoundsErrors and
																			// ignore
																			// words that are longer than the longest
																			// common word in
																			// English.
			word.append(letters.get(i));
			if (dictionaryTable.containsKey(Utilities.hash64(word.toString()))) {
				foundWords.add(word.toString());
				if (mostLikelyTable.containsKey(Utilities.hash64(word.toString()))) {
					probableWords.add(word.toString());
				}
			}
		}
		if (foundWords.size() > 0 && !probableWords.contains(foundWords.get(foundWords.size() - 1))) {
			// This introduces the longest found word into probable words, even if it isn't
			// particularly likely on its own. This is to try and mitigate over-prediction
			// of short words.
			probableWords.add(foundWords.get(foundWords.size() - 1));
		}
		ArrayList<String>[] out = new ArrayList[2];
		out[0] = foundWords;
		out[1] = probableWords;
		return out;
	}

	/**
	 * Performs a first-pass respacing of an unspaced string based on an estimate of
	 * english.
	 * 
	 * @param text
	 * @return String representing the best guess as to the actual words in the
	 *         text.
	 */
	@SuppressWarnings("unchecked")
	public String respace(String text) {
		if (twoGramsTable.isEmpty()) {
			twoGramsTable = (Hashtable<Long, String>) Utilities.readHashTable("2grams.htb");
		}
		ArrayList<Character> letters = (ArrayList<Character>) text.toLowerCase().replaceAll("[^a-zA-Z ]", "").chars()
				.mapToObj(e -> (char) e).collect(Collectors.toList());
		StringBuilder out = new StringBuilder();
		boolean changes = false;
		while (letters.size() > 0) {
			ArrayList<String>[] words = findWords(letters);
			ArrayList<String> foundWords = words[0];
			ArrayList<String> probableWords = words[1];
			ArrayList<String> possibleGroups = new ArrayList<String>();
			for (String word : probableWords) {
				StringBuilder succeeding = new StringBuilder();
				int length = word.length();
				switch (length) { // More cases can be added if I find more refined patterns in word lengths.
				case 1:
					for (int i = 0; i < 2 && i + length < letters.size(); i++) {
						succeeding.append(letters.get(length + i));
					}
					break;
				default:
					if (length < letters.size()) {
						succeeding.append(letters.get(length));
					}
					break;
				}
				String toCheck = word + "," + succeeding.toString();
				String[] lines = Utilities.readFile("2grams.txt"); // Goes through all likely two word combinations and
				for (String line : lines) {
					if (line.startsWith(toCheck)) {
						possibleGroups.add(line.replaceAll(",", " "));
						break;
					}
				}

			}
			int[] toRemove = firstMatch(possibleGroups, letters); // Check if this equals 0, then examine words in
																	// foundWords.
			if (toRemove[0] > 0) {
				if (changes == false && out.length() > 0)
					out.append(" ");
				for (int i = 0; i < toRemove[0] && letters.size() > 0; i++) {
					out.append(letters.get(0));
					if (i == toRemove[1] - 1) {
						out.append(" ");
					}
					letters.remove(0);
				}
				out.append(" ");
				changes = true;
			} else {
				// Examine found words and select the most likely.
				String wordToRemove = "";
				boolean found = false;
				for (String word : foundWords) {
					if (mostLikelyTable.containsKey(Utilities.hash64(word))) {
						wordToRemove = word;
						found = true;
						break;
					}
					if (found)
						break;
				}
				if (wordToRemove == "") {
					out.append(letters.get(0));
					letters.remove(0);
					changes = false;
				} else {
					for (int i = 0; i < wordToRemove.length(); i++) {
						out.append(letters.get(0));
						letters.remove(0);
					}
					out.append(" ");
					changes = true;
				}
			}
		}
		return out.toString();
	}

	public void initialise() {
		String[] lines = Utilities.readFile("1w.txt");
		double N = 1024908267229d;
		for (String line : lines) {
			String[] splitLine = line.split(",");
			double valueToInsert = Math.log10(Double.valueOf(splitLine[1]) / N);
			firstOrder.put(splitLine[0], valueToInsert);
		}
		lines = Utilities.readFile("2w.txt");
		for (String line : lines) {
			String[] splitLine = line.split(",");
			double valueToInsert;
			if (!firstOrder.containsKey(splitLine[0])) {
				valueToInsert = Math.log10(Double.parseDouble(splitLine[2]) / N);
				secondOrder.put(splitLine[0] + " " + splitLine[1], valueToInsert);
			} else {
				valueToInsert = Math.log10(Double.parseDouble(splitLine[2]) / N) - firstOrder.get(splitLine[0]);
				secondOrder.put(splitLine[0] + " " + splitLine[1], valueToInsert);
			}
		}
		
		for (int i = 0; i < 50; i++) {
			unseenScores[i] = Math.log10(10 / (N * Math.pow(10, i)));
		}
	}

	public double conditionalWordProbability(String word, String prev) {
		if(prev == null) {
			prev = "<UNK>";
		}
		if(!firstOrder.containsKey(word)) {
			return unseenScores[word.length()];
		} else if (!secondOrder.containsKey(prev + " " + word)){
			return firstOrder.get(word);
		} else {
			return secondOrder.get(prev + " " + word);
		}
	}

	public String score(String text, int maxWordLength) { //TODO this still doesn't actually work.
		initialise();
		char[] letters = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().toCharArray();
		double[][] probabilities = new double[maxWordLength][letters.length];
		String[][] strings = new String[maxWordLength][letters.length];
		for (int r = 0; r < maxWordLength; r++) {
			for (int c = 0; c < letters.length; c++) {
				probabilities[r][c] = -99e99;
				strings[r][c] = "";
			}
		}
		for(int j = 0; j < maxWordLength; j++) {
			StringBuilder graph = new StringBuilder();
			for(int x = 0; x < j + 1; x++) {
				graph.append(letters[x]);
			}
			String word = graph.toString();
			probabilities[0][j] = conditionalWordProbability(word, null);
			strings[0][j] = word; 
		}
		for(int i = 1; i < maxWordLength; i++) {
			for(int j = 0; j < letters.length ; j++) {
				if(i + j + 1 > letters.length) break;
				int max;
				if(i < maxWordLength) {
					max = i;
				} else {
					max = maxWordLength;
				}
				TreeMap<Double, String> candidates = new TreeMap<Double, String>();
				for(int k = 0; k < max; k++) {
					StringBuilder word = new StringBuilder();
					for(int a = i; a < i + j + 1; a++) {
						word.append(letters[a]);
					}
					double score = probabilities[i - k - 1][k] + conditionalWordProbability(word.toString(), lastLetter(strings[i - k - 1][k]));
					String toInsert = strings[i - k - 1][k] + word;
					candidates.put(score, toInsert);
				}
				probabilities[i][j] = candidates.lastKey();
				strings[i][j] = candidates.get(candidates.lastKey());
			}
		}
		for (int r = 0; r < maxWordLength; r++) {
			for (int c = 0; c < letters.length; c++) {
				System.out.print("Probability: " + probabilities[r][c] + " ");
				System.out.print("String: " + strings[r][c] + "\n");
			}
		}
		return "";
	}
	
	public String lastLetter (String in) {
		if(in.length() == 0) {
			return "";
		} else {
			char[] letters = in.toCharArray();
			return Character.toString(letters[in.length() - 1]);
		}
	}
	// Note: I would clean up the text, but respacing it is already computationally
	// expensive as it is.
}
