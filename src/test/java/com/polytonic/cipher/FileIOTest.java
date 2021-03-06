package com.polytonic.cipher;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.TreeMap;

import org.junit.Test;

public class FileIOTest {

	private FileIO tester = new FileIO();

	@Test
	public void testReadFile() {
		String[] expected = { "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth",
				"tenth" };
		assertArrayEquals(expected, tester.readFile("src/test/resources/readTest.txt"));
	}

	@Test
	public void testHash64() {
		assertEquals(-439409999022904539L, tester.hash64("test"));
	}

	@Test
	public void testGenerateHashTable() {
		tester.generateHashMap("src/test/resources/readTest.txt", "src/test/resources/hashTable.htb");
	}

	@Test
	public void testReadHashTable() {
		tester.generateHashMap("src/test/resources/readTest.txt", "src/test/resources/hashTable.htb");
		String[] expected = { "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth",
				"tenth" };
		Hashtable<Long, String> table = new Hashtable<Long, String>();
		for (String line : expected) {
			table.put(tester.hash64(line), line);
		}
		assertEquals(table.entrySet(), tester.readHashTable("src/test/resources/hashTable.htb").entrySet());
	}

	@Test
	public void testCleanText() {
		assertEquals("amaqmjzmj", tester.cleanText("A򽚛ݚM둑aQ˛򖰭ݨ1⹦Mj󿅳ɿ뻜z晗񞠗紡�?�Ӏm(J⃃!럑󣾜庴"));
	}

	@Test
	public void testDeSpace() {
		assertEquals("", tester.deSpace(" 	"));
	}

	@Test
	public void testGenerateLetterFrequencies() {
		tester.generateLetterFrequencies("src/test/resources/frequencies.txt", "src/test/resources/frequencies.tmp");
	}

	@Test
	public void testReadLetterFrequencies() {
		TreeMap<Character, Double> frequencies = new TreeMap<Character, Double>();
		frequencies.put('e', 0.227d);
		assertEquals(frequencies.entrySet(),
				tester.readLetterFrequencies("src/test/resources/frequencies.tmp").entrySet());
	}

	@Test
	public void testLoadNgramMap() {
		TreeMap<String, Double> probabilities = tester.loadNgramMap(tester.TRIGRAM_LOG_MAP_PATH);
		assertTrue(probabilities.keySet().size() == 17556);
		assertEquals(77534223d, Math.pow(10, probabilities.get("the")) * 4274127909d, 0.077534223);
		// Tests that it can resolve frequencies to order E-9 accuracy by
		// performing the inverse of the done operation.
		assertEquals(7610, Math.pow(10, probabilities.get("gng")) * 4274127909d, 0.00000761);
	}

}
