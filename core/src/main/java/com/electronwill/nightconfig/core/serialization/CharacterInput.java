package com.electronwill.nightconfig.core.serialization;

/**
 * @author TheElectronWill
 */
public interface CharacterInput {
	int read();

	default int readAndSkip(char[] toSkip) {
		int c;
		do {
			c = read();
		} while (c != -1 && Utils.arrayContains(toSkip, (char)c));
		return c;
	}

	char[] read(int n);

	char readChar();

	default char readCharAndSkip(char[] toSkip) {
		char c;
		do {
			c = readChar();
		} while (Utils.arrayContains(toSkip, c));
		return c;
	}

	char[] readChars(int n);

	CharsWrapper readCharUntil(char[] stop);
}
