package cipher;

public class Mapping {

	private boolean isDefinite;
	private char cipherChar;
	private char plainChar;

	public Mapping() {
		
	}
	
	/**
	 * @param isDefinite Whether or not the relationship is definite in the text.
	 * @param fromChar   The enciphered character.
	 * @param toChar     The deciphered character the enciphered character
	 *                   represents.
	 */
	public Mapping(char fromChar, char toChar) {
		this.isDefinite = false;
		this.cipherChar = fromChar;
		this.plainChar = toChar;
	}

	public boolean isDefinite() {
		return isDefinite;
	}

	public void setDefinite(boolean isDefinite) {
		this.isDefinite = isDefinite;
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
	 * Swaps the plain char encodings of mappings a and b.
	 * 
	 * @return MappingPair containing mappings a and b.
	 */
	public MappingPair swap(Mapping a, Mapping b) {
		char temp = b.getCipherChar();
		b.setCipherChar(a.getCipherChar());
		a.setCipherChar(temp);
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