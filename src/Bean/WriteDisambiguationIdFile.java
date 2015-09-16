package Bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class WriteDisambiguationIdFile {
	public void WriteIdFile(String endisambiguationFile,String zhdisambiguationFile,
			String enid2titlefile,String zhid2titlefile,
			String endisambiguationidfile,String zhdisambiguationidfile) throws IOException{
		//Init title2id map
		HashMap<String, String> entitle2idMap=new HashMap<String, String>();
		HashMap<String, String> zhtitle2idMap=new HashMap<String,String>();
		
		//Init IO
		BufferedReader enid2titleReader=new BufferedReader(new FileReader(enid2titlefile));
		BufferedReader zhid2titleReader=new BufferedReader(new FileReader(zhid2titlefile));
		BufferedReader endisamtitleReader=new BufferedReader(new FileReader(endisambiguationFile));
		BufferedReader zhdisamtitleReader=new BufferedReader(new FileReader(zhdisambiguationFile));
		File endisamidFile=new File(endisambiguationidfile);
		if(!endisamidFile.exists()){
			endisamidFile.createNewFile();
		}
		FileWriter endisamidWriter=new FileWriter(endisamidFile);
		File zhdisamidFile=new File(zhdisambiguationidfile);
		if(!zhdisamidFile.exists()){
			zhdisamidFile.createNewFile();
		}
		FileWriter zhdisamidWriter=new FileWriter(zhdisamidFile);
		
		//read title2id map
		String line;
		String id;
		String title;
		while((line=enid2titleReader.readLine())!=null){
			String columns[]=line.split("\t");
			id=columns[0];
			title=columns[1];
			entitle2idMap.put(title, id);
		}
		while((line=zhid2titleReader.readLine())!=null){
			String columns[]=line.split("\t");
			id=columns[0];
			title=columns[1];
			zhtitle2idMap.put(title, id);
		}
		//scan diam title file
		line=endisamtitleReader.readLine();//skip first line
		String subtitle;
		String maintitle;
		String subid;
		String mainid;
		int count=0;
		while((line=endisamtitleReader.readLine())!=null){
			String columns[]=line.split("\t");
			subtitle=columns[0];
			maintitle=columns[1];
			if(!entitle2idMap.containsKey(subtitle)){
				System.out.println(subtitle+" not in title2id file!");
				count++;
				continue;
			}else{
				subid=entitle2idMap.get(subtitle);
			}
			if(!entitle2idMap.containsKey(maintitle)){
				System.out.println(maintitle+" not in title2id file!");
				count++;
				continue;
			}else{
				mainid=entitle2idMap.get(maintitle);
			}
			endisamidWriter.write(subid+"\t"+mainid+"\n");
			endisamidWriter.flush();
		}
		System.out.println(count+" titles in total is not in title2id file");
		
		count=0;
		line=zhdisamtitleReader.readLine();//skip first line
		while((line=zhdisamtitleReader.readLine())!=null){
			String columns[]=line.split("\t");
			subtitle=columns[0];
			maintitle=columns[1];
			if(!zhtitle2idMap.containsKey(subtitle)){
				System.out.println(subtitle+" not in entitle2id file!");
				count++;
				continue;
			}else{
				subid=zhtitle2idMap.get(subtitle);
			}
			if(!zhtitle2idMap.containsKey(maintitle)){
				System.out.println(maintitle+" not in title2id file!");
				count++;
				continue;
			}else{
				mainid=zhtitle2idMap.get(maintitle);
			}
			zhdisamidWriter.write(subid+"\t"+mainid+"\n");
			zhdisamidWriter.flush();
		}
		System.out.println(count+" titles in total is not in zhtitle2id file");
		
		//close IO
		enid2titleReader.close();
		zhid2titleReader.close();
		endisamtitleReader.close();
		zhdisamtitleReader.close();
		endisamidWriter.close();
		zhdisamidWriter.close();
	}
	public static void main(String args[]) throws IOException{
		WriteDisambiguationIdFile test=new WriteDisambiguationIdFile();
		String dir="/home/bean/wiki/BAN/";
		String endisambiguationFile=dir+"disambiguation.txt";
		String zhdisambiguationFile=dir+"disambiguation_zh.txt";
		String enid2titlefile=dir+"raw/en_id2title.txt";
		String zhid2titlefile=dir+"raw/zh_id2title.txt";
		String endisambiguationidfile=dir+"en_disam_id.txt";
		String zhdisambiguationidfile=dir+"zh_disam_id.txt";
		test.WriteIdFile(endisambiguationFile, zhdisambiguationFile, enid2titlefile, zhid2titlefile, endisambiguationidfile, zhdisambiguationidfile);
	}
}
