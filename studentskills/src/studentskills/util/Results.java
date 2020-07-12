package studentskills.util;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Used to print contents to stdout and an output file
 * 
 * @author Harshit Vadodaria
 *
 */
public class Results implements ResultsI, FileDisplayInterface, StdoutDisplayInterface, Closeable {

	final protected Writer stdOut, fileOut;
	final protected String filename;
	final protected Queue<String> fileOutBuffer, stdOutBuffer;
	final protected boolean buffered;

	/**
	 * Equivalent to the constructor {@link #Results(String, boolean)
	 * Results(filename, true)}}
	 * 
	 * @param filename {@code String} containing the name of the output file
	 */
	public Results(String filename) {
		this(filename, true);
	}

	/**
	 * Setting {@code buffered} to {@code true} will enable buffering while printing
	 * contents to output file and stdout. Calling the {@link #flush()} method
	 * flushes the buffer to the file and stdout
	 * 
	 * @param filename {@code String} containing the name of the output file
	 * @param buffered {@code boolean} Indicate whether buffered is to be used
	 */
	public Results(String filename, boolean buffered) {
		this.buffered = buffered;
		this.stdOut = this.initStdOutWriter();
		this.fileOut = this.initFileWriter(filename);
		this.filename = filename;
		this.fileOutBuffer = !buffered ? null : new LinkedList<>();
		this.stdOutBuffer = !buffered ? null : new LinkedList<>();
	}

	protected void write(Writer out, String str) throws IOException {
		out.write(str);
		out.flush();
	}

	protected void writeLn(Writer out) throws IOException {
		out.flush();
	}

	@Override
	public void printToStdOut(String s) {
		try {
			if (!buffered)
				write(stdOut, s);
			else
				stdOutBuffer.add(s);
		} catch (IOException e) {
			throw new RuntimeException("Failed to write to stdout", e);
		}
	}

	@Override
	public void printToFile(String s) {
		try {
			if (!buffered)
				write(fileOut, s);
			else
				fileOutBuffer.add(s);
		} catch (IOException e) {
			throw new RuntimeException("Failed to write to File", e);
		}
	}

	@Override
	public void printLn(String printStr) {
		printToStdOut(printStr + "\n");
		printToFile(printStr + "\n");
	}

	@Override
	public void print(String printStr) {
		printToStdOut(printStr);
		printToFile(printStr);
	}

	@Override
	public Writer initStdOutWriter() {
		return new PrintWriter(System.out);
	}

	@Override
	public Writer initFileWriter(String filename) {
		try {
			return new FileWriter(filename);
		} catch (IOException e) {
			throw new RuntimeException("Failed to open file [" + filename + "] for writing", e);
		}
	}

	protected void flushBuffer(Writer out, Queue<String> buffer) throws IOException {
		while (!buffer.isEmpty())
			write(out, buffer.poll());
	}

	public void flush() throws IOException {
		if (!buffered)
			return;
		flushBuffer(fileOut, fileOutBuffer);
		flushBuffer(stdOut, stdOutBuffer);
	}

	@Override
	public void close() {
		try {
			stdOut.flush();
			stdOut.close();
			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			throw new RuntimeException("Failed to close results", e);
		}
	}

	@Override
	public String toString() {
		return "{stdOut: " + stdOut + ", fileOut: " + fileOut + "}";
	}

}
