/**
 * Name: franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 * DateCreated: 2018-01-20
 */
package com.frontend.overhead;

import java.io.File;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>arguments class.</p>
 *
 * @author franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 *
 * This class contains all of the pertiniant global information.
 * It contains the source file being read from, a determination about the os, and several accessor methods
 * 		on the source file.
 */
public class arguments {

	//region ClassFields
	@Getter
	private final boolean isLinux= !System.getProperty("os.name").contains("Windows");
	@Getter
	@Setter
	private File sourceFile;
	@Getter
	private final String debugFiles= System.getProperty("user.dir") + (isLinux ? "/src/test/resources/" : "\\src\\test\\resources\\");
	@Getter
	private final String fileSplit = isLinux ? "/" : "\\";
	@Getter
	@Setter
	private boolean showAll;
	@Getter
	@Setter
	private boolean runTest;
	@Getter
	private final boolean debug;
	@Getter
	private final String[] periodDelimitedPath;
	public enum resources{correct,custommade,incorrect}
	//endregion


	/**
	 * Creates the arguments container from the arguments given
	 *
	 * @param input the arguments from the command line
	 */
	public arguments(String[] input,resources resource,String fileName)
	{
		if (input.length==1)
		{
			debug=false;
			this.setSourceFile(new File(input[0]));
		}
		else {
			this.setSourceFile(new File(this.getDebugFiles() + this.getFileSplit() + resource + this.getFileSplit() + fileName));
			debug = true;
		}
		this.periodDelimitedPath=this.sourceFile.getAbsoluteFile().toString().split(Pattern.quote("."));
	}


	//region PublicMethods
	//region CommandLine Headings
	/**
	 * <p>start.</p>
	 *
	 * @return - String
	 */
	public String start() {
		StringBuilder temp = new StringBuilder();

		temp.append(this.end());
		temp.append("Start : " + this.sourceFile.getName() +"\n");
		temp.append(this.end());

		return temp.toString();
	}

	/**
	 * <p>end.</p>
	 *
	 * @return - String used for delimiting
	 */
	public String end() {
		return "--------------------------------------------------------------------------\n";
	}

	//endregion

	//region SourceFile Accessors
	/**
	 * <p>getBareFileName.</p>
	 *
	 * @return the file name (without the extension) from the source file
	 */
	public String getBareFileName()
	{
		if (this.getFileExt()=="")
			return this.sourceFile.getName().replaceAll(this.getFileExt(),"");
		else
			return this.sourceFile.getName().replaceAll("."+this.getFileExt(),"");
	}

	/**
	 * <p>getFileName.</p>
	 *
	 * @return the file name (with the extension) from the source file
	 */
	public String getFileName()
	{
		return this.sourceFile.getName();
	}

	/**
	 * <p>getFilePath.</p>
	 *
	 * @return the file path from the source file
	 */
	public String getFilePath()
	{
		return this.sourceFile.getAbsolutePath().replace(this.sourceFile.getName(), "");
	}

	/**
	 * <p>getFileExt.</p>
	 *
	 * @return the file extenion from the source file
	 */
	public String getFileExt()
	{
		if (periodDelimitedPath.length<2)
			return "";
		else
			return periodDelimitedPath[periodDelimitedPath.length-1];
	}
	//endregion

	//endregion
	
}
