package io;

import java.util.*;
import java.io.*;

public class HashMapReader<KeyType, ValueType> {
	private HashMap<KeyType, ValueType> hashMap = new HashMap<KeyType, ValueType>();

	public HashMapReader(String inputPath, int keyColumn, Object key, Object value) throws Exception {
		File inputFile = new File(inputPath);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			String parts[] = line.split("\t");
			String keyString = parts[keyColumn];
			String valueString = parts[1 - keyColumn];
			if (key instanceof Integer) {
				key = (KeyType) Integer.valueOf(keyString);
			}
			if (key instanceof Double) {
				key = (KeyType) Double.valueOf(keyString);
			}
			if (key instanceof String) {
				key = (KeyType) keyString;
			}
			if (value instanceof Integer) {
				value = (ValueType) Integer.valueOf(valueString);
			}
			if (value instanceof Double) {
				value = (ValueType) Double.valueOf(valueString);
			}
			if (value instanceof String) {
				value = (ValueType) valueString;
			}
			hashMap.put((KeyType) key, (ValueType) value);
		}
	}

	public HashMap<KeyType, ValueType> getHashMap() {
		return hashMap;
	}
}