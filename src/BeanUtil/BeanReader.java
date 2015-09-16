package BeanUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.sound.sampled.Line;

/**
 * UTF-8 encoding file reader
 * 
 * @author Bean
 *
 */
public class BeanReader {
	private BufferedReader breader;
	private static String encoding = "UTF-8";
	private static String commentPrefix = "##*";

	public BeanReader(String filename) throws IOException {
		this(filename, encoding);
	}

	public BeanReader(String filename, String encoding) throws IOException,
			FileNotFoundException {
		this.encoding = encoding;
		InputStreamReader isr = new InputStreamReader(new FileInputStream(
				filename), encoding);
		breader = new BufferedReader(isr);
		System.out.println("Reader for file:" + filename);
	}

	public String readLine() throws IOException {
		String line;
		while ((line = breader.readLine()) != null) {
			if (line.startsWith(commentPrefix)) {
				continue;
			} else {
				break;
			}
		}
		return line;
	}

	public String[] readSplitLine(String splitter) throws IOException {
		String line=null;
		while ((line = breader.readLine()) != null) {
			if (line.startsWith(commentPrefix)) {
				continue;
			} else {
				break;
			}
		}
		if(line==null){
			return null;
		}
		return line.split(splitter);
	}

	public String[] readSplitLine() throws IOException {
		return this.readSplitLine("\t");
	}

	public void close() throws IOException {
		breader.close();
	}
}
