package bottomLevel.fileIO;

import java.util.*;
import java.io.*;

public class HashMapFile<KeyType, ValueType> {
	String filePath = null;

	public HashMapFile(String filePath) throws Exception {
		this.filePath = filePath;
	}

	public HashMap<KeyType, ValueType> read(int keyColumn, Class keyClass, Class valueClass) throws Exception {
		HashMap<KeyType, ValueType> hashMap = new HashMap<KeyType, ValueType>();
		readTo(hashMap, keyColumn, keyClass, valueClass);
		return hashMap;
	}
	public HashMap<KeyType, ValueType> read(int keyColumn, int valueColumn,Class keyClass, Class valueClass) throws Exception {
		HashMap<KeyType, ValueType> hashMap = new HashMap<KeyType, ValueType>();
		readTo(hashMap, keyColumn,valueColumn, keyClass, valueClass);
		return hashMap;
	}

	public void readTo(HashMap<KeyType, ValueType> hashMap, int keyColumn, Class keyClass, Class valueClass) throws Exception {
		File inputFile = new File(filePath);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			String parts[] = line.split("\t");
			String keyString = parts[keyColumn];
			String valueString = parts[1 - keyColumn];
			KeyType key = null;
			ValueType value = null;
			if (keyClass == Integer.class) {
				key = (KeyType) Integer.valueOf(keyString);
			}
			if (keyClass == Double.class) {
				key = (KeyType) Double.valueOf(keyString);
			}
			if (keyClass == String.class) {
				key = (KeyType) keyString;
			}
			if (valueClass == Integer.class) {
				value = (ValueType) Integer.valueOf(valueString);
			}
			if (valueClass == Double.class) {
				value = (ValueType) Double.valueOf(valueString);
			}
			if (valueClass == String.class) {
				value = (ValueType) valueString;
			}
			hashMap.put(key, value);
		}
	}
	
	public void readTo(HashMap<KeyType, ValueType> hashMap, int keyColumn,int valueColumn, Class keyClass, Class valueClass) throws Exception {
		File inputFile = new File(filePath);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			String parts[] = line.split("\t");
			if(parts.length<3){
				continue;
			}
			String keyString = parts[keyColumn];
			String valueString = parts[valueColumn];
			KeyType key = null;
			ValueType value = null;
			if (keyClass == Integer.class) {
				key = (KeyType) Integer.valueOf(keyString);
			}
			if (keyClass == Double.class) {
				key = (KeyType) Double.valueOf(keyString);
			}
			if (keyClass == String.class) {
				key = (KeyType) keyString;
			}
			if (valueClass == Integer.class) {
				value = (ValueType) Integer.valueOf(valueString);
			}
			if (valueClass == Double.class) {
				value = (ValueType) Double.valueOf(valueString);
			}
			if (valueClass == String.class) {
				value = (ValueType) valueString;
			}
			hashMap.put(key, value);
		}
	}

	public void writeFrom(HashMap<KeyType, ValueType> hashMap) throws IOException {
		File outputFile = new File(filePath);
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