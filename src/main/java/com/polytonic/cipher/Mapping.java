package com.polytonic.cipher;

/**
 * A class to store fractions of a Simple Substitution Cipher key in a
 * manageable way.
 * 
 * @author Max Wood
 * @see Substitution
 */
public class Mapping {

	private char cipherChar;
	private char plainChar;

	public Mapping() {

	}

	/**
	 * @param fromChar The enciphered character.
	 * @param toChar   The deciphered character the enciphered character represents.
	 */
	public Mapping(char fromChar, char toChar) {
		this.cipherChar = fromChar;
		this.plainChar = toChar;
	}

	public char getCipherChar() {
		return cipherChar;
	}

	public void setCipherChar(char cipherChar) {
		this.cipherChar = cipherChar;
	}

	public char getPlainChar() {
		return plainChar;
	}

	public void setPlainChar(char plainChar) {
		this.plainChar = plainChar;
	}

	/**
	 * Converts each mapping to a string. If mapping is A -> B, out is AB.
	 */
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append(cipherChar);
		out.append(plainChar);
		return out.toString();
	}

	/**
	 * Swaps the plain char encodings of mappings a and b.
	 * 
	 * @return MappingPair containing mappings a and b.
	 */
	public MappingPair swap(Mapping a, Mapping b) {
		char temp = b.getPlainChar();
		b.setPlainChar(a.getPlainChar());
		a.setPlainChar(temp);
		return new MappingPair(a, b);
	}

	public class MappingPair {
		private Mapping a;
		private Mapping b;

		MappingPair(Mapping a, Mapping b) {
			this.a = a;
			this.b = b;
		}

		public Mapping getA() {
			return a;
		}

		public void setA(Mapping a) {
			this.a = a;
		}

		public Mapping getB() {
			return b;
		}

		public void setB(Mapping b) {
			this.b = b;
		}
	}
}