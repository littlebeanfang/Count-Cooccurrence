package count;

import io.HashMapReader;

import java.util.*;
import java.io.*;

import path.*;
import synset.*;



public class CountTlCoOccur {
	HashMap<String, Integer> tlc = new HashMap<String, Integer>();

	public CountTlCoOccur() throws Exception {
		Synset synset = new Synset(CommonPath.selectSynsetPath);
		HashSet<String> voca = synset.getAllVoca();
		HashMap<String, Integer> titleToId = new HashMapReader<String, Integer>(CommonPath.pageMapLinePath, 1, "", 1).getHashMap();
		HashMap<Integer, String> idToTitle = new HashMapReader<Integer, String>(CommonPath.pageMapLinePath, 0, 1, "").getHashMap();
		HashSet<Integer> vocaId = new HashSet<Integer>();
		for (String word : voca) {
			if (titleToId.containsKey(word)) {
				vocaId.add(titleToId.get(word));
			}
		}
		File inputFile = new File(CommonPath.pageLinkPath);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);

		String line = null;
		int lineNum = 0;
		while ((line = bufferedReader.readLine()) != null) {
			++lineNum;
			if (lineNum % 10000000 == 0) {
				System.out.println(lineNum);
			}
			String parts[] = line.split("\t");
			int id1 = Integer.parseInt(parts[0]);
			int id2 = Integer.parseInt(parts[1]);
			if (vocaId.contains(id1) && vocaId.contains(id2)) {
				String mainForm1 = synset.getMainForm(idToTitle.get(id1));
				String mainForm2 = synset.getMainForm(idToTitle.get(id2));
				if (mainForm1.compareTo(mainForm2) < 0) {
					String pair = mainForm1 + "\t" + mainForm2;
					if (!tlc.containsKey(pair)) {
						tlc.put(pair, 0);
					}
					tlc.put(pair, tlc.get(pair) + 1);
				}
				if (mainForm1.compareTo(mainForm2) > 0) {
					String pair = mainForm2 + "\t" + mainForm1;
					if (!tlc.containsKey(pair)) {
						tlc.put(pair, 0);
					}
					tlc.put(pair, tlc.get(pair) + 1);
				}
			}
		}
	}

	public HashMap<String, Integer> getTlc() {
		return tlc;
	}
}