package synset;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HashMapCounter {
	private HashMap<String, Integer> key_count=new HashMap<String, Integer>();
	public void AddCountToMap(String key){
		if(key_count.containsKey(key)){
			key_count.put(key, key_count.get(key)+1);
		}else{
			key_count.put(key, 1);
		}
	}
	public HashMap<String,Integer> getCounterMap(){
		return key_count;
	}
	public void WriteCountToFile(String writefile,String spliter) throws IOException{
		String encoding="UTF-8";
		File out=new File(writefile);
		if(!out.exists()){
			out.createNewFile();
		}
		FileWriter writer=new FileWriter(out);
		for(Map.Entry<String, Integer> entry:key_count.entrySet()){
			writer.write(entry.getKey()+spliter+entry.getValue());
			writer.flush();
		}
		writer.close();
	}
}
