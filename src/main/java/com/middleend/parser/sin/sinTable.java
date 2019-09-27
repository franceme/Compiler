/**
 * Name : franceme
 * Class : Compiler Theory
 * Project : Custom Compiler
 * DateCreated : 2018-02-06
 * FileRevision : $Revision$
 */
package com.middleend.parser.sin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.stream.Collectors;

import lombok.Getter;
import com.middleend.parser.sin.sinType.paramType;

/**
 * <p>sinTable class.</p>
 *
 * @author franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 *
 * The class that contains all local variables for the current method.
 * These are all chained so the children all route up to the same parent, essentially like a tree.
 */
public class sinTable extends Hashtable<String, sin> {
	//region classFields
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private sinTable parent = null;
	@Getter
	private ArrayList<String> params=new ArrayList<String>();
	ArrayList<String> BuiltIn = new ArrayList<String>(Arrays.asList("bool", "integer", "float", "string", "char"));
	//endregion

	//region Constructors

	/**
	 * <p>Constructor for sinTable.</p>
	 */
	public sinTable() {
		for (String kai : BuiltIn) {
			// OUT
			sin out=new sin("get" + kai);
			out.setIsGlobal(true);
			sinTable builtout = this.enter_scope(out,-1);
			builtout.put("val", new sin("get" + kai,new sinType(sinType.specType.reserveToSpecType(kai))));
			builtout.addParam("val", paramType.OUT);

			// IN
			sin in=new sin("put" + kai);
			out.setIsGlobal(true);
			sinTable builtin = this.enter_scope(in, - 1);
			builtin.put("val", new sin("put" + kai,new sinType(sinType.specType.reserveToSpecType(kai))));
			builtin.addParam("val", paramType.IN);

		}

	}

	/**
	 * <p>Constructor for sinTable.</p>
	 *
	 * @param parent the parent of the new sintable
	 */
	public sinTable(sinTable parent) {
		this.parent = parent;
	}
	//endregion

	//region PublicMethods

	//region OverriddenMethods
	@Override
	public String toString() {
		StringBuilder rslt = new StringBuilder();
		this.keySet().stream().sorted(
				(sin1, sin2) -> Integer.compare(this.get(sin1).getLineNumber(), this.get(sin2).getLineNumber()))
				.forEach(key -> rslt.append("\"Name\" : \"" + key + "\", \"value\" :" + this.get(key) + "\n"));
		return "{\n" + rslt.toString() + "\n}";
	}

	/**
	 * {@inheritDoc}
	 *
	 * Enabling a return of global variables too Prioritization requires the local
	 * call be used first
	 */
	@Override
	public sin get(Object obj) {
		sin workingSin;
		if ((workingSin = super.get(obj)) != null) return workingSin;
		else if (this.parent != null) return this.getGlobal().get(obj);
		else return null;
	}
	//endregion

	//region MovingScopes

	/**
	 * <p>enter_scope.</p>
	 *
	 * @param workingSin    the sin containing the procedure
	 * @param integer the linenumber of the procedure
	 * @return the new scope
	 */
	public sinTable enter_scope(sin workingSin, Integer integer)
	{
		workingSin.setTable(new sinTable(this));
		workingSin.setLineNumber(integer);

		this.put(workingSin.getName(), workingSin);
		return workingSin.getTable();

	}

	/**
	 * <p>exit_scope.</p>
	 *
	 * @return boolean
	 */
	public sinTable exit_scope() {
		if (this.parent == null) return this;
		else return this.parent;
	}
	//endregion

	//region AddingElements

	/**
	 * <p>getGlobal.</p>
	 *
	 * @return global symbol table
	 */
	public sinTable getGlobal() {
		if (this.parent != null) {
			sinTable rent = this.parent;
			while (rent.parent != null)
				rent = rent.parent;
			return rent;
		}
		else return this;
	}

	/**
	 * <p>getGlobalVariables.</p>
	 *
	 * @return a list of names of global variables/procedures
	 */
	public ArrayList<String> getGlobalVariables()
	{
		return new ArrayList<String>(this.getGlobal().values().stream().filter(vice->vice.getIsGlobal()).map(vice->vice.getName()).collect(Collectors.toList()));
	}

	/**
	 * <p>putGlobal.</p>
	 *
	 * @param newSin the new sin
	 */
	public void putGlobal(sin newSin) {
		this.getGlobal().put(newSin.getName(), newSin);
	}

	/**
	 * <p>addParam.</p>
	 *
	 * @param name  the name of the paramater
	 * @param param the type of the parameter
	 */
	public void addParam(String name, sinType.paramType param) {
		this.params.add(name);
		this.get(name).getType().setParam(param);
	}
	//endregion
	//endregion

}
