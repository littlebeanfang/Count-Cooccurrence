package Graph;

import java.io.IOException;
import java.util.*;
import java.io.*;

import path.Path;


import bottomLevel.ds.RankedObject;
import bottomLevel.fileIO.CollectionFile;
import bottomLevel.fileIO.GraphFile;
import bottomLevel.fileIO.HashMapFile;
import bottomLevel.others.PageRanker;

public class Preprocessing {
	final String BANDir = Path.BANDir;
	final String BAN_rawdata_Dir = Path.BANDir + "raw/";

	public Preprocessing() throws IOException, Exception {
		HashMap<String, String> en_id2title = new HashMapFile(BAN_rawdata_Dir + "en_id2title.txt").read(0, String.class, String.class);
		HashMap<String, String> zh_title2id = new HashMapFile(BAN_rawdata_Dir + "zh_id2title.txt").read(1, String.class, String.class);
		HashMap<String, String> co_en_id2title = new HashMap<String, String>();
		HashMap<String, String> co_zh_id2title = new HashMap<String, String>();
		HashMap<String, String> co_langlinks = new HashMap<String, String>();
		HashSet<String> co_en_links = new HashSet<String>();
		HashSet<String> co_zh_links = new HashSet<String>();

		File inputFile = new File(BAN_rawdata_Dir + "langlinks.txt");
		InputStreamReader reader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			String parts[] = line.split("\t");
			if (parts.length < 3) {
				continue;
			}
			String en_id = parts[0];
			String zh_title = parts[2];
			if (en_id2title.containsKey(en_id) && zh_title2id.containsKey(zh_title)) {
				String en_title = en_id2title.get(en_id);
				String zh_id = zh_title2id.get(zh_title);
				co_en_id2title.put(en_id, en_title);
				co_zh_id2title.put(zh_id, zh_title);
				co_langlinks.put(en_title, zh_title);
			}
		}

		inputFile = new File(BAN_rawdata_Dir + "en_links.txt");
		reader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
		bufferedReader = new BufferedReader(reader);
		line = null;
		while ((line = bufferedReader.readLine()) != null) {
			String parts[] = line.split("\t");
			String id1 = parts[0];
			String id2 = parts[1];
			if (co_en_id2title.containsKey(id1) && co_en_id2title.containsKey(id2)) {
				co_en_links.add(co_en_id2title.get(id1) + "\t" + co_en_id2title.get(id2));
			}
		}

		inputFile = new File(BAN_rawdata_Dir + "zh_links.txt");
		reader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
		bufferedReader = new BufferedReader(reader);
		line = null;
		while ((line = bufferedReader.readLine()) != null) {
			String parts[] = line.split("\t");
			String id1 = parts[0];
			String id2 = parts[1];
			if (co_zh_id2title.containsKey(id1) && co_zh_id2title.containsKey(id2)) {
				co_zh_links.add(co_zh_id2title.get(id1) + "\t" + co_zh_id2title.get(id2));
			}
		}

		new HashMapFile(BANDir + "co_en_id2title.txt").writeFrom(co_en_id2title);
		new HashMapFile(BANDir + "co_zh_id2title.txt").writeFrom(co_zh_id2title);
		new HashMapFile(BANDir + "en2zh.txt").writeFrom(co_langlinks);
		new CollectionFile(BANDir + "en_network.txt").writeFrom(co_en_links);
		new CollectionFile(BANDir + "zh_network.txt").writeFrom(co_zh_links);

		System.out.println(en_id2title.size());
		System.out.println(zh_title2id.size());
		System.out.println(co_langlinks.size());
	}
}
