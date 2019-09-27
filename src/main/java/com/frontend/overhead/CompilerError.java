/**
 *
 */
package com.frontend.overhead;

import com.middleend.scanner.token;

/**
 * <p>CompilerError class.</p>
 *
 * @author franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 *
 * This class is the custom error that will wrap every kind of exception or error that is thrown throughout
 * the program.
 */
public class CompilerError extends Exception {
	private Integer lineNum = null;
	private String methodName=null;
	private token curToken = null;

	/**
	 *
	 */
	private static final long serialVersionUID = - 8553662306091631425L;

	/**
	 * <p>Constructor for CompilerError.</p>
	 *
	 * @param message to be passed in
	 */
	public CompilerError(String message) {
		super(message);
	}

	/**
	 * <p>Constructor for CompilerError.</p>
	 *
	 * @param message to be passed in
	 * @param methodName to be passed in
	 */
	public CompilerError(String message, String methodName, Integer lineNum) {
		super(message);
		this.methodName=methodName;
		this.lineNum=lineNum;
	}

	/**
	 * <p>Constructor for CompilerError.</p>
	 *
	 * @param message to be passed in
	 * @param lineNumber     the current location of the scanner
	 */
	public CompilerError(String message, int lineNumber) {
		super(message);
		this.lineNum = lineNumber;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuffer rtrn = new StringBuffer();

		rtrn.append(getDivider() + "\n");

		if (this.lineNum != null)
			rtrn.append(addErrorStart() + "Issue @ Line " + this.lineNum + ". \n");

		if (this.methodName != null)
			rtrn.append(addErrorStart()+"Issue within the method : "+this.methodName+"\n");

		rtrn.append(addErrorStart() + this.getMessage() + ".\n");

		if (this.curToken != null)
			rtrn.append(addErrorStart() + "Issue with the following token : " + this.curToken + "\n");

		rtrn.append(getDivider() + "\n");

		return rtrn.toString();
	}

	/**
	 * @return String
	 */
	private String addErrorStart() {
		return "[ERROR] : ";
	}

	/**
	 * @return String
	 */
	private String getDivider() {
		return "-----------------------------------------------------------------------------------------";
	}
}
