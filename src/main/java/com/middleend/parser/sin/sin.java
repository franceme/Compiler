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
import lombok.NonNull;
import com.middleend.parser.sin.sinType.specType;
import com.middleend.scanner.token;

/**
 * <p>sin class.</p>
 *
 * @author franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 *
 * This class is the custom data object that contains all of the pertintant information for
 * 		variables, raw values, or methods.
 */
public class sin
{

	//region classFields
	@Getter
	@Setter
	private String name;
	@Getter
	private sinType type;
	@Getter
	private sinTable table;
	@Getter
	@Setter
	private Float lowerbound;
	@Getter
	@Setter
	private Float upperbound;
	@Getter
	@Setter
	private Integer lineNumber;
	@Getter
	@Setter
	private String val;
	@Getter
	@Setter
	private Boolean isGlobal=false;
	//endregion

	//region Constructors

	/**
	 * <p>Constructor for sin.</p>
	 *
	 * @param Name the name of the sin
	 * @param type the value containing the type of sin
	 */
	public sin(@NonNull String Name, @NonNull sinType type) {
		this.type = type;
		this.table = null;
		this.name=Name;
	}

	/**
	 * <p>Constructor for sin.</p>
	 *
	 * @param newToken the token to be translated into a sin
	 */
	public sin(@NonNull token newToken) {
		this.type = newToken.getTokenType();
		this.table = null;
	}

	/**
	 * <p>Constructor for sin.</p>
	 *
	 * @param Name - the name of the sin
	 */
	public sin(@NonNull String Name) {
		this.name=Name;
	}
	//endregion

	//region InternalMethods
	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuilder rtrn = new StringBuilder();

		rtrn.append("\"name\" : \""+this.name+"\"\n");

		rtrn.append("\"Type\" : " + this.type);

		if (this.lineNumber != null) rtrn.append(",\"Line#\" : \"" + this.lineNumber + "\"");

		if (this.table != null) rtrn.append(",\"Table\" : " + this.table);

		if (this.lowerbound != null && this.upperbound != null)
			rtrn.append(",\"Bounds\" : \"[" + this.lowerbound + " : " + this.upperbound + "]\"");

		return "{" + rtrn.toString() + "}";
	}

	/**
	 * <p>isArray.</p>
	 *
	 * @return boolean checking if the current sin is an array
	 */
	public boolean isArray() {
		return lowerbound != null && upperbound != null;
	}

	/**
	 * <p>Setter for the field <code>table</code>.</p>
	 *
	 * @param tableu the table contained by the sin
	 */
	public void setTable(sinTable tableu)
	{
		this.table=tableu;
		this.type = new sinType(specType.TABLE);
	}


	/**
	 * <p>arrayLength.</p>
	 *
	 * @return the length of the array
	 */
	public int arrayLength()
	{
		if (this.isArray())
			return Math.round(this.upperbound-this.lowerbound);
		else
			return -1;
	}

	/**
	 * <p>irDeclaration</p>
	 *
	 * @return the string version of the sin declaration
	 */
	public String irVarDecl()
	{
		StringBuilder irDecl=new StringBuilder();
			if (this.isGlobal)
				irDecl.append("@");
			else
				irDecl.append("%");

			irDecl.append(this.getName());

			if (this.isGlobal)
				irDecl.append(" = common global ");
			else
				irDecl.append(" = alloca ");

		irDecl.append(this.getType().ir(this.isGlobal,this.arrayLength()));

		return irDecl.toString();
	}

	/**
	 * <p>irParam</p>
	 *
	 * @param typeOfCalledParameter when used it is being used within a function call
	 * @return the string ir representation of this variable as a param
	 */
	public String irParam(sinType.paramType typeOfCalledParameter)
	{
		StringBuilder param=new StringBuilder();

		param.append(this.getType().ir(false,this.arrayLength()));

		if ((this.getType().getParam()!=null && !this.getType().getParam().equals(sinType.paramType.IN)) || (typeOfCalledParameter!=null && !typeOfCalledParameter.equals(sinType.paramType.IN)))
			param.append("*");

		if (!this.name.equals("RAW"))
			param.append(" %"+this.getName());
		else
			param.append(" "+this.getVal());


		return param.toString();
	}
	//endregion

}
