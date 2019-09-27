package com.middleend.scanner;

import java.io.IOException;

import com.frontend.overhead.CompilerError;
import com.frontend.overhead.arguments;
import com.middleend.parser.sin.sinType;
import com.middleend.parser.sin.sinType.specType;

/**
 * <p>scandew class</p>
 * <p>
 * Class: Compiler Theory
 * <p>
 * Project: Custom Compiler
 *
 * @author franceme Essentially the encapsulated scanner class.
 * <p>
 * Used for "streaming" via @hasNext and @kurtoken to go through tokens
 * from the sourcecode.
 * <p>
 * This will never return null, the @hasNext will flag as false if there
 * are no more tokens.
 */
public class scandew {

	//region Fields
	private reader sourcecode;
	// The current token, read first
	private token kurtoken;
	// The next token, read second
	private token nxttoken;
	// The arguments pulled from the start
	@SuppressWarnings ("unused")
	private arguments args;
	//endregion


	/**
	 * Creating the current scanner class
	 * <p>
	 * Essentially creates "stream" of tokens
	 *
	 * @param argz the arguments given through the main class
	 * @throws java.io.IOException caused the scanner class
	 * @throws com.frontend.overhead.CompilerError caused by an incorrect token
	 */
	public scandew(arguments argz) throws IOException, CompilerError {
		sourcecode = new reader(argz);

		this.kurtoken = this.readNextToken();
		this.nxttoken = this.readNextToken();

	}

	//region PublicMethods
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "{\nCurrent : \n" + this.kurtoken + "\nNext : \n" + this.nxttoken + "}";
	}

	//region MovingToken

	/**
	 * This damn class...
	 * Reads through the sourcecode and returns the latest token. A huge switch/case
	 * case case...
	 *
	 * @return token the next token from the file
	 *
	 * @throws IOException an exception if the file empties out
	 * @throws com.frontend.overhead.CompilerError caused by an incorrect token
	 */
	@SuppressWarnings ("unlikely-arg-type")
	private token readNextToken() throws IOException, CompilerError {

		sourcecode.next();
		while (isEmpty(sourcecode.current()))
			sourcecode.next();

		if (sourcecode.kurrent() == - 1) return null;

		token kurtoken = null;
		switch (sourcecode.current()) {

			//region Comment
			case '/':
				switch (sourcecode.next()) {
					case '/':
						/* Spinning until the end of line character */
						do {
						}
						while (sourcecode.cext() != '\n' && sourcecode.kurrent() != - 1);
						return readNextToken();
					case '*':
						/* Spinning until the end of multi line comment */
						int depthktr = 1;
						do {
							switch (sourcecode.cext()) {
								// End of the comment - ?
								case '*':
									if (sourcecode.cext() == '/') depthktr--;
									break;

								// New nested comment
								case '/':
									if (sourcecode.cext() == '*') depthktr++;
									break;
							}
						}
						while (depthktr > 0 && sourcecode.kurrent() != - 1);
						return readNextToken();
					default:
						kurtoken = new token(sourcecode.current(), specType.DIV, sourcecode.getLineNum(),
								sourcecode.getColNum());
						sourcecode.rollBack();
						break;
				}
				break;
			//endregion

			//region Character
			//@formatter:off
			case 'a' :case 'A' :case 'b' :case 'B' :case 'c' :case 'C' :case 'd' :case 'D' :case 'e' :case 'E' :case 'f' :case 'F' :case 'g' :case 'G' :case 'h' :case 'H' :case 'i' :case 'I' :case 'j' :case 'J' :case 'k' :case 'K' :case 'l' :case 'L' :case 'm' :case 'M' :case 'n' :case 'N' :case 'o' :case 'O' :case 'p' :case 'P' :case 'q' :case 'Q' :case 'r' :case 'R' :case 's' :case 'S' :case 't' :case 'T' :case 'u' :case 'U' :case 'v' :case 'V' :case 'w' :case 'W' :case 'x' :case 'X' :case 'y' :case 'Y' :case 'z' :case 'Z' :
			//@formatter:on
				kurtoken = new token(sinType.specType.ID, sourcecode.getLineNum(), sourcecode.getColNum());

				do {
					kurtoken.append(sourcecode.current());
				}
				while (isValidID(sourcecode.cext()));
				sourcecode.rollBack();

				// Resetting the token type to either ID or a more explicit token type
				kurtoken.setTokenType(sinType.specType.reserveToSpecType(kurtoken.getTokenVal()));

				break;
			//endregion

			//region Number
			//@formatter:off
			case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':
			//@formatter:on
				kurtoken = new token(specType.INTEGER, sourcecode.getLineNum(), sourcecode.getColNum());
				int dotcounter = - 1;

				do {
					dotcounter++;

					if (dotcounter > 1) {
						kurtoken.isError();
						kurtoken.addError('.', sourcecode.getLineNum(), sourcecode.getColNum());
					}

					do {
						if (sourcecode.current() == '_') kurtoken.addFold();
						else kurtoken.append(sourcecode.current());
					}
					while (isNumber(sourcecode.cext()) || sourcecode.current() == '_');

				}
				while (sourcecode.current() == '.');

				kurtoken.flipFolding();
				if (dotcounter>0)
					kurtoken.setTokenType(specType.FLOAT);

				sourcecode.rollBack();

				break;
			//endregion

			//region Quote Handling
			case '"':
				kurtoken = new token(sourcecode.current(), specType.STRING, sourcecode.getLineNum(),
						sourcecode.getColNum());
				sourcecode.next();
				do {
					if (isValidString(sourcecode.current())) kurtoken.append(sourcecode.current());
					else kurtoken.addError(sourcecode.current(), sourcecode.getLineNum(), sourcecode.getColNum());

				}
				while (sourcecode.cext() != '"');
				kurtoken.append(sourcecode.current());
				break;

			case '\'':
				kurtoken = new token(sourcecode.pext(), specType.CHAR, sourcecode.getLineNum(), sourcecode.getColNum());
				do {
					if (isValidChar(sourcecode.current())) kurtoken.append(sourcecode.current());
					else kurtoken.addError(sourcecode.current(), sourcecode.getLineNum(), sourcecode.getColNum());

				}
				while (sourcecode.cext() != '\'');
				kurtoken.append(sourcecode.current());
				if (kurtoken.getTokenVal().replaceAll("'", "").length() > 1) kurtoken.isError();
				break;
			//endregion

			//region Misc
			case ';':
				kurtoken = new token(sourcecode.current(), specType.SEMICOLON, sourcecode.getLineNum(),
						sourcecode.getColNum());
				break;
			case ',':
				kurtoken = new token(sourcecode.current(), specType.COMMA, sourcecode.getLineNum(),
						sourcecode.getColNum());
				break;

			case ':':
				kurtoken = new token(sourcecode.current(), specType.COLON, sourcecode.getLineNum(),
						sourcecode.getColNum());
				if (sourcecode.cext() == '=') kurtoken.append(sourcecode.current(), specType.ASSIGN);
				else sourcecode.rollBack();
				break;

			case '.':
				kurtoken = new token(sourcecode.current(), specType.PERIOD, sourcecode.getLineNum(),
						sourcecode.getColNum());
				break;

			case '&':
				kurtoken = new token(sourcecode.current(), specType.AND, sourcecode.getLineNum(),
						sourcecode.getColNum());
				break;
			case '|':
				kurtoken = new token(sourcecode.current(), specType.OR, sourcecode.getLineNum(),
						sourcecode.getColNum());
				break;

			case '+':
				kurtoken = new token(sourcecode.current(), specType.PLUS, sourcecode.getLineNum(),
						sourcecode.getColNum());
				break;
			case '-':
				kurtoken = new token(sourcecode.current(), specType.MIN, sourcecode.getLineNum(),
						sourcecode.getColNum());
				break;
			case '*':
				kurtoken = new token(sourcecode.current(), specType.MULT, sourcecode.getLineNum(),
						sourcecode.getColNum());
				break;

			case '(':
				kurtoken = new token(sourcecode.current(), specType.PAREN_ST, sourcecode.getLineNum(),
						sourcecode.getColNum());
				break;
			case ')':
				kurtoken = new token(sourcecode.current(), specType.PAREN_ND, sourcecode.getLineNum(),
						sourcecode.getColNum());
				break;
			case '[':
				kurtoken = new token(sourcecode.current(), specType.BRACK_ST, sourcecode.getLineNum(),
						sourcecode.getColNum());
				break;
			case ']':
				kurtoken = new token(sourcecode.current(), specType.BRACK_ND, sourcecode.getLineNum(),
						sourcecode.getColNum());
				break;
			//endregion

			//region ConditionalChecking
			//@formatter:off
			case '>':case '<':
			//@formatter:on
				kurtoken = new token(sourcecode.current(), sourcecode.equals(">") ? specType.GT : specType.LT,
						sourcecode.getLineNum(), sourcecode.getColNum());

				if (sourcecode.cext() == '=') if (kurtoken.getTokenType().getType() == specType.LT)
					kurtoken.append(sourcecode.current(), specType.LT_EQL);
				else kurtoken.append(sourcecode.current(), specType.GT_EQL);
				else sourcecode.rollBack();
				break;

			//@formatter:off
			case '=':case '!':
			//@formatter:on
				kurtoken = new token(sourcecode.current(), sourcecode.equals("=") ? specType.ASSIGN : specType.ERROR,
						sourcecode.getLineNum(), sourcecode.getColNum());

				if (sourcecode.cext() == '=') if (kurtoken.getTokenType().getType() == specType.ASSIGN)
					kurtoken.append(sourcecode.current(), specType.EQUAL);
				else kurtoken.append(sourcecode.current(), specType.NT_EQUAL);

				else {
					kurtoken.isError();
					sourcecode.rollBack();
				}
				break;
			//endregion

			default:
				throw new CompilerError("Invalid Character token : "+ sourcecode.current(),sourcecode.getLineNum());
		}
		return kurtoken;
	}

	/**
	 * Returns the current token, nothing interesting here.
	 *
	 * @return the kurtoken
	 */
	public token current() {
		return this.kurtoken;
	}

	/**
	 * Returns the current token, and then updates both the current and next token.
	 * <br>
	 * <b>POST RETURN ADVANCE</b>
	 *
	 * @return current token
	 * @throws java.io.IOException caused the scanner class
	 * @throws com.frontend.overhead.CompilerError caused by an incorrect token
	 */
	public token next() throws IOException, CompilerError {
		token rtrn = this.current();
		if (this.kurtoken != null) {
			if (this.nxttoken == null) this.kurtoken = null;
			else {
				this.kurtoken = this.nxttoken;
				this.nxttoken = this.readNextToken();
			}
		}
		return rtrn;
	}

	/**
	 * <p>peak.</p>
	 *
	 * @return {@literal -} current token
	 */
	public token peak() {
		return this.nxttoken;
	}
	//endregion

	//region CheckingCharacter

	/**
	 * Checks if the given char is an whitespace char, ' ', '\n', '\r', '\t'.
	 * <p>
	 * Checks single digit only.
	 *
	 * @param steve the character being checked
	 *
	 * @return if the character is an empty or whitespace character
	 */
	private boolean isEmpty(char steve) {
		if (steve == ' ' || steve == '\n' || steve == '\r' || steve == '\t') return true;
		else return false;
	}

	/**
	 * Checks if the given char is a valid character for a string, [a{@literal -}zA{@literal -}Z0{@literal -}9
	 * _,;:.'].
	 * <p>
	 * Checks single char only.
	 *
	 * @param kar the char being checked
	 *
	 * @return bool if the character is valid
	 */
	private boolean isValidString(char kar) {
		if (isValidIDExpanded(kar) || kar == ',' || kar == '\'') return true;
		else return false;
	}

	/**
	 * Checks if the given char is a valid character for a char, [a{@literal -}zA{@literal -}Z0{@literal -}9 _;:."].
	 * <p>
	 * Checks single char only.
	 *
	 * @param kar the char being checked
	 *
	 * @return bool if the character is valid
	 */
	private boolean isValidChar(char kar) {
		if (isValidIDExpanded(kar) || kar == '"') return true;
		else return false;
	}

	/**
	 * Checks if the given char is a @isValidID, ' ', ';', ':', or a '.', [a{@literal -}zA{@literal -}Z0{@literal -}9
	 * _,;:.].
	 * <p>
	 * Checks single char only.
	 *
	 * @param kar the char being checked
	 *
	 * @return bool if the character is valid
	 */
	private boolean isValidIDExpanded(char kar) {
		if (isValidID(kar) || kar == ' ' || kar == ';' || kar == ':' || kar == '.') return true;
		else return false;
	}

	/**
	 * Checks if the given char is a @isLetter, @isNumber, or is a space,
	 * [a{@literal -}zA{@literal -}Z0{@literal -}9_].
	 * <p>
	 * Checks single char only.
	 *
	 * @param kar the char being checked
	 *
	 * @return bool if the character is valid
	 */
	private boolean isValidID(char kar) {
		if (isLetter(kar) || isNumber(kar) || kar == '_') return true;
		else return false;
	}

	/**
	 * Checks if the given char is a letter of capital/lowercase, [a{@literal -}zA{@literal -}Z].
	 * <p>
	 * Checks single char only.
	 *
	 * @param kar the char being checked
	 *
	 * @return bool if the character is valid
	 */
	private boolean isLetter(char kar) {
		switch (kar) {
			//@formatter:off
			case 'a' :case 'A' :case 'b' :case 'B' :case 'c' :case 'C' :case 'd' :case 'D' :case 'e' :case 'E' :case 'f' :case 'F' :case 'g' :case 'G' :case 'h' :case 'H' :case 'i' :case 'I' :case 'j' :case 'J' :case 'k' :case 'K' :case 'l' :case 'L' :case 'm' :case 'M' :case 'n' :case 'N' :case 'o' :case 'O' :case 'p' :case 'P' :case 'q' :case 'Q' :case 'r' :case 'R' :case 's' :case 'S' :case 't' :case 'T' :case 'u' :case 'U' :case 'v' :case 'V' :case 'w' :case 'W' :case 'x' :case 'X' :case 'y' :case 'Y' :case 'z' :case 'Z' :
			//@formatter:on
				return true;
			default:
				return false;
		}
	}

	/**
	 * Checks if the given char is a digit of any kind, [0{@literal -}9].
	 * <p>
	 * Checks single char only.
	 *
	 * @param kar the char being checked
	 *
	 * @return bool if the character is valid
	 */
	private boolean isNumber(char kar) {
		switch (kar) {
			//@formatter:off
			case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':
			//@formatter:on
				return true;
			default:
				return false;
		}
	}
	//endregion
	//endregion


}
