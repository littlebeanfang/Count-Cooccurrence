package BeanUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * UTF-8 writer, allow encoding and append
 * 
 * @author Bean
 *
 */
public class BeanWriter {
	private BufferedWriter bwriter;
	private static String encoding = "UTF-8";
	private static String commentPrefix = "##*";

	public BeanWriter(String filename, boolean append, String encoding,
			String filedescription) throws IOException {
		this.encoding = encoding;
		File outputFile = new File(filename);
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
				outputFile, append), encoding);
		bwriter = new BufferedWriter(osw);
		if (!append) {
			this.writeln(commentPrefix
					+ "================================================");
			this.writeln(commentPrefix + "==   File: " + filename);
			this.writeln(commentPrefix + "==   Description: " + filedescription);
			this.writeln(commentPrefix + "==   Codepath: "
					+System.getProperty("user.dir") +"  ");
			this.writeln(commentPrefix + "==   Generateby: "
					+ Thread.currentThread().getStackTrace()[3].getClassName()+"."
					+ Thread.currentThread().getStackTrace()[3].getMethodName()+"()");
			this.writeln(commentPrefix
					+ "================================================");
		}
		System.out.println("Writer for file:"+filename);
	}

	public BeanWriter(String filename, boolean append, String filedescription)
			throws IOException {
		this(filename, append, encoding, filedescription);
	}

	public BeanWriter(String filename, String filedescription)
			throws IOException {
		this(filename, false, encoding, filedescription);
	}

	public void write(String content) throws IOException {
		bwriter.write(content);
		bwriter.flush();
	}

	public void writeln(String content) throws IOException {
		bwriter.write(content);
		bwriter.write(System.getProperty("line.separator"));
		bwriter.flush();
	}

	public void writecomment(String comment) throws IOException {
		this.writeln(commentPrefix + comment);
		bwriter.flush();
	}

	public void close() throws IOException {
		bwriter.close();
	}
}
