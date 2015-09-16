package Bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import path.CommonPath;

public class Util {
	public void GetMainTitle(String PageMapLineTxtFile, String synsetfile) throws IOException{
		//DS
		HashMap<String, String> subpage_mainpage=new HashMap<String,String>();
		HashMap<String, String> id_titlemap = new HashMap<String,String>();
		HashSet<String> mainSet= new HashSet<String>();
		
		BufferedReader reader=new BufferedReader(new FileReader(PageMapLineTxtFile));
		File out=new File(synsetfile);
		if(!out.exists()){
			out.createNewFile();
		}
		FileWriter writer=new FileWriter(out);
		String line="";
		while((line=reader.readLine())!=null){
			String columns[]=line.split("\t");
			String curpageid=columns[0];
			String curpagetitle=columns[1];
			String mainpageid=columns[2];
			this.AddElemToMap(curpageid, mainpageid, subpage_mainpage);
			this.AddElemToMap(curpageid, curpagetitle, id_titlemap);
			mainSet.add(mainpageid);
		}
		
		for(String sub:subpage_mainpage.keySet()){
			String subtitle=this.GetTitleById(sub, id_titlemap);
			String mainid=subpage_mainpage.get(sub);
			String maintitle=this.GetTitleById(mainid, id_titlemap);
			writer.write(subtitle+"\t"+maintitle+"\n");
		}
		System.out.println("Main Page Num:"+mainSet.size()+"\n"
				+ "Total Page Num:"+id_titlemap.size());
		reader.close();
		writer.close();
	}
	private void AddElemToMap(String key, String value, HashMap<String, String> map){
		if(map.containsKey(key)){
			String oldvalue=map.get(key);
			map.put(key, oldvalue+"\t"+value);
		}else{
			map.put(key, value);
		}
	}
	private void AddElemToMap(String key, HashMap<String, Integer> map){
		if(map.containsKey(key)){
			int oldvalue=map.get(key);
			map.put(key, oldvalue+1);
		}else{
			map.put(key, 1);
		}
	}
	private String GetTitleById(String id, HashMap<String, String> id_titlemap){
		return id_titlemap.get(id);
	}
	private HashMap<String, Integer> PopularityTitleCountFromSynset(String WikiPlainTextFile, HashMap<String, String> synset) throws IOException{
		HashMap<String, Integer> title_count=new HashMap<String,Integer>();
		BufferedReader wikiReader=new BufferedReader(new FileReader(WikiPlainTextFile));
		String line="";
		int count = 0 ;
		System.out.println("==========PopularityTitleCountFromSynset===========");
		while((line=wikiReader.readLine())!=null){
			count++;
			if(count%10000==0&&CommonPath.printlog){
				System.out.println("count:"+count);
			}
			if(line.startsWith("<doc id=")){
				line=wikiReader.readLine();
				line=wikiReader.readLine();//main body context
			}
			for(String key:synset.keySet()){
				if(line.contains(key)){
					//System.out.println("key:"+key);
					//count on the main page, instead of the sub pages
					this.AddElemToMap(synset.get(key), title_count);
				}
			}
		}
		System.out.println("==========End===========");
		wikiReader.close();
		return title_count;
	}
	public HashMap<String, String> ReadSynsetFromFile(String synsetFile) throws IOException{
		//!must set encoding, cross platform (windows and linux)
		HashMap<String, String> synset=new HashMap<String, String>();
		FileInputStream fis=new FileInputStream(synsetFile);
		InputStreamReader isr=new InputStreamReader(fis,"utf-8");
		BufferedReader reader=new BufferedReader(isr);
		String lineString="";
		while((lineString=reader.readLine())!=null){
			//System.out.println(lineString);
			String columns[]=lineString.split("\t");
			String subtitle=columns[0];
			String maintitle=columns[1];
			//System.out.println("synset:"+subtitle+"\t"+maintitle);
			synset.put(subtitle, maintitle);
		}
		reader.close();
		//System.out.println("克兰布鲁克 contain?:"+synset.containsKey("克兰布鲁克"));
		return synset;
	}
	
	private List<Map.Entry<String,Integer>> SortCountMap(HashMap<String, Integer> popmap){
		List<Map.Entry<String,Integer>> list=new ArrayList<>();
		list.addAll(popmap.entrySet());  
        ValueComparator vc=new ValueComparator();  
        Collections.sort(list,vc);
		return list;  
	}
	private void WritePopFile(String PopWirteFile, List<Map.Entry<String, Integer>> poplist) throws IOException{
		File outFile=new File(PopWirteFile);
		if(!outFile.exists()){
			outFile.createNewFile();
		}
		FileWriter writer=new FileWriter(outFile);
		for(Map.Entry<String, Integer> entry:poplist){
			writer.write(entry.getKey()+"\t"+entry.getValue()+"\n");
		}
		writer.close();
	}
	public void TitlePopularity(String SynsetFile, String PlainTextFile, String PopWirteFile) throws IOException{
		double start,end=0;
		
		if(CommonPath.printlog){
			start=System.currentTimeMillis();
		}
		HashMap<String, String> synset=this.ReadSynsetFromFile(SynsetFile);
		if(CommonPath.printlog){
			end=System.currentTimeMillis();
			System.out.println("Reading synset done, Time:"+(end-start)/1000+" s,size:"+synset.size());
		}
		
		if(CommonPath.printlog){
			start=System.currentTimeMillis();
		}
		HashMap<String, Integer> popmap=this.PopularityTitleCountFromSynset(PlainTextFile, synset);
		if(CommonPath.printlog){
			end=System.currentTimeMillis();
			System.out.println("Popularity count done, Time:"+(end-start)/1000+" s, popmap size:"+popmap.size());
		}
		
		if(CommonPath.printlog){
			start=System.currentTimeMillis();
		}
		List<Map.Entry<String, Integer>> sortpop=this.SortCountMap(popmap);
		if(CommonPath.printlog){
			end=System.currentTimeMillis();
			System.out.println("Sort pop count map done, Time:"+(end-start)/1000+" s");
		}
		
		if(CommonPath.printlog){
			start=System.currentTimeMillis();
		}
		this.WritePopFile(PopWirteFile, sortpop);
		if(CommonPath.printlog){
			end=System.currentTimeMillis();
			System.out.println("write pop count map done, Time:"+(end-start)/1000+" s");
		}
	}
	private class ValueComparator implements Comparator<Map.Entry<String, Integer>>    
	{    
	    public int compare(Map.Entry<String, Integer> mp1, Map.Entry<String, Integer> mp2)     
	    {    
	        return mp2.getValue() - mp1.getValue();    
	    }    
	}  
}

