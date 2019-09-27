package com.middleend;

import com.middleend.frontendHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.frontend.overhead.CompilerError;
import com.frontend.overhead.arguments;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class frontendHandlerTest {

    //region Test Attributes

    //region Test Settings
    private final boolean isLinux= !System.getProperty("os.name").contains("Windows");
    private final String debugFiles= System.getProperty("user.dir") + (isLinux ? "/src/test/resources/" : "\\src\\test\\resources\\");

    private ArrayList<String> getFiles(arguments.resources resource) {
        ArrayList<String> lyst = new ArrayList<String>();
        for (File kai : new File(debugFiles + resource.name()).listFiles())
            if (kai.getName().endsWith(".src")) lyst.add(kai.getName());
        return lyst;
    }

    private void setArgz(String file,arguments.resources resource)
    {
        this.args=new arguments(new String[0],resource,file);
    }

    //endregion

    //region Test Fields
    protected ArrayList<String> currentFiles;
    protected HashMap<String,ArrayList<CompilerError>> currentErr;
    protected static frontendHandler frontend;
    protected arguments args;
    //endregion

    //endregion

    @Before
    public void setUp() throws Exception
    {
        this.currentFiles=new ArrayList<String>();
        this.currentErr=new HashMap<String,ArrayList<CompilerError>>();
    }

    @After
    public void tearDown() throws Exception
    {
        this.currentFiles=null;
        this.currentErr=null;
        this.args=null;
    }

    @Test
    public void massCorrectTest()
    {
        currentFiles=getFiles(arguments.resources.correct);

        assertEquals(13,currentFiles.size());

        for (String currentFile:currentFiles)
        {
            setArgz(currentFile,arguments.resources.correct);
            assertTrue(frontend.frontend(args).isEmpty());
        }
    }



    @Test
    public void massIncorrectTest()
    {
        currentFiles=getFiles(arguments.resources.incorrect);

        assertEquals(11,currentFiles.size());

        for (String currentFile:currentFiles)
        {
            setArgz(currentFile,arguments.resources.incorrect);
            currentErr.put(currentFile,frontendHandler.frontend(args));

            /**
            if (args.getFileName().equals("test3.src"))
                currentErr.get("test3.src").stream().forEach(System.out::println);
             **/
        }

        for (String key:currentErr.keySet())
            assertTrue(currentErr.get(key).size()>=1);

        assertEquals(11,currentErr.size());
    }
}