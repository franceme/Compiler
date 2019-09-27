/**
 * Name: franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 * DateCreated: 2018-01-18
 */
package com.middleend;

import java.io.IOException;
import java.util.ArrayList;

import com.middleend.parser.parsertongue;
import com.frontend.overhead.CompilerError;
import com.frontend.overhead.arguments;
import com.middleend.parser.sin.sinTable;

/**
 * <p>frontendHandler class.</p>
 *
 * @author franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 *
 * The handling of the verification of the source file, as well as calling the parser.
 * This class will also handle whatever the parser returns with.
 */
public class frontendHandler {

	//Arguments pulled from the start
	private static arguments args;

	//region Methods
	/**
	 * This method handles the delegation of the parsing after verifying
	 * 	the file.
	 *
	 * @param argz {@literal -} the arguments class created from the start of the program
	 * @return {@literal -} full symbol table
	 */
	@SuppressWarnings ("unchecked")
	public static ArrayList<CompilerError> frontend(arguments argz) {

		ArrayList<CompilerError> errors=new ArrayList<CompilerError>();

		args=argz;

		try{
			verifyFile();

			Object result=parsertongue.parse(args);

			//Checking if there were any errors returned, add them into the result
			if (!(result instanceof sinTable))
				errors.addAll((ArrayList<CompilerError>) result);
			else { //Handling of the sinTable?
				sinTable resultant=(sinTable)result;
				resultant.size();
			}
		}
		catch(IOException io)
		{
			errors.add(new CompilerError("IO Exception Thrown: "+io.getLocalizedMessage()));
		}
		catch (CompilerError ce)
		{
			errors.add(ce);
		}
		catch (Exception e)
		{
			errors.add(new CompilerError("Unhandled Error: "+e.getLocalizedMessage()));
		}

		return errors;
	}

	/**
	 * This method handles the verification of the file.
	 * 		It will throw an error if:
	 * 			the file doesn't exist or if it is a directory
	 * 			the file is hidden or can't be read
	 * 			the file doesn't end with the .src extension
	 *
	 * @throws CompilerError based on whether the file exists or the file ends with the correct extension
	 */
	private static void verifyFile() throws CompilerError
	{
		if (!args.getSourceFile().exists() || args.getSourceFile().isDirectory())
			throw new CompilerError("File " + args.getFileName() + " does not exist.");

		if (args.getSourceFile().isHidden() || !args.getSourceFile().canRead())
			throw new CompilerError("File "+args.getFileName()+" cannot be read.");

		if (! args.getFileExt().equals("src"))
			throw new CompilerError("File " +args.getFileName() + " does not end with the correct extension.");

	}
	//endregion

}
