/**
 * Name : franceme
 * Class : Compiler Theory
 * Project : Custom Compiler
 * DateCreated : 2018-02-06
 * FileRevision : $Revision$
 */
package com.middleend.parser.sin;

import lombok.Getter;
import lombok.Setter;
import com.frontend.overhead.CompilerError;

/**
 * <p>sinType class.</p>
 *
 * @author franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 *
 * The type of the symbol (sin) in question.
 * This determines the exact function of the symbol, the grouping of the symbol, or the paramater type.
 */
public class sinType {

	//region ClassFields
	@Getter
	private groupType group;
	@Getter
	private specType type;
	@Getter
	@Setter
	private paramType param;
	@Getter
	@Setter
	private boolean array = false;
	//endregion

	//region SpecType

	/**
	 * @author minkus
	 */
	public enum specType {

		//region EnumValues
		/**
		 *
		 */
		PERIOD("."),
		/**
		 *
		 */
		ID("[a-zA-Z][a-zA-Z0-9_]*"),
		/** * */
		key_IF("if"),
		/** * */
		key_ELSE("else"),
		/** * */
		key_THEN("then"),
		/** * */
		key_END("end"),
		/** * */
		key_PROGRAM("program"),
		/** * */
		key_PROCEDURE("procedure"),
		/** * */
		key_BEGIN("begin"),
		/** * */
		key_RETURN("return"),
		/** * */
		key_IN("in"),
		/** * */
		key_OUT("out"),
		/** * */
		key_INOUT("inout"),
		/** * */
		key_GLOBAL("global"),
		/** * */
		key_FOR("for"),
		/**
		 * is
		 */
		key_IS("is"),
		/**
		 *
		 */
		ERROR("unknown"),
		/**
		 * {@literal [}0{@literal -}9{@literal ]}{@literal [}0{@literal -}9{@literal _}{@literal ]}{@literal *}{@literal [}{@literal .}{@literal [}0{@literal -}9{@literal _}{@literal ]}{@literal *}{@literal ]}
		 */
		INTEGER("int"),
		/**
		 * [0-9][0-9_]*[.[0-9_]*]
		 */
		FLOAT("float"),
		/**
		 * "[a-zA-Z0-9 _,;:.']*"
		 */
		STRING("char[]"),
		/**
		 * true, false
		 */
		BOOL("bool"),
		/**
		 * '[a-zA-Z0-9 _;:."]'
		 */
		CHAR("char"),
		/**
		 * {@literal &}
		 */
		AND("&"),
		/**
		 * {@literal |}
		 */
		OR("|"),
		/**
		 * not
		 */
		NOT("not"),
		/**
		 * {@literal +}
		 */
		PLUS("+"),
		/**
		 * {@literal -}
		 */
		MIN("-"),
		/**
		 * {@literal *}
		 */
		MULT("*"),
		/**
		 * {@literal /}
		 */
		DIV("/"),
		/**
		 * {@literal :}
		 */
		COLON(":"),
		/**
		 * {@literal ;}
		 */
		SEMICOLON(";"),
		/**
		 * {@literal ,}
		 */
		COMMA(","),
		/**
		 * {@literal (}
		 */
		PAREN_ST("("),
		/**
		 * {@literal )}
		 */
		PAREN_ND(")"),
		/**
		 * {@literal [}
		 */
		BRACK_ST("["),
		/**
		 * {@literal ]}
		 */
		BRACK_ND("]"),
		/**
		 * {@literal :}{@literal =}
		 */
		ASSIGN(":="),
		/**
		 * {@literal =}{@literal =}
		 */
		EQUAL("=="),
		/**
		 * {@literal !}{@literal =}
		 */
		NT_EQUAL("!="),
		/**
		 * {@literal <}
		 */
		LT("<"),
		/**
		 * {@literal <=}
		 */
		LT_EQL("<="),
		/**
		 * {@literal >}
		 */
		GT(">"),
		/**
		 * {@literal >=}
		 */
		GT_EQL(">="),
		/**
		 * SinTable
		 */
		TABLE("SinTable"),
		/**
		 * getBool
		 **/
		getBool("getBool"),
		/**
		 * getInteger
		 **/
		getInteger("getInteger"),
		/**
		 * getFloat
		 **/
		getFloat("getFloat"),
		/**
		 * getString
		 **/
		getString("getString"),
		/**
		 * getChar
		 **/
		getChar("getChar"),
		/**
		 * putBool
		 **/
		putBool("getBool"),
		/**
		 * putInteger
		 **/
		putInteger("putInteger"),
		/**
		 * putFloat
		 **/
		putFloat("putFloat"),
		/**
		 * putString
		 **/
		putString("putString"),
		/**
		 * putChar
		 **/
		putChar("putChar");
		//endregion

		private final String stringVal;

		/**
		 *
		 * @param val the string value of the specType
		 */
		specType(String val) {
			this.stringVal = val;
		}

		//region PublicMethods

		/**
		 * @param check checking if the string check is in the enum
		 *
		 * @return boolean
		 */
		public static boolean contains(String check) {
			for (specType kai : values())
				if (kai.toString().equals(check.toUpperCase())) return true;
			return false;
		}

		/**
		 * @param reserve the string to be checked if its a reserved word
		 *
		 * @return specType
		 */
		public static specType reserveToSpecType(String reserve) {
			if (reserve.equalsIgnoreCase("true") || reserve.equalsIgnoreCase("false")
					|| reserve.equalsIgnoreCase("bool"))
				return specType.BOOL;
			else if (reserve.equalsIgnoreCase("integer")) return specType.INTEGER;
			else if (reserve.equalsIgnoreCase("string")) return specType.STRING;
			else if (reserve.equalsIgnoreCase("float")) return specType.FLOAT;
			else if (reserve.equalsIgnoreCase("char")) return specType.CHAR;
			else {
				for (specType kai : values())
					if (kai.shortHand().equalsIgnoreCase(reserve) && kai.toString().startsWith("key_")) return kai;
					else if (kai.shortHand().equalsIgnoreCase(reserve)
							&& (reserve.startsWith("get") || reserve.startsWith("put")))
						return kai;
				return specType.ID;
			}
		}

		/** Boilerplate **/

		/**
		 * @return string version of the enum
		 */
		public String shortHand() {
			return this.stringVal;
		}
		//endregion

	}
	//endregion

	//region GroupType

	/**
	 * @author minkus
	 */
	public enum groupType {
		//region EnumValues
		//@formatter:off
		/**
		 * getBool(bool val out)
		 * getInteger(integer val out)
		 * getFloat(float val out)
		 * getString(string val out)
		 * getChar(char val out)
		 * putBool(bool val in)
		 * putInteger(integer val in)
		 * putFloat(float val in)
		 * putString(string val in)
		 * putChar(char val in)
		 *///@formatter:on
		BUILTIN("get/put"),
		/**
		 * any raw value,
		 */
		ID("ptr to sintable..."),
		/**
		 * (.),[,]
		 */
		CAPSULE("(,),[,]"),
		/**
		 * , :
		 */
		SEPERATORS(", ,:"),
		/**
		 * ;,.
		 */
		FINISHERS(";,."),
		/**
		 * return
		 */
		RETURN("return"),
		/**
		 * integer, string, char, bool, float
		 */
		TYPE_MARK("integer, string, char, bool, float"),
		/**
		 * GLOBAL
		 */
		GLOBAL("global"),
		/**
		 * in, out, inout
		 */
		PARAM("in, out, inout"),
		/**
		 * end
		 */
		END("end"),
		/**
		 * is
		 */
		IS("is"),
		/**
		 * begin
		 */
		BEGIN("begin"),
		/**
		 * procedure
		 */
		PROCEDURE("procedure"),
		/**
		 * program
		 */
		PROGRAM("program"),
		/**
		 * if, then, else
		 */
		IF("if, then, else"),
		/**
		 * for
		 */
		LOOP("for"),
		/**
		 * {@literal !},{@literal |},{@literal &}
		 */
		BITWISE("!, |, &"),
		/**
		 * {@literal +},{@literal -}
		 */
		ARITHOP("+, -"),
		/**
		 * {@literal *},{@literal /}
		 */
		CALC("*, /"),
		/**
		 * {@literal <},{@literal <}{@literal =},
		 * {@literal >},{@literal >}{@literal =},{@literal =}{@literal =},{@literal !}{@literal =}
		 */
		COND("<,<=,>,>=, ==, !="),
		/**
		 * :=
		 */
		ASSIGN(":="),
		/**
		 * A new sintable
		 */
		TABLE("SinTable"),
		/**
		 * Complete Failure, something wrong happened
		 */
		ERROR("unkwn");
		//endregion

		private final String stringVal;

		/**
		 *
		 * @param val the string value of the groupType
		 */
		groupType(String val) {
			this.stringVal = val;
		}

		//region PublicMethods

		/**
		 * @param check checking if the string check is in the enum
		 *
		 * @return boolean
		 */
		public static boolean contains(String check) {
			for (groupType kai : values())
				if (kai.toString().equals(check.toUpperCase())) return true;
			return false;
		}

		/**
		 * @param input setting the grouptype based off of the spectype passed in
		 *
		 * @return valueType
		 */
		public static groupType checkType(specType input) {
			if (input == specType.TABLE) return TABLE;

			if (input == specType.INTEGER || input == specType.STRING || input == specType.BOOL
					|| input == specType.CHAR || input == specType.FLOAT)
				return TYPE_MARK;

			if (input == specType.MULT || input == specType.DIV) return CALC;

			if (input == specType.PLUS || input == specType.MIN) return ARITHOP;

			if (input == specType.AND || input == specType.OR || input == specType.NOT) return BITWISE;

			if (input == specType.BRACK_ND || input == specType.BRACK_ST || input == specType.PAREN_ND
					|| input == specType.PAREN_ST)
				return CAPSULE;

			if (input == specType.EQUAL || input == specType.NT_EQUAL || input == specType.LT
					|| input == specType.LT_EQL || input == specType.GT || input == specType.GT_EQL)
				return COND;

			if (input == specType.COLON || input == specType.COMMA) return SEPERATORS;

			if (input == specType.ID || input == specType.INTEGER || input == specType.FLOAT || input == specType.STRING
					|| input == specType.CHAR || input == specType.BOOL)
				return ID;

			if (input == specType.SEMICOLON || input == specType.PERIOD) return FINISHERS;

			if (input == specType.key_PROGRAM) return PROGRAM;

			if (input == specType.key_PROCEDURE) return PROCEDURE;

			if (input == specType.key_IF || input == specType.key_ELSE || input == specType.key_THEN) return IF;

			if (input == specType.key_END) return END;

			if (input == specType.key_BEGIN) return BEGIN;

			if (input == specType.key_IN || input == specType.key_OUT || input == specType.key_INOUT) return PARAM;

			if (input == specType.key_GLOBAL) return GLOBAL;

			if (input == specType.ASSIGN) return ASSIGN;

			if (input == specType.key_RETURN) return RETURN;

			if (input == specType.key_FOR) return LOOP;

			if (input == specType.key_IS) return IS;

			if (input == specType.getBool || input == specType.getInteger || input == specType.getFloat
					|| input == specType.getString || input == specType.getChar || input == specType.putBool
					|| input == specType.putInteger || input == specType.putFloat || input == specType.putString
					|| input == specType.putChar)
				return BUILTIN;

			return ERROR;
		}

		/** Boilerplate stuff **/

		/**
		 * @return string version of the enum
		 */
		public String shortHand() {
			return this.stringVal;
		}
		//endregion

	}
	//endregion

	//region ParamType

	/**
	 * @author minkus
	 */
	public enum paramType {
		//region EnumValues
		/**
		 * in param
		 */
		IN("Passed In"),
		/**
		 * out param
		 */
		OUT("Passed Out"),
		/**
		 * in/out param
		 */
		INOUT("Passed In/Out");
		//endregion

		private final String stringVal;

		/**
		 *
		 * @param val the string value used for the paramType
		 */
		paramType(String val) {
			this.stringVal = val;
		}

		//region PublicMethods

		/**
		 * @return string version of the enum
		 */
		public String shortHand() {
			return this.stringVal;
		}

		/**
		 * @param input determining the type of parameter based on the spec type
		 *
		 * @return the designated paramType
		 */
		public static paramType checkType(specType input) {
			if (input == specType.key_IN) return paramType.IN;
			else if (input == specType.key_OUT) return paramType.OUT;
			else if (input == specType.key_INOUT) return paramType.INOUT;
			else return null;
		}
		//endregion
	}

	;
	//endregion

	/**
	 * <p>Constructor for sinType.</p>
	 *
	 * @param type creating a new sinType based on the specType
	 */
	public sinType(specType type) {
		this.setType(type);
	}

	//region PublicMethods

	//region OverriddenMethods
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof sinType) {
			return this.type == ((sinType) obj).getType() && this.group == ((sinType) obj).getGroup()
					&& this.param == ((sinType) obj).getParam();
		}
		else if (obj instanceof String) return this.type.toString().equalsIgnoreCase((String) obj);
		else if (obj instanceof specType) {
			return this.type == obj;
		}
		else return false;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuilder temp = new StringBuilder();

		temp.append("\"SpecType\" : \"" + this.type.toString() + "~>" + this.type.shortHand() + "\"");
		// temp.append("Group : " + this.group.toString() + " => " +
		// this.group.shortHand());
		if (this.param != null) temp.append(",\"ParamType\" : \"" + this.param.toString() + "\"");

		return "{" + temp.toString() + "}";
	}
	//endregion

	//region PublicMethods

	/**
	 * <p>conversionHappy.</p>
	 *
	 * @param rhand new type check
	 * @param assignment a check whether the conversion is for assignment purposes
	 * @return boolean check
	 */
	public boolean conversionHappy(sinType rhand,boolean assignment) {

		if (this.getType() == rhand.getType()) return true;

		if (assignment)
		{
			if (this.type == specType.FLOAT) return (rhand.getType()) == specType.INTEGER;
			else if (this.type == specType.INTEGER) return  (rhand.getType()) == specType.BOOL;
		}
		else
		{
			if (this.type == specType.BOOL)
				return (rhand.getType()) == specType.INTEGER;
			else if (this.type == specType.FLOAT) return (rhand.getType()) == specType.FLOAT
					|| (rhand.getType()) == specType.INTEGER;
			else if (this.type == specType.INTEGER) return (rhand.getType()) == specType.FLOAT
					|| (rhand.getType()) == specType.BOOL;
		}

		return false;
	}

	/**
	 * <p>Setter for the field <code>type</code>.</p>
	 *
	 * @param val the val to set
	 */
	public void setType(specType val) {
		this.type = val;
		this.group = groupType.checkType(val);
		this.param = paramType.checkType(val);
	}

	/**
	 * <p>ir.</p>
	 *
	 * @return the ir representation of the sinType if it's
	 *      a valid type
	 *
	 * @param isGlobal an indicator as to whether the sin is a global or not
	 */
	private String ir(boolean isGlobal)
	{
		if (!this.getGroup().equals(groupType.TYPE_MARK))
			return "";

		StringBuilder irRep=new StringBuilder();

		if (this.getType().equals(specType.INTEGER) || this.getType().equals(specType.BOOL))
			irRep.append("i32");
		else if (this.getType().equals(specType.FLOAT))
			irRep.append("float");
		else if (this.getType().equals(specType.CHAR))
			irRep.append("i8");
		else //if (this.getType().equals(specType.STRING))
			irRep.append("i8*");

		if (isGlobal)
			irRep.append(" "+irDefaultValue());

		return irRep.toString();
	}

	/**
	 * <p>ir.</p>
	 *
	 * @return the ir representation of the sinType if it's
	 *      a valid type
	 * @param arrayLength a check on the length of the array
	 *  if it's -1, than it returns the non-array allocation
	 * @param isGlobal an indicator as to whether the sin is a global or not
	 */
	public String ir(boolean isGlobal,int arrayLength)
	{
		if (arrayLength==-1)
			return this.ir(isGlobal);

		if (!this.getGroup().equals(groupType.TYPE_MARK))
			return "";

		StringBuilder irRep=new StringBuilder("[");

		irRep.append(arrayLength+" x "+this.ir(false, -1));

		irRep.append("]");

		if (isGlobal)
			irRep.append(" zeroinitializer");

		return irRep.toString();
	}

	/**
	 * <p>irDefaultValue</p>
	 *
	 * @return the string default value of the current type
	 */
	public String irDefaultValue()
	{
		if (!this.getGroup().equals(groupType.TYPE_MARK))
			return "";

		if (this.getType().equals(specType.INTEGER) || this.getType().equals(specType.BOOL) || this.getType().equals(specType.CHAR))
			return "0";
		else if (this.getType().equals(specType.FLOAT))
			return "0.000000e+00";
		else //if (this.getType().equals(specType.STRING))
			return "null";
	}

	/**
	 * <p>align.</p>
	 *
	 * @return the align specification
	 * @throws com.frontend.overhead.CompilerError throws a compiler error if it's not type mark
	 */
	public String align() throws CompilerError
	{
		if (!this.getGroup().equals(groupType.TYPE_MARK))
			throw new CompilerError("Current sinType is not a valid IR alignment");

		String rtrn="";

		if (this.getType().equals(specType.CHAR))
			rtrn = "align 1";
		else if (this.getType().equals(specType.INTEGER) || this.getType().equals(specType.BOOL) || this.getType().equals(specType.FLOAT) || this.getType().equals(specType.STRING))
			rtrn = "align 4";

		return rtrn;
	}
	//endregion
	//endregion

}
