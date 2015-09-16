package Test;

import java.io.IOException;

import BeanUtil.BeanReader;
import BeanUtil.BeanWriter;


public class IOPackTest {
	public void writerTest(String filename) throws IOException {
		BeanWriter writer = new BeanWriter(filename,
				"BeanReader & BeanWriter test");
		writer.write("test write...");
		writer.writeln("test writeln...");
		writer.writecomment("This is comment");
		writer.write("test write...");
		writer.writeln("test writeln...");
		writer.writecomment("This is comment");
		writer.close();
		
		BeanWriter writer2 = new BeanWriter(filename,true,
				"BeanReader & BeanWriter test");
		writer2.write("test write2...");
		writer2.writeln("test writeln2...");
		writer2.writecomment("This is comment2");
		writer2.write("test write2...");
		writer2.writeln("test writeln2...");
		writer2.writecomment("This is comment2");
		writer2.close();
	}
	
	public void readerTest(String filename) throws IOException{
		BeanReader reader=new BeanReader(filename);
		String line;
		while((line=reader.readLine())!=null){
			System.out.println(line);
		}
	}

	public static void main(String args[]) throws IOException {
		String testfile="beanwrite.txt";
		IOPackTest test=new IOPackTest();
		test.writerTest(testfile);
		test.readerTest(testfile);
	}
}
