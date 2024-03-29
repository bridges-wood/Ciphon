package com.polytonic.cipher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 * A class that handles the management of the decryption of the string passed to
 * the server by each client. It is designed to be lightweight and pass on the
 * heavy lifting of decryption to {@link com.polytonic.cipher.CipherBreakers}.
 * 
 * @author Max Wood
 *
 */
public class Manager {

	private FileIO u;
	private ProbableSubstitutions p;
	private IOC i;
	private NGramAnalyser n;
	private KasiskiExamination k;
	private CipherBreakers c;

	private String result = "";
	private String text = "";
	private boolean fail = false;

	public Manager(String text, boolean test, boolean debug) {
		this.text = text;
		this.u = new FileIO();
		this.p = new ProbableSubstitutions();
		this.n = new NGramAnalyser(u);
		this.i = new IOC(n);
		Vigenere v = new Vigenere();
		DetectEnglish d = new DetectEnglish(u, n);
		this.k = new KasiskiExamination(u, n, v, i, d);
		this.c = new CipherBreakers(u, k, p);
		try {
			if (!test) {
				run(u.cleanText(text), debug);
			}
		} catch (Exception e) {
			fail = true;
			result = e.getMessage();
		}
	}

	private void run(String text, boolean debug) {
		switch (detectCipher(text)) {
		case "Periodic":
			result = c.vigenereBreaker(u.deSpace(text), debug);
			if (result.startsWith("Error"))
				fail = true;
			break;
		case "Substitution":
			// result = c.substitutionBreaker(text);
			result = "Error 3: Substitution cipher not yet implemented.";
			fail = true;
			break;
		case "":
			result = "Error 2: Detection failed. Cipher not recognised.";
			fail = true;
			break;
		}
	}

	/**
	 * Detects whether a given piece of text is encoded with a periodic cipher, by
	 * identifying peaks in the indices of coincidence.
	 * 
	 * @param text The text to be analysed.
	 * @return A boolean representing whether the text is encoded periodically.
	 */
	public boolean detectPeriodic(String text) {
		text = u.deSpace(text);
		double[] IOCs = new double[19];
		for (int n = 2; n <= 20; n++) {
			IOCs[n - 2] = i.periodIndexOfCoincidence(n, text);
		}
		double sd = new StandardDeviation().evaluate(IOCs);
		double mean = new Mean().evaluate(IOCs);
		List<Integer> suggestedKeyLengths = new ArrayList<Integer>();
		for (int n = 2; n <= 20; n++) {
			double score = IOCs[n - 2];
			if (score >= mean + sd)
				suggestedKeyLengths.add(n);
			/*
			 * If the text is encrypted with a periodic cipher, peaks should be identified
			 * in the indices of coincidence.
			 */
		}
		Map<Integer, Integer> factorsMap = new TreeMap<Integer, Integer>(); // These peaks should also be identified at
																			// intervals that are multiples of one
																			// factor.
		for (int i : suggestedKeyLengths) {
			factorsMap = k.factorise(factorsMap, i);
		}
		if (factorsMap.get(k.maxKeyInt(factorsMap)) >= suggestedKeyLengths.size() - 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Detects whether a given piece of text has been encoded with a substitution
	 * cipher by calculating the Bhattacharya coefficient of the letter distribution
	 * of the text and normal English.
	 * 
	 * @param text The text to be analysed.
	 * @return A boolean representing whether the text is encoded using a
	 *         substitution cipher.
	 */
	public boolean detectSubstitution(String text) {
		TreeMap<Character, Double> expectedLetterFrequencies = u.readLetterFrequencies(u.LETTER_FREQUENCIES_MAP_PATH);
		ArrayList<Double> expected = new ArrayList<Double>(expectedLetterFrequencies.values());
		Collections.sort(expected);
		TreeMap<String, Double> observeredLetterFrequencies = n.NgramAnalysis(1, text, false);
		ArrayList<Double> observed = new ArrayList<Double>(observeredLetterFrequencies.values());
		Collections.sort(observed);
		double bc = 0;
		for (int i = 0; i < 25; i++) {
			bc += Math.sqrt(expected.get(i) * observed.get(i));
		}
		bc = -Math.log(bc);
		if (Math.abs(bc) < 0.25) {
			return true;
		} else {
			return false;
		}
	}

	public String detectCipher(String text) {
		if (detectPeriodic(text)) {
			return "Periodic";
		} else if (detectSubstitution(text)) {
			return "Substitution";
		} else {
			return "";
		}
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isFail() {
		return fail;
	}

	public void setFail(boolean fail) {
		this.fail = fail;
	}

}
