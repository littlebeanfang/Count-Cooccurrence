package synset;

import java.util.*;
import java.io.*;

import BeanUtil.BeanReader;
import path.CommonPath;


public class Synset {
	//subpage_mainpage
	private HashMap<String, String> synset = new HashMap<String, String>();
	public Synset(String synsetfile) throws Exception {
		String spliter="\t";
		BeanReader synreader=new BeanReader(synsetfile);
		String line="";
		while((line=synreader.readLine())!=null){
//			System.out.println(line);
			String columns[]=line.split(spliter);
			String subpage=columns[0];
			String mainpage=columns[1];
			synset.put(subpage, mainpage);
		}
		System.out.println("Synset reading done, size:"+synset.size());
		synreader.close();
	}
	

	public HashSet<String> getAllVoca() {
		HashSet<String> ret=new HashSet<String>();
		for(String key:synset.keySet()){
			ret.add(key);
		}
		return ret;
	}

	public String getMainForm(String word) {
		return synset.get(word);
	}
}