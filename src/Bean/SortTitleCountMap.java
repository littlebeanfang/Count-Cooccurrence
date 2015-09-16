package Bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BeanUtil.DoubleValueMapSorter;
import path.CommonPath;

public class SortTitleCountMap {
	public HashMap<String, Double> ReadStringDoubleMapFromFile(String stringDoubleMapFile) throws IOException{
		HashMap<String, Double> str_dou_map=new HashMap<String, Double>();
		FileInputStream fis=new FileInputStream(stringDoubleMapFile);
		InputStreamReader isr=new InputStreamReader(fis,"utf-8");
		BufferedReader reader=new BufferedReader(isr);
		String lineString="";
		while((lineString=reader.readLine())!=null){
			//System.out.println(lineString);
			String columns[]=lineString.split("\t");
			String str=columns[0];
			String dou=columns[1];
			//System.out.println("synset:"+subtitle+"\t"+maintitle);
			str_dou_map.put(str, Double.parseDouble(dou));
		}
		reader.close();
		//System.out.println("克兰布鲁克 contain?:"+synset.containsKey("克兰布鲁克"));
		return str_dou_map;
	}
	public void process(String pageMapLineTxt, String pageWeigthFile, int selectTitleCount,String selectedTitleFile) throws IOException{
		Util test=new Util();
		HashMap<String, String> ID_TitleHashMap=test.ReadSynsetFromFile(pageMapLineTxt);
		HashMap<String, Double> ID_WeightHashMap=ReadStringDoubleMapFromFile(pageWeigthFile);
		DoubleValueMapSorter sorter=new DoubleValueMapSorter();
		List<Map.Entry<String,Double>> sortedList=sorter.SortCountMap(ID_WeightHashMap);
		File selecTitlFile=new File(selectedTitleFile);
		if(!selecTitlFile.exists()){
			selecTitlFile.createNewFile();
		}
		FileWriter writer=new FileWriter(selecTitlFile);
		int count=0;
		for(Map.Entry<String,Double> entry:sortedList){
			if(count>selectTitleCount)
				break;
			String ID=entry.getKey();
			String Title=ID_TitleHashMap.get(ID);
			writer.write(Title+"\n");
			System.out.println("Weight:"+entry.getValue());
			count++;
		}
		writer.close();
	}
}
