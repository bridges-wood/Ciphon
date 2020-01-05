package cipher;

import java.util.Arrays;
import java.util.Random;

import cipher.Mapping.MappingPair;

public class CipherBreakers {

	private Utilities u;
	private DetectEnglish d;
	private KasiskiExamination k;
	private Vigenere v;
	private ProbableSubstitutions p;
	private NGramAnalyser n;
	private Substitution s;

	public CipherBreakers(Utilities u, KasiskiExamination k, DetectEnglish d, ProbableSubstitutions p,
			NGramAnalyser n) {
		this.d = d;
		this.u = u;
		this.k = k;
		this.n = n;
		this.p = new ProbableSubstitutions();
		this.v = new Vigenere();
		this.s = new Substitution();
	}

	public String vigenereBreaker(String text) {
		String[] keys = k.vigenereKeys(text);
		double maxScore = Double.NEGATIVE_INFINITY;
		String fittestKey = "";
		for (String key : keys) {
			double score = u.deSpace(d.graphicalRespace(v.decrypt(text, key), 20)).length() / text.length();
			if (score > maxScore) {
				maxScore = score;
				fittestKey = key;
			}
		}
		return d.graphicalRespace(v.decrypt(text, fittestKey), 20);
	}

	public String substitutionBreaker(String text) {
		//TODO change this.
		Mapping[] mappings = p.probableSubstitutionGenerator(n.NgramAnalysis(1, text, false));
		Mapping m = new Mapping();
		int counter = 0;
		Random rng = new Random();
		while (counter <= 1000000) {
			System.out.println(s.decrypt(text, mappings));
			double score = u.deSpace(d.graphicalRespace(s.decrypt(text, mappings), 20)).length() / text.length();
			int a = rng.nextInt(26);
			int b = rng.nextInt(26);
			MappingPair pair = m.swap(mappings[a], mappings[b]);
			Mapping[] child = Arrays.stream(mappings).map(mapping -> mapping == null ? null : new Mapping()).toArray(Mapping[] :: new);
			child[a] = pair.getA();
			child[b] = pair.getB();
			if(u.deSpace(d.graphicalRespace(s.decrypt(text, child), 20)).length() / text.length() > score) {
				counter = 0;
				mappings = child;
			} else {
				counter += 1;
			}
		}
		return d.graphicalRespace(s.decrypt(text, mappings), 20);
	}

}
