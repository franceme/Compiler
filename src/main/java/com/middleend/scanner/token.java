/**
 * Name: franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 * DateCreated: 2018-01-18
 */
package com.middleend.scanner;

import java.util.ArrayList;
import java.util.List;

//@formatter:off
/* Problem via Linux Side
	import com.fasterxml.jackson.annotation.JsonIgnore;
	import com.fasterxml.jackson.annotation.JsonInclude;
	import com.fasterxml.jackson.annotation.JsonInclude.Include;
	import com.fasterxml.jackson.core.JsonProcessingException;
	import com.fasterxml.jackson.databind.ObjectMapper;
*/
//@formatter:on

import lombok.Getter;
import lombok.Setter;
import com.middleend.parser.sin.sinType;
import com.middleend.parser.sin.sinType.groupType;
import com.middleend.parser.sin.sinType.specType;

/**
 *
 * <p>token class.</p>
 * This token is the initial container of ids/reserved words, and everything.
 * <p>
 * &#8195;Contains a lot of internal functionality to work with at a fundamental
 * level.
 *
 * @author franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 */
public class token {

	//region ClassFields
	@Getter
	private sinType tokenType = null;
	@Setter
	private StringBuilder tokenVal = new StringBuilder();
	@Getter
	@Setter
	private Integer lineNum;
	@Getter
	@Setter
	private Integer colNum;
	@Getter
	private Boolean isError = false;
	private List<token> errorPortion;
	private List<Integer> folding;
	//endregion

	//region Constructors

	/**
	 * <p>Constructor for token.</p>
	 *
	 * @param kurType    the type of the current token
	 * @param lineNumber the line number of the token
	 * @param colNumber  the column number of the token
	 */
	public token(specType kurType, int lineNumber, int colNumber) {

		this.lineNum = lineNumber;
		this.colNum = colNumber;
		this.setTokenType(kurType);
	}

	/**
	 * <p>Constructor for token.</p>
	 *
	 * @param kurrentKar the starting character of the token value
	 * @param kurType    the type of the token
	 * @param lineNumber the line number of the token
	 * @param colNumber  the column number of the token
	 */
	public token(char kurrentKar, specType kurType, int lineNumber, int colNumber) {

		this.lineNum = lineNumber;
		this.colNum = colNumber;
		this.append(kurrentKar);

		this.setTokenType(kurType);
	}
	//endregion

	//region PublicMethods

	//region OverriddenMethods

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof String) return this.getTokenVal().equals((String) obj);
		if (obj instanceof specType) return this.isToken((specType) obj);
		if (obj instanceof groupType) return this.getTokenType().getGroup().equals((groupType) obj);
		else return false;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		/* Json */
		//@formatter:off
		/*
		try
		{
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
			return e.getMessage();
		}
		*/

		/* basic */
		 if (this.folding != null)
		 	return "<" + this.tokenType.toString().toLowerCase() + "," + this.getTokenVal() + "," + String.valueOf(this.lineNum) + "," + this.folding.toString() + ">"; 
		 else
		 	return "<" + this.tokenType.toString().toLowerCase() + "," +this.getTokenVal() + "," + String.valueOf(this.lineNum) + ">";
		//@formatter:on
	}
	//endregion

	/**
	 * <p>length.</p>
	 *
	 * @return {@literal -} int length
	 */
	public int length() {
		if (this.folding == null) return this.tokenVal.length();
		else return this.tokenVal.length() + folding.size();
	}

	//region AppendingValues

	/**
	 * <p>addError.</p>
	 *
	 * @param errorkar   the error character of the token
	 * @param lineNumber the line number of the error character
	 * @param colNumber  the column number of the error character
	 */
	public void addError(char errorkar, int lineNumber, int colNumber) {
		if (errorPortion == null) this.errorPortion = new ArrayList<token>();

		this.errorPortion.add(new token(errorkar, specType.ERROR, lineNumber, colNumber));
		this.isError = true;
	}

	/**
	 * <p>append.</p>
	 *
	 * @param kar appending kar to the currentVal
	 */
	public void append(char kar) {
		this.tokenVal.append(Character.toLowerCase(kar));
	}

	/**
	 * <p>append.</p>
	 *
	 * @param kar  appending kar to the currentVal
	 * @param type the specType to reset the current token
	 */
	public void append(char kar, specType type) {
		this.append(kar);
		this.tokenType.setType(type);
	}

	/**
	 * <p>addFold.</p>
	 */
	public void addFold() {
		if (folding == null) folding = new ArrayList<Integer>();

		folding.add(this.length());
	}

	/**
	 * <p>addTokenVal.</p>
	 *
	 * @param kurkar the char to add on into the string builder
	 */
	public void addTokenVal(char kurkar) {
		this.tokenVal.append(kurkar);
	}
	//endregion

	/**
	 * <p>flipFolding.</p>
	 */
	public void flipFolding() {
		if (this.folding != null && this.folding.size() > 0) {
			List<Integer> temp = new ArrayList<Integer>();
			for (Integer kai : this.folding)
				temp.add(this.tokenVal.toString().length() - kai);
			this.folding = temp;
		}
	}

	//region BoilerplateStuff

	/**
	 * <p>Setter for the field <code>tokenType</code>.</p>
	 *
	 * @param tokenType the tokenType to set
	 */
	public void setTokenType(specType tokenType) {
		this.tokenType = new sinType(tokenType);
	}

	/**
	 * <p>Getter for the field <code>tokenVal</code>.</p>
	 *
	 * @return the tokenVal
	 */
	public String getTokenVal() {
		if (this.folding != null && this.folding.size() > 0) {
			StringBuilder tempBuilder = new StringBuilder(this.tokenVal);
			tempBuilder.reverse();

			int ktr = 0;
			for (int place : this.folding) {
				tempBuilder.insert(place + ktr, '_');
				ktr++;
			}

			tempBuilder.reverse();
			return tempBuilder.toString();
		}
		else return tokenVal.toString();
	}

	/**
	 * <p>isError.</p>
	 */
	public void isError() {
		this.isError = true;
	}

	/**
	 * <p>isToken.</p>
	 *
	 * @param end checking if the current token is the same type of end
	 * @return boolean
	 */
	public boolean isToken(specType end) {
		return this.tokenType.getType() == end;
	}
	//endregion
	//endregion

}
