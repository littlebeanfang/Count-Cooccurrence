package io;

import java.util.*;
import java.io.*;

public class HashMapWriter {
	private HashMap hashMap = null;

	public HashMapWriter(HashMap hashMap) throws Exception {
		this.hashMap = hashMap;
	}

	public void writeTo(String outputPath) throws IOException {
//		System.out.println("path:"+outputPath);
		File outputFile = new File(outputPath);
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
		BufferedWriter bufferedWriter = new BufferedWriter(writer);
		for (Object key : hashMap.keySet()) {
			Object value = hashMap.get(key);
			bufferedWriter.write(key.toString() + "\t" + value.toString() + "\n");
		}
		bufferedWriter.flush();
		bufferedWriter.close();
	}
}