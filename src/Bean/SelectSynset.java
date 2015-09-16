package Bean;

import java.io.IOException;
import java.util.HashMap;

import BeanUtil.BeanReader;
import BeanUtil.BeanWriter;

public class SelectSynset {
	public void select(String wholesynsetfile, String selecttitlefile,
			String selectsynsetfile) throws IOException {
		Util synreader = new Util();
		HashMap<String, String> synset = synreader
				.ReadSynsetFromFile(wholesynsetfile);
		BeanWriter writer = new BeanWriter(selectsynsetfile,
				"This file is generated by 30000 select title, and rewrite synset file.");
		BeanReader titleReader=new BeanReader(selecttitlefile);
		String line;
		while((line=titleReader.readLine())!=null){
//			System.out.println("title:"+line);
			if(synset.containsKey(line)){
//				System.out.println("write file");
				writer.writeln(line+"\t"+synset.get(line));
			}else{
				System.out.println("Error:"+line+" not in synset!");
			}
		}
		titleReader.close();
		writer.close();
	}
}
