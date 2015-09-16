package Test;

import path.Path;
import Bean.MemoryTestOfNetwork;

public class MemoryTest {
	public static void main(String args[]) throws Exception {
		MemoryTestOfNetwork test = new MemoryTestOfNetwork();
//		test.ReadMap(Path.enTitle2zhTitleFile);
//		System.gc();
		test.ReadGraph(Path.en_linksFileSplit00);
		System.gc();
		test.ReadGraph(Path.zh_linksFile);
		System.gc();
//		System.out.println("Raw package:");
//		test.ReadMap(Path.en_id2titleFile);
//		System.gc();
//		test.ReadMap(Path.zh_id2titleFile);
//		System.gc();
//		test.ReadMap(Path.en_linksFile);
//		System.gc();
//		test.ReadMap(Path.zh_linksFile);
//		System.gc();
//		test.ReadMap(Path.enId2zhIdFile);
	}
}
