package com.backend;

import lombok.Getter;
import lombok.NonNull;
import com.frontend.overhead.CompilerError;
import com.frontend.overhead.arguments;
import com.middleend.parser.sin.sin;
import com.middleend.parser.sin.sinType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * <p>codegen class.</p>
 *
 * @author franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 *
 * This class is the abstraction layer used for generating code by talking to the writer class.
 * The parser never actually directly writes anything to the file, but delegates to this class.
 */
public class codegen {

    //region ClassFields
    @Getter
    private File llfile;
    private String location;
    @Getter
    private writer out;

    private Queue<String> globalVariables;
    private Stack<ArrayList<String>> runningMethods;
    private Queue<ArrayList<String>> finalizedMethods;

    private arguments args;

    //endregion

    /**
     * <p>Constructor for codegen.</p>
     *
     * @param argz       the arguments loaded into the program
     * @throws com.frontend.overhead.CompilerError thrown error
     */
    public codegen(arguments argz) throws CompilerError {

        this.globalVariables=new LinkedList<String>();
        this.runningMethods=new Stack<ArrayList<String>>();
        this.finalizedMethods=new LinkedList<ArrayList<String>>();

        this.args=argz;

        this.location = this.args.isDebug()
                ? (System.getProperty("user.dir") + this.args.getFileSplit() + "target" + this.args.getFileSplit() + "manual_test" + this.args.getFileSplit())
                : this.args.getFilePath();

        if (this.args.isDebug()) {
            if (new File(this.location).exists())
                new File(this.location).delete();
            new File(this.location).mkdir();
        }
    }

    //region Handling
    //region Creation

    /**
     * @param sourceCodeName test
     *
     * @return test
     */
    private boolean createSource(String sourceCodeName) {

        try {
            llfile.createNewFile();
            llfile.setExecutable(true, true);

            out = new writer(new FileWriter(llfile.getAbsolutePath()));

            out.comment("/*************************************");
            out.comment("Source Code : " + sourceCodeName + ".src");
            out.comment("File Created :" + llfile.getName());
            out.comment("If there are any issues with this generation of code,");
            out.comment("\tplease contact Professor.");
            out.comment("*///**********************************");

            globalVariables.stream().forEach(out::append);

            out.newLine();

            finalizedMethods.stream().forEach(lyst->lyst.stream().forEach(out::append));

        }
        catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * @param location where to make the make file
     * @return boolean whether the makefile was generated successfully
     */
    private boolean createMake(String location) {
        try {
            File makeFile = new File(location + "Makefile");
            BufferedWriter makeWrite = new BufferedWriter(new FileWriter(makeFile.getAbsolutePath()));
            
            
            String baseName = args.getBareFileName();
            String newName = baseName+".ll";

            makeWrite.write("default:: build");
            makeWrite.newLine();
            makeWrite.newLine();
            makeWrite.write("build: " + newName);
            makeWrite.newLine();
            makeWrite.write("\tllc -o " + baseName + ".s " + newName);
            makeWrite.newLine();
            makeWrite.write("\tgcc -o " + baseName + " " + baseName + ".s");
            makeWrite.newLine();
            if (this.args.isDebug())
            {
                makeWrite.write("\t./"+baseName);
                makeWrite.newLine();
            }
            makeWrite.newLine();
            makeWrite.write("run: " + baseName);
            makeWrite.newLine();
            makeWrite.write("\t./" + baseName);
            makeWrite.newLine();
            makeWrite.newLine();
            makeWrite.write("clean: " + llfile.getName());
            makeWrite.newLine();
            makeWrite.write("\trm " + baseName);
            makeWrite.newLine();
            makeWrite.write("\trm " + baseName+".s");

            makeWrite.flush();
            makeWrite.close();

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    //endregion

    /**
     * <p>cleanUp.</p>
     *
     * @return test
     * @throws com.frontend.overhead.CompilerError test
     */
    public boolean cleanUp() throws CompilerError
    {
        this.llfile = new File(this.location + this.args.getBareFileName() + ".ll");


        if (! createSource(this.args.getBareFileName())) throw new CompilerError("Issue within llLibCodegen: Creating the source file.");

        if (! createMake(this.location)) throw new CompilerError("Issue within creating the MakeFile");

        try {
            out.flush();
            out.close();
        }catch(Exception e)
        {
            throw new CompilerError("Issue with closing the source file");
        }

        return true;
    }
    //endregion

    //region PublicMethods


    /**
     * <p>generateDeclare.</p>
     *
     * @param pennance {@literal -} the sin, value being declared
     */
    public void generateDeclare(@NonNull sin pennance)
    {
        if (!pennance.getIsGlobal())
            this.getCurrentMethod().add("\t"+pennance.irVarDecl());
        else
            this.globalVariables.add(pennance.irVarDecl());
    }

    /**
     * <p>generateMethodCall.</p>
     *
     * @param arguments passed into the procedure call
     * @param method the name of the method being called
     * @param paramTypes the types of parameter specifiers, in out inout
     * @throws com.frontend.overhead.CompilerError in case there is a raw value being used for a out or inout argument
     */
    public void generateMethodCall(@NonNull String method, ArrayList<sin> arguments, ArrayList<sinType.paramType> paramTypes, int lineNumber) throws CompilerError
    {
        StringBuilder call=new StringBuilder();

        call.append("call void @");
        call.append(method);
        call.append("(");
        boolean addedArgs=false;
        for (int ktr=0;ktr<arguments.size();ktr++)
        {
            sinType.paramType currentParam=paramTypes.get(ktr);
            sin currentArg=arguments.get(ktr);

            if (addedArgs)
                call.append(',');

            if (!currentParam.equals(sinType.paramType.IN) && currentArg.getVal()!=null)
                throw new CompilerError("Raw values ("+currentArg.getVal()+") aren't allowed to be used as an "+currentParam.name()+" argument",method,lineNumber);

            call.append(currentArg.irParam(currentParam));

            addedArgs=true;
        }
        call.append(")");

        getCurrentMethod().add("\t"+call.toString());
    }


    /**
     * <p>startProc.</p>
     *
     * @param proc {@literal -} the sin used to start the procedure or program
     */
    public void startProc(@NonNull sin proc)
    {

        runningMethods.push(new ArrayList<String>());

        StringBuilder methodDecl=new StringBuilder("define ");

        if (proc.getName().equals("main"))
            methodDecl.append("i32 @main()");
        else
        {
            methodDecl.append("void @");

            methodDecl.append(proc.getName());

            ArrayList<String> params=new ArrayList<String>();

            if (!proc.getTable().getParams().isEmpty())
                params=new ArrayList<String>(proc.getTable().getParams().stream().map(paramName->proc.getTable().get(paramName).irParam(null)).collect(Collectors.toList()));

            methodDecl.append("(");

            if (!params.isEmpty())
               methodDecl.append(params.stream().collect(Collectors.joining(", ")));

            methodDecl.append(")");
        }

        getCurrentMethod().add(methodDecl.toString());
        getCurrentMethod().add("{");
    }


    /**
     * <p>endProc.</p>
     *
     * @param isMain an indication as to whether or not the program is exiting the main function
     */
    public void endProc(boolean isMain)
    {
        if (isMain)
            getCurrentMethod().add("\tret i32 0");
        else
            getCurrentMethod().add("\tret void");

        getCurrentMethod().add("}");

        finalizedMethods.add(runningMethods.pop());
    }

    //endregion

    //region PrivateMethods

    /**
     * <p>getCurrentMethod</p>
     * @return the current method, aka the current list of strings
     */
    private ArrayList<String> getCurrentMethod()
    {
        return this.runningMethods.peek();
    }

    //endregion
}
