package Bean;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import synset.HashMapCounter;
import synset.Synset;

public class TitleOccurCountFromSegmentFile {
	private Synset synset;
	private HashMapCounter title_count=new HashMapCounter();
	private void InitSynset(String synsetfile) throws Exception{
		synset=new Synset(synsetfile);
	}
	public void CountOccurFromSeg(String segfile,String synsetfile, String segsorttitlefile) throws Exception{
		String tokenspliter=" ";
		String labelspliter="/";
		String usrdiclabel="title";
		String encoding="UTF-8";
		
		InitSynset(synsetfile);
		InputStreamReader segisr=new InputStreamReader(new FileInputStream(segfile),encoding);
		BufferedReader segreader=new BufferedReader(segisr);
		
		
		String line="";
		int linecount=0;
		int titlecount=0;
		double start=System.currentTimeMillis();
		double end=0;
		while((line=segreader.readLine())!=null){
			//filter lines
			linecount++;
			if(linecount%10000==0){
				end=System.currentTimeMillis();
				System.out.println("line:"+linecount+",time:"+(end-start)/1000+" s.");
			}
			if(line.startsWith("<doc id=")||line.startsWith("</doc>")||line.equals(""))
				continue;
			//process line
			String[] tokens=line.split(tokenspliter);
			for(String token:tokens){
				String parts[]=token.split(labelspliter);
				if(parts.length!=2){
					//System.out.println("Error token:"+token);
				}else{
					String content=parts[0];
					String label=parts[1];
					if(label.equals(usrdiclabel)){
						titlecount++;
						title_count.AddCountToMap(content);
					}
				}
			}
		}
		System.out.println("total title count:"+titlecount);
		segreader.close();
		
		IntValueMapSorter sorter=new IntValueMapSorter();
		List<Map.Entry<String,Integer>> list=sorter.SortCountMap(title_count.getCounterMap());
		File out=new File(segsorttitlefile);
		if(!out.exists()){
			out.createNewFile();
		}
		FileWriter writer=new FileWriter(out);
		for(Entry entry:list){
			writer.write(entry.getKey()+"\t"+entry.getValue()+"\n");
		}
		writer.close();
		System.out.println("Title num:"+list.size());
	}
}
