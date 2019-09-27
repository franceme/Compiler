package com.backend;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * <p>writer class.</p>
 *
 * @author franceme
 * Class: Compiler Theory
 * Project: Custom Compiler
 *
 * This class is the actual writer class used to directly write to the file.
 * There are specialized methods created for only allowing certain actions to the file.
 */
public class writer extends BufferedWriter {

    /**
     * <p>Constructor for writer.</p>
     *
     * @param out {@literal -} the writer being used to start writing
     */
    public writer(Writer out) {
        super(out);
    }

    /** {@inheritDoc} */
    @Override
    public void newLine() {
        try {
            super.newLine();
        }
        catch (IOException e) {
            /* TODO - Capture Exception */
            e.printStackTrace();
        }
    }

    //region PublicMethods

    /**
     * <p>append.</p>
     *
     * @param line the current string to be written to the out file
     */
    public void append(String line) {
        try {
            super.write(line);
            super.newLine();
        }
        catch (Exception e) {
            /* TODO - Capture Exception */
        }
    }

    /**
     * <p>comment.</p>
     *
     * @param comment the comment
     */
    public void comment(String comment) {
        this.append("; " + comment);
    }

    //endregion
}
