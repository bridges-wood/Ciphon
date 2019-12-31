package cipherTest;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cipher.Mapping;
import cipher.MultiLemmaAnalysis;
import cipher.PredictWords;
import cipher.Substitution;
import cipher.Utilities;

public class MultiLemmaAnalysisTest {

	private Utilities u = new Utilities();
	private PredictWords p = new PredictWords(u);
	private MultiLemmaAnalysis tester = new MultiLemmaAnalysis(u, p);
	private Substitution s = new Substitution();
	private final Mapping[] MAPPINGS = SubstitutionTest.initialiseMappings("zyxwvutsrqponmlkjihgfedcba");

	@Test
	public void testPossibleLemmata() {
		String[] cases = { "that the", "this is", "could be", "this is an", "how could this", "this is the result of" };
		// Test cases can be added as needed.
		for (String testCase : cases) {
			String[] possibleLemmata = tester.possibleLemmata(s.encrypt(testCase, MAPPINGS));
			List<String> list = Arrays.asList(possibleLemmata);
			assertTrue(list.contains(testCase));
		}
	}

}
