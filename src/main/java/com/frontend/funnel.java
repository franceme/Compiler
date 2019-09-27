package com.frontend;

import java.util.ArrayList;

import com.middleend.frontendHandler;
import com.frontend.overhead.CompilerError;
import com.frontend.overhead.arguments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * <p>funnel class.</p>
 *
 * @author franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 *
 * The starting point of the program.
 * The delegation of the program starts here, this creates the arguments class than passes it off to the
 * 	frontend handler.
 */
public class funnel {

	//Used throughout the project
	private static arguments args;

	/**
	 * This is the entry of my Translator.
	 *
	 * @param in arguments passed in via command line
	 */
	public static void main(String[] in)  {
		
		//Setting the arguments from the given string
		args=new arguments(in,arguments.resources.custommade,"test_program_minimal.src");

		//region DEBUG
		if (args.isDebug())
		{
			args.setShowAll(false);
			args.setRunTest(true);

				if (args.isShowAll())
					System.out.println("Analyzing file : " + args.getBareFileName());

				ArrayList<CompilerError> errors = frontendHandler.frontend(args);

				if (errors.size() == 0) {
					if (args.isDebug())
						System.out.println("Compilation was Successful");
				} else //Printing out all of the Compiler Errors
					errors.stream().forEach(System.out::println);
		}
		//endregion
		//region REAL
		else {
			ArrayList<CompilerError> errors = frontendHandler.frontend(args);
			
			if (errors.size() == 0) {
					System.out.println("Compilation was Successful");
			} else //Printing out all of the Compiler Errors
				errors.stream().forEach(System.out::println);
		}
		//endregion
	}

}
