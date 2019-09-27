package com.middleend.parser;

import java.io.IOException;
import java.util.ArrayList;

import com.backend.codegen;
import com.middleend.scanner.scandew;
import com.frontend.overhead.CompilerError;
import com.frontend.overhead.arguments;
import com.middleend.parser.sin.sin;
import com.middleend.parser.sin.sinTable;
import com.middleend.parser.sin.sinType;
import com.middleend.parser.sin.sinType.groupType;
import com.middleend.parser.sin.sinType.specType;

/**
 * <p>funnel class.</p>
 *
 * @author franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 *
 * The parser class.
 * The parser functionality is limited to only parsing the tokens in order as given from the scanner (scandew).
 * Any writing to the file is passed to the codegen class.
 */
public class parsertongue {
	//region Fields
	private static scandew sourcecode;
	private static sinTable table;
	private static codegen out;
	private static arguments args;
	private static ArrayList<CompilerError> runningErrors=new ArrayList<CompilerError>();
	//endregion

	/**
	 * The main entry for the parser.
	 *
	 * @param argz     the arguments put into the program
	 * @return {@literal -} object overloaded returning either errors or the ir table
	 * @throws java.io.IOException   caused by the scanner
	 * @throws com.frontend.overhead.CompilerError a custom error object
	 */
	public static Object parse(arguments argz) throws IOException, CompilerError {

		//region Creating Global Variables
		args=argz;
		sourcecode = new scandew(args);
		table = new sinTable();

		out = new codegen(args);
		//endregion

		isValidGoal();

		if (runningErrors.isEmpty())
		{
			out.cleanUp();
			return table;
		}
		else
			return runningErrors;
	}

	//region Generation
	/**
	 * Checks to make sure the source code is a valid {@literal "}goal{@literal "}
	 * <p>
	 * Checks to make sure the program is == validProgram, nothing else after the
	 * program
	 *
	 * @throws CompilerError a custom compiler error
	 * @throws IOException an error caused from the scanner
	 */
	private static void isValidGoal() throws CompilerError, IOException {

		if (sourcecode.current() == null)
			throw new CompilerError("Source code is empty");

		program();

		if (sourcecode.current() != null)
			throw new CompilerError("Source code has contents after the program end");
	}

	//region Program
	//@formatter:off
	/**
	 * Checks to make sure the current program is a valid program
	 * <p>
	 * Checks to make sure the program is {@literal =}{@literal =} validHeader, validBody, {@literal"}period{@literal"}
	 * </p>
	 * <br>
	 * aka {@literal <}program{@literal >} ::= {@literal <}program_header{@literal >} {@literal <}program_body{@literal >}
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void program() throws IOException, CompilerError
	{
		out.startProc(programHeader());

		programBody();

		out.endProc(true);

		if (! sourcecode.next().equals(specType.PERIOD))
			throw new CompilerError("Parser is expecting " + specType.PERIOD.shortHand(), sourcecode.current().getLineNum());

	}

	//@formatter:off
	/**
	 * Checks to make sure the current program is a valid program header
	 * <p>
	 * Checks to make sure the program is {@literal =}{@literal =} {@literal"}program{@literal"}, id, {@literal"}program{@literal"}
	 * </p>
	 * <br>
	 * aka {@literal :} {@literal <}program_header{@literal >} ::= program {@literal <}identifier{@literal >} is
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 * @return the sin containing the program
	 *///@formatter:on
	private static sin programHeader() throws IOException, CompilerError {

		if (! sourcecode.next().equals(groupType.PROGRAM))
			throw new CompilerError("Parser is expecting " + groupType.PROGRAM.shortHand(), sourcecode.current().getLineNum());

		if (! sourcecode.next().equals(groupType.ID))
			throw new CompilerError("Parser is expecting " + groupType.ID.shortHand(), sourcecode.current().getLineNum());

		if (! sourcecode.next().equals(groupType.IS))
			throw new CompilerError("Parser is expecting " + groupType.IS.shortHand(), sourcecode.current().getLineNum());

		return new sin("main");
	}

	//@formatter:off
	/**
	 * Checks to make sure the current program is a valid program body
	 * <p>
	 * Checks to make sure the program is {@literal =}{@literal =} {@literal"}begin{@literal"}, id, {@literal"}end{@literal"} {@literal"}program{@literal"}
	 * <br>
	 * {@literal <}program_body{@literal >} {@literal :}{@literal :}{@literal =} {@literal (} {@literal <}declaration{@literal >} {@literal ;} {@literal )}{@literal *} begin {@literal (} {@literal <}statement{@literal >} {@literal ;} {@literal )}{@literal *} end program
	 * </p>
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void programBody() throws IOException, CompilerError {
		// if Declaration
		while (isDeclaration()) {
			int lineNum=sourcecode.current().getLineNum();
			declaration();

			//Attempt to go forward by not continuing if the current token isn't a semicolon
			if (!sourcecode.current().equals(specType.SEMICOLON))
				runningErrors.add(new CompilerError("Parsing Error within the Program Body",lineNum));
			else
				sourcecode.next();
		}

		if (! sourcecode.next().equals(groupType.BEGIN))
			throw new CompilerError("Parser is expecting " + groupType.BEGIN.shortHand(), sourcecode.current().getLineNum());

		while (isStatement()) {
			int lineNum=sourcecode.current().getLineNum();
			statement();

			//Attempt to go forward by not continuing if the current token isn't a semicolon
			if (!sourcecode.current().equals(specType.SEMICOLON))
				runningErrors.add(new CompilerError("Parsing Error within the Program Body",lineNum));
			else
				sourcecode.next();
		}

		if (! sourcecode.next().equals(groupType.END))
			throw new CompilerError("Parser is expecting " + groupType.END.shortHand(), sourcecode.current().getLineNum());

		if (! sourcecode.next().equals(groupType.PROGRAM))
			throw new CompilerError("Parser is expecting " + groupType.PROGRAM.shortHand(), sourcecode.current().getLineNum());

	}
	//endregion

	//region Procedure
	//@formatter:off
	/**
	 * {@literal <}procedure_declaration{@literal >}
	 * {@literal :}{@literal :}{@literal =}{@literal <}procedure_header{@literal >}
	 * {@literal <}procedure_body{@literal >}
	 * @param isGlobal an indicator checking to see if the procedure is global
	 *
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void procedure(boolean isGlobal) throws IOException, CompilerError {
		out.startProc(procedureHeader(isGlobal));

		procedureBody();
		exit_scope();
		out.endProc(false);
	}

	//@formatter:off
	/**
	 * {@literal <}procedure_header{@literal >} {@literal :}{@literal :}
	 * {@literal =} procedure {@literal <}identifier{@literal >}{@literal (}
	 * {@literal [}{@literal <}parameter_list{@literal >}{@literal ]} {@literal )}
	 *
	 * @param isGlobal the indicator for whether the proceddure is global or not
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 * @return the sin containing the procedurel
	 *///@formatter:on
	private static sin procedureHeader(boolean isGlobal) throws IOException, CompilerError {
		sin currentProc;
		if (! sourcecode.next().equals(groupType.PROCEDURE))
			throw new CompilerError("Parser is expecting " + groupType.PROCEDURE, sourcecode.current().getLineNum());

		if (! sourcecode.current().equals(groupType.ID))
			throw new CompilerError("Parser is expecting " + groupType.ID.shortHand(), sourcecode.current().getLineNum());
		else {
			currentProc=new sin(sourcecode.current().getTokenVal());
			currentProc.setIsGlobal(isGlobal);

			enter_scope(currentProc, sourcecode.current().getLineNum());
			sourcecode.next();
		}

		if (! sourcecode.next().equals(specType.PAREN_ST))
			throw new CompilerError("Parser is expecting " + specType.PAREN_ST.shortHand(), sourcecode.current().getLineNum());

		if (sourcecode.current().equals(groupType.TYPE_MARK))
			parameterList();

		if (! sourcecode.next().equals(specType.PAREN_ND))
			throw new CompilerError("Parser is expecting " + specType.PAREN_ND.shortHand(), sourcecode.current().getLineNum());

		return currentProc;
	}


	//@formatter:off
	/**
	 * {@literal <}procedure_body{@literal >} {@literal :}{@literal :}{@literal =} {@literal (} {@literal <}declaration{@literal >} {@literal ;} {@literal )}{@literal *} begin {@literal (} {@literal <}statement{@literal >} {@literal ;} {@literal )}{@literal *} end procedure
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void procedureBody() throws IOException, CompilerError {
		// if Declaration
		while (isDeclaration()) {
			int lineNum=sourcecode.current().getLineNum();
			declaration();

			//Attempt to go forward by not continuing if the current token isn't a semicolon
			if (!sourcecode.current().equals(specType.SEMICOLON))
				runningErrors.add(new CompilerError("Parsing Error within the Procedure Body",lineNum));
			else
				sourcecode.next();
		}

		if (! sourcecode.next().equals(groupType.BEGIN))
			throw new CompilerError("Parser is expecting " + groupType.BEGIN.shortHand(), sourcecode.current().getLineNum());

		while (isStatement()) {
			int lineNum=sourcecode.current().getLineNum();
			statement();

			//Attempt to go forward by not continuing if the current token isn't a semicolon
			if (!sourcecode.current().equals(specType.SEMICOLON))
				runningErrors.add(new CompilerError("Parsing Error within the Program Body",lineNum));
			else
				sourcecode.next();
		}

		if (! sourcecode.next().equals(groupType.END))
			throw new CompilerError("Parser is expecting " + groupType.END.shortHand(), sourcecode.current().getLineNum());

		if (! sourcecode.next().equals(groupType.PROCEDURE))
			throw new CompilerError("Parser is expecting " + groupType.PROCEDURE.shortHand(), sourcecode.current().getLineNum());

	}
	//endregion

	//region Declaration
	//@formatter:off
	/**
	 * A quick method to peak for multiple variations of declarations
	 *
	 * @return boolean an indicator checking to see if the current token is a declaration
	 * @throws IOException the exception created from the scanner
	 *///@formatter:on
	private static boolean isDeclaration() throws IOException {
		if (sourcecode.current().equals(groupType.GLOBAL) || sourcecode.current().equals(groupType.TYPE_MARK)
				|| sourcecode.current().equals(groupType.PROCEDURE))
			return true;
		return false;
	}

	//@formatter:off
	/**
	 * {@literal <}declaration{@literal >} {@literal :}{@literal :}{@literal =} 	[ global ] {@literal <}procedure_declaration{@literal >}
	 * 					{@literal |}	[ global ] {@literal <}variable_declaration{@literal >}
	 *
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void declaration() throws IOException, CompilerError {
		boolean isGlobal = false;
		if (sourcecode.current().equals(groupType.GLOBAL)) {
			sourcecode.next();
			isGlobal = true;
		}

		if (sourcecode.current().equals(groupType.TYPE_MARK)) variableDeclaration(isGlobal, false);
		else if (sourcecode.current().equals(groupType.PROCEDURE)) procedure(isGlobal);

	}

	/**
	 * @param isGlobal an indicator for if the variable is a global variable
	 * @param isParam an indicator for if the current variable is a parameter
	 *
	 * @return String the name of the declaration
	 *
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 */
	private static String variableDeclaration(boolean isGlobal, boolean isParam) throws IOException, CompilerError {
		sin workingSin = null;


		//region CreatingSin
		if (! sourcecode.current().equals(groupType.TYPE_MARK)) {
			throw new CompilerError("Parser is expecting " + groupType.TYPE_MARK.shortHand(), sourcecode.current().getLineNum());
		}
		else {
			workingSin = new sin(sourcecode.current());
			workingSin.setLineNumber(sourcecode.current().getLineNum());

			workingSin.setIsGlobal(isGlobal);

			sourcecode.next();
		}

		if (! sourcecode.current().equals(groupType.ID)) {
			throw new CompilerError("Parser is expecting " + groupType.ID.shortHand(), sourcecode.current().getLineNum());
		}
		else {
			workingSin.setName(sourcecode.current().getTokenVal());
			sourcecode.next();
		}

		if (sourcecode.current().equals(groupType.CAPSULE)) {
			if (! sourcecode.next().equals(specType.BRACK_ST))
				throw new CompilerError("Parser is expecting " + specType.BRACK_ST.shortHand(), sourcecode.current().getLineNum());

			boolean Min = false;
			if (sourcecode.current().equals(specType.MIN)) {
				Min = true;
				sourcecode.next();
			}

			if (! sourcecode.current().equals(specType.INTEGER))
				throw new CompilerError("Parser is expecting " + specType.INTEGER.shortHand(), sourcecode.current().getLineNum());
			else {
				workingSin.setLowerbound(Min ? (- 1 * Float.valueOf(sourcecode.current().getTokenVal()))
						: Float.valueOf(sourcecode.current().getTokenVal()));
				Min = false;
				sourcecode.next();
			}

			if (! sourcecode.current().equals(specType.COLON)) {
				throw new CompilerError("Parser is expecting " + specType.COLON.shortHand(), sourcecode.current().getLineNum());
			}
			else sourcecode.next();

			if (sourcecode.current().equals(specType.MIN)) {
				Min = true;
				sourcecode.next();
			}

			if (! sourcecode.current().equals(specType.INTEGER))
				throw new CompilerError("Parser is expecting " + specType.INTEGER.shortHand(), sourcecode.current().getLineNum());
			else {
				workingSin.setUpperbound(Min ? (- 1 * Float.valueOf(sourcecode.current().getTokenVal()))
						: Float.valueOf(sourcecode.current().getTokenVal()));
				Min = false;
				sourcecode.next();
			}

			if (! sourcecode.next().equals(specType.BRACK_ND)) throw new CompilerError(
					"Parser is expecting " + groupType.ID.shortHand() + " or " + specType.INTEGER.shortHand(),
					sourcecode.current().getLineNum());

			workingSin.getType().setArray(true);
		}
		//endregion

		if (table.containsKey(workingSin.getName()) || table.getGlobalVariables().contains(workingSin.getName()))
			throw new CompilerError("Cannot redeclare variable "+workingSin.getName()+" at line "+workingSin.getLineNumber());

		if (workingSin.getIsGlobal()) table.putGlobal(workingSin);
		else table.put(workingSin.getName(), workingSin);

		if (!isParam)
			out.generateDeclare(workingSin);

		if (isParam) return workingSin.getName();
		else return null;
	}
	//endregion

	//region Parameter
	//@formatter:off
	/**
	 * {@literal <}parameter_list{@literal >} {@literal :}{@literal :}{@literal =}		{@literal <}parameter{@literal >} , {@literal <}parameter_list{@literal >}
	 *						{@literal |}	{@literal <}parameter{@literal >}
	 *
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void parameterList() throws IOException, CompilerError {
		parameter();

		if (sourcecode.current().equals(specType.COMMA)) {
			sourcecode.next();
			parameterList();
		}
	}

	//@formatter:off
	/**
	 * {@literal <}parameter{@literal >} {@literal :}{@literal :}{@literal =}{@literal <}variable_declaration{@literal >} {@literal (}in {@literal |} out {@literal |} inout{@literal )}
	 *
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void parameter() throws IOException, CompilerError {
		String workingParam = variableDeclaration(false, true);

		if (! sourcecode.current().equals(groupType.PARAM))
			throw new CompilerError("Parser is expecting " + groupType.PARAM.shortHand(), sourcecode.current().getLineNum());
		else {
			table.addParam(workingParam, sourcecode.current().getTokenType().getParam());
			sourcecode.next();
		}

	}

	//@formatter:off
	/**
	 * {@literal <}argument_list{@literal >} {@literal :}{@literal :}{@literal =}	{@literal <}expression{@literal >} , {@literal <}argument_list{@literal >}
	 *					{@literal |}	{@literal <}expression{@literal >}
	 * TODO {@literal -} type check on this
	 * @param method the name of the method
	 * @param argcount the current argument counter, used as an iterator
	 * @param arguments the total list of arguments
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void argumentList(String method, int argcount, ArrayList<sin> arguments,ArrayList<sinType.paramType> paramTypes) throws IOException, CompilerError {
		sin curTable;
		if ((curTable = table.get(method)) == null || curTable.getTable() == null)
			throw new CompilerError("Method " + method + " is not available.", sourcecode.current().getLineNum());

		if (argcount >= curTable.getTable().getParams().size())
			throw new CompilerError("Method " + method + " does not have a " + argcount + " parameter.");

		sin curType;
		String curParam = curTable.getTable().getParams().get(argcount);
		paramTypes.add(table.get(method).getTable().get(curParam).getType().getParam());

		if (! table.get(method).getTable().get(curParam).getType().conversionHappy((curType = expression()).getType(),false))
			throw new CompilerError("ArgumentType : " + curType.getType().getType().shortHand() + " is not applicable for "
					+ curTable.getTable().get(curParam), sourcecode.current().getLineNum());

		arguments.add(curType);
		if (sourcecode.current().equals(specType.COMMA)) {
			sourcecode.next();
			argumentList(method, argcount++,arguments,paramTypes);
		}
	}
	//endregion

	//region Statement
	//@formatter:off
	/**
	 * A quick method to peak for multiple variations of statements
	 *
	 * @return boolean an indication as to whether the current token is a statement
	 * @throws IOException the exception created from the scanner
	 *///@formatter:on
	private static boolean isStatement() throws IOException {
		// IF - FOR - RETURN
		if (sourcecode.current().equals(specType.key_IF) || sourcecode.current().equals(groupType.LOOP)
				|| sourcecode.current().equals(groupType.RETURN) || sourcecode.current().equals(groupType.ID)
				|| sourcecode.current().equals(groupType.BUILTIN)) {
			return true;
		}

		return false;
	}

	//@formatter:off
	/**
	 * {@literal <}statement{@literal >} {@literal :}{@literal :}{@literal =}	{@literal <}assignment_statement{@literal >}
	 *		<br>&emsp;{@literal |}	{@literal <}if_statement{@literal >}
	 *		<br>&emsp;{@literal |}	{@literal <}loop_statement{@literal >}
	 *		<br>&emsp;{@literal |}	{@literal <}return_statement{@literal >}
	 *		<br>&emsp;{@literal |}	{@literal <}procedure_call{@literal >}
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void statement() throws IOException, CompilerError {
		if (sourcecode.current().equals(groupType.RETURN)) returnStatement();

		else if (sourcecode.current().equals(groupType.IF)) ifStatement();

		else if (sourcecode.current().equals(groupType.LOOP)) loopStatement();

		else if (sourcecode.current().equals(groupType.ID) || sourcecode.current().equals(groupType.BUILTIN)) {
			if (sourcecode.peak().equals(specType.PAREN_ST)) {
				String methodName = sourcecode.current().getTokenVal();
				sourcecode.next();
				procedureCall(methodName);
			}
			else if (sourcecode.peak().equals(specType.BRACK_ST) || sourcecode.peak().equals(specType.ASSIGN)) {
				assignmentStatement();
			}
		}
		else throw new CompilerError("Source code is expecting a valid statement", sourcecode.current().getLineNum());
	}

	//@formatter:off
	/**
	 * {@literal <}assignment_statement{@literal >} {@literal :}{@literal :}{@literal =} {@literal <}destination{@literal >} {@literal :}{@literal =} {@literal <}expression{@literal >}
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void assignmentStatement() throws IOException, CompilerError {
		sin lhand = destination();

		if (! sourcecode.next().equals(specType.ASSIGN))
			throw new CompilerError("Parser is expecting " + specType.ASSIGN.shortHand(), sourcecode.current().getLineNum());

		sin rhand = expression();
		if (! lhand.getType().conversionHappy(rhand.getType(),true))// TODO {@literal -} Make sure to have NUMBER able to go to INT/FLOAT &&
			// INT/BOOL
			throw new CompilerError(
					"Parser cannot assign type of " + rhand.getType().getType().name() + " to " + lhand.getType().getType().name()
							+ "; ie: " + lhand.getType().getType().name() + " = " + rhand.getType().getType().name(),
					sourcecode.current().getLineNum());

		//TODO - CODEGEN
		//out.generateAssign(lhand,rhand);
	}

	//@formatter:off
	/**
	 * if {@literal (} {@literal <}expression{@literal >} {@literal )} then {@literal (} {@literal <}statement{@literal >} {@literal ;} {@literal )}{@literal +} {@literal [} else {@literal (} {@literal <}statement{@literal >} {@literal ;} {@literal )}{@literal +} {@literal ]} end if
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void ifStatement() throws IOException, CompilerError {
		if (! sourcecode.next().equals(specType.key_IF))
			throw new CompilerError("Parser is expecting " + specType.key_IF.shortHand(), sourcecode.current().getLineNum());

		if (! sourcecode.next().equals(specType.PAREN_ST))
			throw new CompilerError("Parser is expecting " + specType.PAREN_ST.shortHand(), sourcecode.current().getLineNum());

		relation();

		if (! sourcecode.next().equals(specType.PAREN_ND))
			throw new CompilerError("Parser is expecting " + specType.PAREN_ND.shortHand(), sourcecode.current().getLineNum());

		if (! sourcecode.next().equals(specType.key_THEN))
			throw new CompilerError("Parser is expecting " + specType.key_THEN.shortHand(), sourcecode.current().getLineNum());

		isStatement();

		while (isStatement()) {
			statement();

			if (! sourcecode.next().equals(specType.SEMICOLON))
				throw new CompilerError("Parser is expecting " + specType.SEMICOLON.shortHand(),
						sourcecode.current().getLineNum());
		}

		if (sourcecode.current().equals(specType.key_ELSE)) {
			sourcecode.next();

			while (isStatement()) {
				statement();

				if (! sourcecode.next().equals(specType.SEMICOLON))
					throw new CompilerError("Parser is expecting " + specType.SEMICOLON.shortHand(),
							sourcecode.current().getLineNum());
			}
		}

		if (! sourcecode.next().equals(specType.key_END))
			throw new CompilerError("Parser is expecting " + specType.key_END.shortHand(), sourcecode.current().getLineNum());

		if (! sourcecode.next().equals(specType.key_IF))
			throw new CompilerError("Parser is expecting " + specType.key_IF.shortHand(), sourcecode.current().getLineNum());

	}

	//@formatter:off
	/**
	 * for {@literal (} {@literal <}assignment_statement{@literal >} {@literal ;} {@literal <}expression{@literal >} {@literal )} {@literal (} {@literal <}statement{@literal >} {@literal ;} {@literal )}{@literal *} end for
	 * {@literal *}
	 * TODO {@literal -} Evaluate the boolean expression
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void loopStatement() throws IOException, CompilerError {
		if (! sourcecode.next().equals(specType.key_FOR))
			throw new CompilerError("Parser is expecting " + specType.key_FOR.shortHand(), sourcecode.current().getLineNum());

		if (! sourcecode.next().equals(specType.PAREN_ST))
			throw new CompilerError("Parser is expecting " + specType.PAREN_ST.shortHand(), sourcecode.current().getLineNum());

		assignmentStatement();

		if (! sourcecode.next().equals(specType.SEMICOLON))
			throw new CompilerError("Parser is expecting " + specType.SEMICOLON.shortHand(), sourcecode.current().getLineNum());

		relation();

		if (! sourcecode.next().equals(specType.PAREN_ND))
			throw new CompilerError("Parser is expecting " + specType.PAREN_ND.shortHand(), sourcecode.current().getLineNum());

		while (isStatement()) {
			statement();

			if (! sourcecode.next().equals(specType.SEMICOLON))
				throw new CompilerError("Parser is expecting " + specType.SEMICOLON.shortHand(),
						sourcecode.current().getLineNum());
		}

		if (! sourcecode.next().equals(specType.key_END))
			throw new CompilerError("Parser is expecting " + specType.key_END.shortHand(), sourcecode.current().getLineNum());

		if (! sourcecode.next().equals(specType.key_FOR))
			throw new CompilerError("Parser is expecting " + specType.key_FOR.shortHand(), sourcecode.current().getLineNum());

	}

	//@formatter:off
	/**
	 * Only looks for RETURN ?
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static void returnStatement() throws IOException, CompilerError {
		if (! sourcecode.next().equals(specType.key_RETURN))
			throw new CompilerError("Parser is expecting " + specType.key_RETURN.shortHand(), sourcecode.current().getLineNum());

	}

	//@formatter:off
	/**
	 * {@literal <}procedure_call{@literal >} {@literal :}{@literal :}{@literal =}{@literal <}identifier{@literal >} {@literal (} {@literal [}{@literal <}argument_list{@literal >}{@literal ]} {@literal )}
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 * @param method the name of the procedure
	 *///@formatter:on
	private static void procedureCall(String method) throws IOException, CompilerError
	{
		ArrayList<sin> args=new ArrayList<sin>();
		ArrayList<sinType.paramType> paramTypes=new ArrayList<sinType.paramType>();
		int lineNumber=0;

		if (! sourcecode.current().equals(specType.PAREN_ST))
			throw new CompilerError("Parser is expecting " + specType.PAREN_ST.shortHand(), sourcecode.current().getLineNum());
		else
		{
			sourcecode.next();
			lineNumber=sourcecode.current().getLineNum();
		}
		if (! sourcecode.current().equals(specType.PAREN_ND))
			argumentList(method, 0,args,paramTypes);
		else if (!table.getGlobal().get(method).getTable().getParams().isEmpty())
			throw new CompilerError("Method "+method+" requires arguments",sourcecode.current().getLineNum());

		if (! sourcecode.next().equals(specType.PAREN_ND))
			throw new CompilerError("Parser is expecting " + specType.PAREN_ND.shortHand(), sourcecode.current().getLineNum());

		out.generateMethodCall(method,args,paramTypes,lineNumber);
	}

	//@formatter:off
	/**
	 * {@literal <}destination{@literal >} {@literal :}{@literal :}{@literal =}{@literal <}identifier{@literal >} {@literal [} {@literal [} {@literal <}expression{@literal >} {@literal ]} {@literal ]}
	 * TODO {@literal -} ArrayChecking here
	 * @return the sin used
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static sin destination() throws IOException, CompilerError {
		String lhand = "";
		if (! sourcecode.current().equals(specType.ID))
			throw new CompilerError("Parser_decl1 is expecting " + specType.ID.shortHand(), sourcecode.current().getLineNum());
		else {
			lhand = sourcecode.current().getTokenVal();
			sourcecode.next();
		}

		if (sourcecode.current().equals(specType.BRACK_ST)) {
			sourcecode.next();
			expression();

			if (! sourcecode.next().equals(specType.BRACK_ND))
				throw new CompilerError("Parser_decl2 is expecting " + specType.BRACK_ND.shortHand(),
						sourcecode.current().getLineNum());
		}
		return table.get(lhand);
	}
	//endregion

	//region ExpressionHandling
	//@formatter:off
	/**
	 * {@literal <}expression{@literal >} {@literal :}{@literal :}{@literal =}	{@literal <}expression{@literal >} {@literal &} {@literal <}arithOp{@literal >}
	 *				<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}expression{@literal >} {@literal |} {@literal <}arithOp{@literal >}
	 *				<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal [} not {@literal ]} {@literal <}arithOp{@literal >}
	 * TODO {@literal -} need to ReAssign Types of IDs based on their type marks
	 * @return the sin used
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static sin expression() throws IOException, CompilerError {
		boolean not = false;
		if (sourcecode.current().equals(specType.NOT)) {
			sourcecode.next();
			not = true;
		}

		sin lhand = arithOp();
		if (not) {
			if (! lhand.getType().equals(specType.INTEGER) || ! lhand.getType().equals(specType.BOOL))
				throw new CompilerError("Parser cannot NOT on a " + lhand.getType().getType().name(), sourcecode.current().getLineNum());
		}

		if (sourcecode.current().equals(groupType.BITWISE)) {
			sourcecode.next();
			sin rhand = expression();
			if (! lhand.getType().conversionHappy(rhand.getType(),false))
				throw new CompilerError("Parser_expression cannot " + groupType.BITWISE.shortHand() + " between "
						+ lhand.getType().getType().name() + " and " + rhand.getType().getType().name(), sourcecode.current().getLineNum());
		}
		return lhand;
	}

	//@formatter:off
	/**
	 * {@literal <}arithOp{@literal >} {@literal :}{@literal :}{@literal =}	{@literal <}arithOp{@literal >} {@literal +} {@literal <}relation{@literal >}
	 *				<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}arithOp{@literal >} {@literal -} {@literal <}relation{@literal >}
	 *				<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}relation{@literal >}
	 * @return the sin used
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static sin arithOp() throws IOException, CompilerError {
		sin lhand = relation();

		if (sourcecode.current().equals(groupType.ARITHOP)) {
			sourcecode.next();
			sin rhand = arithOp();
			if (! lhand.getType().conversionHappy(rhand.getType(),false))
				throw new CompilerError("Parser_arithop cannot " + groupType.ARITHOP.shortHand() + " between "
						+ lhand.getType().getType().name() + " and " + rhand.getType().getType().name(), sourcecode.current().getLineNum());
		}

		return lhand;
	}

	//@formatter:off
	/**
	 * {@literal <}relation{@literal >} ::{@literal =} 	{@literal <}relation{@literal >} {@literal <} {@literal <}term{@literal >}
	 *				<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}relation{@literal >} {@literal >}{@literal =} {@literal <}term{@literal >}
	 *				<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}relation{@literal >} {@literal <}{@literal =} {@literal <}term{@literal >}
	 *				<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}relation{@literal >} {@literal >} {@literal <}term{@literal >}
	 *				<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}relation{@literal >} {@literal =}{@literal =} {@literal <}term{@literal >}
	 *				<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}relation{@literal >} {@literal !}{@literal =} {@literal <}term{@literal >}
	 *				<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}term{@literal >}
	 * @return the sin used
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static sin relation() throws IOException, CompilerError {
		sin lhand = term();

		if (sourcecode.current().equals(groupType.COND)) {
			sourcecode.next();
			sin rhand = relation();
			if (! lhand.getType().conversionHappy(rhand.getType(),false))
				throw new CompilerError("Parser_relation cannot " + groupType.COND.shortHand() + " between "
						+ lhand.getType().getType().name() + " and " + rhand.getType().getType().name(), sourcecode.current().getLineNum());
		}
		return lhand;
	}

	//@formatter:off
	/**
	 * {@literal <}term{@literal >} {@literal :}{@literal :}{@literal =}{@literal <}term{@literal >} {@literal *} {@literal <}factor{@literal >}
	 *			<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}term{@literal >} / {@literal <}factor{@literal >}
	 *			<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}factor{@literal >}
	 * @return the sin used
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static sin term() throws IOException, CompilerError {
		sin lhand = factor();

		if (sourcecode.current().equals(groupType.CALC)) {
			sourcecode.next();
			sin rhand = term();

			if (! lhand.getType().conversionHappy(rhand.getType(),false))
				throw new CompilerError("Parser_term cannot " + groupType.CALC.shortHand() + " between "
						+ lhand.getType().getType().name() + " and " + rhand.getType().getType().name(), sourcecode.current().getLineNum());
		}
		return lhand;
	}

	//@formatter:off
	/**
	 * {@literal <}factor{@literal >} {@literal :}{@literal :}{@literal =} {@literal (} {@literal <}expression{@literal >} {@literal )}
	 *			<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal [} {@literal -} {@literal ]} {@literal <}name{@literal >}
	 *			<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal [} {@literal -} {@literal ]} {@literal <}number{@literal >}
	 *			<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}string{@literal >}
	 *			<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	{@literal <}char{@literal >}
	 *			<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	true
	 *			<br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;{@literal |}	false
	 * @return the sin used
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 *///@formatter:on
	private static sin factor() throws IOException, CompilerError {
		sin curType;
		boolean Min = false;

		if (sourcecode.current().equals(specType.MIN)) {
			Min = true;
			sourcecode.next();
		}

		//region Expression
		if (sourcecode.current().equals(specType.PAREN_ST)) {
			sourcecode.next();

			curType = expression();

			if (! sourcecode.next().equals(specType.PAREN_ND))
				throw new CompilerError("Parser_factor_1 is expecting " + specType.PAREN_ND.shortHand(),
						sourcecode.current().getLineNum());
		}
		else if (sourcecode.current().equals(groupType.TYPE_MARK)) {
			if (Min && (sourcecode.current().equals(specType.STRING) || sourcecode.current().equals(specType.BOOL)
					|| sourcecode.current().equals(specType.CHAR)))
				throw new CompilerError("Parser_factor_2 cannot have a negative value with " + sourcecode.current(),
						sourcecode.current().getLineNum());


			curType = new sin("RAW",sourcecode.current().getTokenType());
			curType.setVal(sourcecode.current().getTokenVal());

			//TODO - CODEGEN
			/*
			IValue curValue=null;
			switch(sourcecode.current().getTokenType().getType())
			{
				case BOOL:
					curValue=ValueCompiler.from(sourcecode.current().getTokenVal().equalsIgnoreCase("true")?1:0);
					break;
				case INTEGER:
					curValue=ValueCompiler.from(Integer.parseInt(sourcecode.current().getTokenVal()));
					break;
				case FLOAT:
					System.out.println("Float Value : "+Float.valueOf(sourcecode.current().getTokenVal()));
					System.out.println("Float Value : "+Float.valueOf(sourcecode.current().getTokenVal()).byteValue());
					System.out.println("Float Value : "+Float.valueOf(sourcecode.current().getTokenVal()).hashCode());
					System.out.println("Float Value : "+Float.toHexString(Float.valueOf(sourcecode.current().getTokenVal())));

					curValue=ValueCompiler.from(Float.valueOf(sourcecode.current().getTokenVal()));
					break;
				case STRING:
					curValue=ValueCompiler.from(sourcecode.current().getTokenVal());
					break;
				case CHAR:
					curValue=ValueCompiler.from(sourcecode.current().getTokenVal().charAt(1));
					break;
			}
			curType.setVal(curValue);
			*/

			sourcecode.next();
		}
		else if (sourcecode.current().equals(groupType.ID)) // NAME
		{
			curType = name();
		}
		else throw new CompilerError("Parser_factor_3 is expecting a valid factor", sourcecode.current().getLineNum());
		//endregion


		return curType;
	}

	//@formatter:off
	/**
	 * {@literal <}name{@literal >} {@literal :}{@literal :}{@literal =} {@literal <}identifier{@literal >} {@literal [} {@literal [} {@literal <}expression{@literal >} {@literal ]} {@literal ]}
	 * @return the sin used
	 * @throws IOException the exception created from the scanner
	 * @throws CompilerError a custom error
	 * TODO {@literal -} Be Able to retrieve array index...
	 *///@formatter:on
	private static sin name() throws IOException, CompilerError {
		sin curType;

		if (! sourcecode.current().equals(specType.ID))
			throw new CompilerError("Parser_name_1 is expecting " + specType.ID.shortHand(), sourcecode.current().getLineNum());
		else curType = table.get(sourcecode.next().getTokenVal());

		// if (!(curType =
		// table.get(sourcecode.next().getTokenVal()).getType()).equals(specType.ID))
		// throw new CompilerError("Parser_name_1 is expecting " +
		// specType.ID.shortHand(), sourcecode.current().getLineNum());

		if (sourcecode.current().equals(specType.BRACK_ST)) {
			sourcecode.next();

			sin kurType = expression();

			if (! kurType.getType().equals(specType.INTEGER))
				throw new CompilerError("Parser_name_2 is expecting " + specType.INTEGER.shortHand(),
						sourcecode.current().getLineNum());


			if (! sourcecode.next().equals(specType.BRACK_ND))
				throw new CompilerError("Parser_name_3 is expecting " + specType.BRACK_ND.shortHand(),
						sourcecode.current().getLineNum());
		}
		return curType;
	}
	//endregion

	//region TableHandling

	/**
	 * @param workingSin the current sin containing the program
	 * @param integer the line number of the procedure
	 */
	private static void enter_scope(sin workingSin, Integer integer) {
		table = table.enter_scope(workingSin, integer);
	}

	/**
	 * exiting the current table and going to the parent
	 */
	private static void exit_scope() {
		table = table.exit_scope();
	}
	//endregion
	//endregion

}
