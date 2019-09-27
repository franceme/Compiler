/**
 *
 */
package com.middleend.scanner;

import lombok.Getter;
import com.frontend.overhead.arguments;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.nio.charset.Charset;

/**
 * <p>funnel class.</p>
 *
 * @author franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 *
 * This method is the read class, extending from the PushbackReader, it scrolls through the input file.
 */
public class reader extends PushbackReader {

	//region ClassFields
	private Integer colNum;
	@Getter
	private Integer lineNum;
	private Integer kurkar;
	@SuppressWarnings ("unused")
	private arguments args;
	//endregion

	/**
	 * <p>Constructor for reader.</p>
	 *
	 * @param argz the arguments containing all of the main information
	 * @throws java.io.FileNotFoundException thrown when the current file is not found
	 */
	public reader(arguments argz) throws FileNotFoundException
	{
		super(new BufferedReader(new InputStreamReader(new FileInputStream(argz.getSourceFile().getAbsolutePath()), Charset.defaultCharset())));
		this.colNum = 1;
		this.lineNum = 1;
		this.kurkar = 0;
		this.args=argz;
	}

	//region PublicMethods
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object kar) {
		if (! char.class.isInstance(kar)) return false;
		else return kar.equals((char) kurkar.intValue());
	}

	//region MovingCharacters
	//region NextCharacter

	/**
	 * <p>next.</p>
	 *
	 * @return {@literal -} int
	 * @throws java.io.IOException caused by the scanner
	 */
	public int next() throws IOException {
		this.setKurkar(super.read());

		if (this.current() == '\n') {
			lineNum++;
			colNum = 1;
		}
		else if (this.current() == '\t') colNum += 3;
		else colNum++;

		return this.kurrent();
	}

	/**
	 * <p>cext.</p>
	 *
	 * @return {@literal -} char
	 * @throws java.io.IOException caused by the scanner
	 */
	public char cext() throws IOException {
		return (char) this.next();
	}

	/**
	 * <p>pext.</p>
	 *
	 * @return the current char
	 * @throws java.io.IOException caused by the scanner
	 */
	public char pext() throws IOException {
		Integer pext = this.kurrent();
		this.next();
		return (char) pext.intValue();
	}
	//endregion

	//region CurrentCharacter

	/**
	 * <p>kurrent.</p>
	 *
	 * @return {@literal -} int
	 */
	public int kurrent() {
		return kurkar.intValue();
	}

	/**
	 * <p>current.</p>
	 *
	 * @return {@literal -} char
	 */
	public char current() {
		return (char) kurkar.intValue();
	}
	//endregion

	/**
	 * <p>rollBack.</p>
	 *
	 * @throws java.io.IOException caused by the scanner
	 */
	public void rollBack() throws IOException {
		if (this.current() == '\n') lineNum--;

		colNum--;

		super.unread(this.kurrent());
	}

	//endregion

	//region SetterGetter

	/**
	 * <p>Getter for the field <code>colNum</code>.</p>
	 *
	 * @return the colNum
	 */
	public Integer getColNum() {
		return colNum - 1;
	}

	/**
	 * <p>Setter for the field <code>kurkar</code>.</p>
	 *
	 * @param kurkar resetting the kurkar to a custom value
	 */
	public void setKurkar(int kurkar) {
		this.kurkar = Integer.valueOf(kurkar);
	}
	//endregion
	//endregion

}
