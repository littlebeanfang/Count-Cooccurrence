package Bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import BeanUtil.BeanReader;

public class RewriteLanglinkGroupFile {
	public void RewriteLanglinksFile(String enid2titlefile,String zhid2titlefile,String langlinkfile,String en_submainidfile,
			String zh_submainidfile, String grouplanglinkfile,
			String en_submainid_countfilter,String zh_submainid_countfilter) throws IOException{
		String line;
		HashMap<String, String> enid2title = new HashMap<String, String>();
		BufferedReader enid2titleReader = new BufferedReader(new FileReader(
				enid2titlefile));
		while ((line = enid2titleReader.readLine()) != null) {
			String columns[] = line.split("\t");
			String id = columns[0];
			String title = columns[1];
			enid2title.put(id, title);
		}
		enid2titleReader.close();
		HashMap<String, String> zhid2title = new HashMap<String, String>();
		BufferedReader zhid2titleReader = new BufferedReader(new FileReader(
				zhid2titlefile));
		while ((line = zhid2titleReader.readLine()) != null) {
			String columns[] = line.split("\t");
			String id = columns[0];
			String title = columns[1];
			zhid2title.put(id, title);
		}
		zhid2titleReader.close();
		
		//Init map
		HashMap<String, String> en_subid_mainid=new HashMap<String, String>();
		HashMap<String, String> zh_subid_mainid=new HashMap<String,String>();
		HashMap<String, Integer> grouplanglink_count=new HashMap<String,Integer>();
		//Init IO
		BeanReader langlinkReader=new BeanReader(langlinkfile);
		BufferedReader en_submain_idReaer=new BufferedReader(new FileReader(en_submainidfile));
		BufferedReader zh_submain_idReader=new BufferedReader(new FileReader(zh_submainidfile));
		File glanglink=new File(grouplanglinkfile);
		if(!glanglink.exists()){
			glanglink.createNewFile();
		}
		FileWriter glanglinkWriter=new FileWriter(glanglink);
		File en_submain_filter=new File(en_submainid_countfilter);
		if(!en_submain_filter.exists()){
			en_submain_filter.createNewFile();
		}
		FileWriter new_en_submain_writer=new FileWriter(en_submain_filter);
		File zh_submain_filter=new File(zh_submainid_countfilter);
		if(!zh_submain_filter.exists()){
			zh_submain_filter.createNewFile();
		}
		FileWriter new_zh_submain_writer=new FileWriter(zh_submain_filter);
		
		//read subid_mainid map
		String subid;
		String mainid;
		while((line=en_submain_idReaer.readLine())!=null){
			String columns[]=line.split("\t");
			if(columns.length!=2){
				System.out.println(line);
				continue;
			}
			subid=columns[0];
			mainid=columns[1];
			en_subid_mainid.put(subid, mainid);
		}
		System.out.println("en_subid_size:"+en_subid_mainid.keySet().size()+","
				+ "en_mainid_size:"+new HashSet<String>(en_subid_mainid.values()).size());
		while((line=zh_submain_idReader.readLine())!=null){
			String columns[]=line.split("\t");
			if(columns.length!=2){
				System.out.println(line);
				continue;
			}
			subid=columns[0];
			mainid=columns[1];
			zh_subid_mainid.put(subid, mainid);
		}
		System.out.println("zh_subid_size:"+zh_subid_mainid.keySet().size()+","
				+ "zh_mainid_size:"+new HashSet<String>(zh_subid_mainid.values()).size());
		//scan langlinks file, write grouplanglink in map and count
		String enid;
		String zhid;
		String genid;
		String gzhid;
		String genzhid;
		int nullcount=0;
		while((line=langlinkReader.readLine())!=null){
			String columns[]=line.split("\t");
			enid=columns[0];
			zhid=columns[1];
			genid=en_subid_mainid.get(enid);
			gzhid=zh_subid_mainid.get(zhid);
			if(genid==null||gzhid==null){
//				System.out.println("enid:"+enid+",zhid:"+zhid+",genid:"+genid+",gzhid:"+gzhid+"\n"
//						+ "entitle:"+enid2title.get(enid)+",zhtitle:"+zhid2title.get(zhid));
				nullcount++;
				continue;
			}
			genzhid=genid+"\t"+gzhid;
			if(grouplanglink_count.containsKey(genzhid)){
				grouplanglink_count.put(genzhid, 1+grouplanglink_count.get(genzhid));
			}else{
				grouplanglink_count.put(genzhid, 1);
			}
		}
		System.out.println("nullcount:"+nullcount);
		//remove items in group langlinks by number filter
		Iterator iterator=grouplanglink_count.entrySet().iterator();
		int filtercount=2;
		HashSet<String> removekeyset=new HashSet<String>();
		while(iterator.hasNext()){
			Map.Entry<String, Integer> entry=(Map.Entry<String, Integer>) iterator.next();
			if(entry.getValue()<filtercount){
				removekeyset.add(entry.getKey());
			}
		}
		for(String key:removekeyset){
			grouplanglink_count.remove(key);
		}
		System.out.println("grouplanglink_count size:"+grouplanglink_count.size()+","
				+ "remove set size:"+removekeyset.size());
		//write group langlinks, if in group langlinkmap then write group
		//if not in group langlinkmap, write single item
		langlinkReader=new BeanReader(langlinkfile);
		HashSet<String> filterlanglinkSet=new HashSet<String>();
		while((line=langlinkReader.readLine())!=null){
			String columns[]=line.split("\t");
			enid=columns[0];
			zhid=columns[1];
			genid=en_subid_mainid.get(enid);
			gzhid=zh_subid_mainid.get(zhid);
			genzhid=genid+"\t"+gzhid;
			if(grouplanglink_count.containsKey(genzhid)){
				filterlanglinkSet.add(genzhid+"\n");
				new_en_submain_writer.write(enid+"\t"+genid+"\n");
				new_zh_submain_writer.write(zhid+"\t"+gzhid+"\n");
			}else{
				filterlanglinkSet.add(line+"\n");
			}
		}
		System.out.println(filterlanglinkSet.size());
		for(String filterline:filterlanglinkSet){
			glanglinkWriter.write(filterline);
		}
		//close IO
		langlinkReader.close();
		en_submain_idReaer.close();
		zh_submain_idReader.close();
		glanglinkWriter.close();
		new_en_submain_writer.close();
		new_zh_submain_writer.close();
	}
	public static void main(String args[]) throws IOException{
		RewriteLanglinkGroupFile test=new RewriteLanglinkGroupFile();
		String dongdir = "/home/bean/wiki/BAN/raw/";
		String langlinkfile = dongdir + "lang_idlinks.txt";
		String enid2titlefile = dongdir + "en_id2title.txt";
		String zhid2titlefile = dongdir + "zh_id2title.txt";
		String enlinkfile = dongdir + "en_links.txt";
		String zhlinkfile = dongdir + "zh_links.txt";
		String dongBANdir = "/home/bean/wiki/BAN/";
		String en_submainidfile = dongBANdir + "en_disam_id.txt";
		String zh_submainidfile = dongBANdir + "zh_disam_id.txt";
		String ensubmainfile = dongBANdir + "en_filter_disam_id.txt";
		String zhsubmainfile = dongBANdir + "zh_filter_disam_id.txt";
		String grouplanglinkfile = dongBANdir + "disamgroup_lang_idlinks.txt";

		String newenlinkfile = dongBANdir + "disamgroup_en_links.txt";
		String newzhlinkfile = dongBANdir + "disamgroup_zh_links.txt";
		test.RewriteLanglinksFile(enid2titlefile, zhid2titlefile, langlinkfile,
				en_submainidfile, zh_submainidfile, grouplanglinkfile,
				ensubmainfile, zhsubmainfile);
	}
}
